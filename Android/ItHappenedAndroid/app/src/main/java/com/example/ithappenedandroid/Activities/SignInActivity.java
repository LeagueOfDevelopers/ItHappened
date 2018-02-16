package com.example.ithappenedandroid.Activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.Models.RegistrationResponse;
import com.example.ithappenedandroid.Models.SynchronizationRequest;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Retrofit.ItHappenedApplication;
import com.example.ithappenedandroid.StaticInMemoryRepository;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.SignInButton;
import com.nvanbenschoten.motion.ParallaxImageView;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SignInActivity extends Activity {

    private final static String G_PLUS_SCOPE =
            "oauth2:https://www.googleapis.com/auth/plus.me";
    private final static String USERINFO_SCOPE =
            "https://www.googleapis.com/auth/userinfo.profile";
    private final static String EMAIL_SCOPE =
            "https://www.googleapis.com/auth/userinfo.email";
    private final static String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE + " " + EMAIL_SCOPE;
    private static final String TAG = "EXC";

    ParallaxImageView mainBackground;
    SignInButton signIn;
    TextView mainTitle;
    ProgressBar mainPB;

    Subscription regSub;
    Subscription syncSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mainPB = (ProgressBar) findViewById(R.id.mainProgressBar);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("UserId", "");
        if(id == "") {
            findControlsById();
            mainBackground.registerSensorManager();
            AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(3000);
            animation.setFillAfter(true);
            mainTitle.setAnimation(animation);

            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                            false, null, null, null, null);
                    startActivityForResult(intent, 228);
                }
            });
        }else{
            Intent intent = new Intent(this, UserActionsActivity.class);
            startActivity(intent);
        }
    }

    private void findControlsById(){

        signIn = (SignInButton) findViewById(R.id.signin);
        mainBackground = (ParallaxImageView) findViewById(R.id.mainBackground);
        mainTitle = (TextView) findViewById(R.id.mainTitle);

    }

    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data){


        if (requestCode == 228 && resultCode == RESULT_OK) {
            showLoading();
            final String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            AsyncTask<Void, Void, String> getToken = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {

                    String idToken = "";

                    try {
                        idToken = GoogleAuthUtil.getToken(SignInActivity.this, accountName,
                                SCOPES);
                        return idToken;

                    } catch (UserRecoverableAuthException userAuthEx) {
                        startActivityForResult(userAuthEx.getIntent(), 228);
                    } catch (IOException ioEx) {
                        //Log.d(TAG, "IOException");
                        hideLoading();
                        Toast.makeText(getApplicationContext(),"IOException",Toast.LENGTH_SHORT).show();
                    } catch (GoogleAuthException fatalAuthEx) {
                        hideLoading();
                        //Log.d(TAG, "Fatal Authorization Exception" + fatalAuthEx.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(),"Fatal Authorization Exception" + fatalAuthEx.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return idToken;
                }

                @Override
                protected void onPostExecute(String idToken) {
                    reg(idToken);
                }
            };

            getToken.execute(null, null, null);
        }
    }

    private void reg(String idToken){

        regSub = ItHappenedApplication.getApi().SignUp(idToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RegistrationResponse>() {
                    @Override
                    public void call(RegistrationResponse registrationResponse) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UserId", registrationResponse.getUserId());
                        editor.putString("Nick", registrationResponse.getUserNickname());
                        editor.putString("Url", registrationResponse.getPicUrl());
                        editor.putLong("NickDate", registrationResponse.getNicknameDateOfChange().getTime());
                        editor.commit();

                        SynchronizationRequest synchronizationRequest = new SynchronizationRequest(registrationResponse.getUserNickname(),
                                new Date(sharedPreferences.getLong("NickDate", 0)),
                                new StaticInMemoryRepository(getApplicationContext()).getInstance().GetTrackingCollection());

                        ItHappenedApplication.
                                getApi().SynchronizeData(registrationResponse.getUserId(), synchronizationRequest)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<SynchronizationRequest>() {
                                    @Override
                                    public void call(SynchronizationRequest sync) {
                                        saveDataToDb(sync.getTrackingCollection());

                                        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putLong("NickDate", sync.getNicknameDateOfChange().getTime());
                                        editor.commit();

                                        Toast.makeText(getApplicationContext(), "Синхронизировано", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), UserActionsActivity.class);
                                        startActivity(intent);
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        hideLoading();
                                        Log.e("RxSync", ""+throwable);
                                        Toast.makeText(getApplicationContext(), "Синхронизация не прошла!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideLoading();
                        Log.e("Reg", ""+throwable);
                        Toast.makeText(getApplicationContext(), "Разорвано подключение!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void saveDataToDb(List<Tracking> trackings){
        ITrackingRepository trackingRepository = new StaticInMemoryRepository(getApplicationContext()).getInstance();
        trackingRepository.SaveTrackingCollection(trackings);
    }

    private  void showLoading(){
        mainBackground.setVisibility(View.INVISIBLE);
        mainPB.setVisibility(View.VISIBLE);
        signIn.setVisibility(View.INVISIBLE);
        mainTitle.setVisibility(View.INVISIBLE);
    }

    private void hideLoading(){
        mainBackground.setVisibility(View.VISIBLE);
        mainPB.setVisibility(View.INVISIBLE);
        signIn.setVisibility(View.VISIBLE);
        mainTitle.setVisibility(View.VISIBLE);
    }

}