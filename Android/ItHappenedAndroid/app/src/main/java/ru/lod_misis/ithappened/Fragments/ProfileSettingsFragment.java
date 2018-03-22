package ru.lod_misis.ithappened.Fragments;

import android.accounts.AccountManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import ru.lod_misis.ithappened.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Models.RegistrationResponse;
import ru.lod_misis.ithappened.Models.SynchronizationRequest;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Retrofit.ItHappenedApplication;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ProfileSettingsFragment extends Fragment {

    TextView userMail;
    TextView userNickName;
    Button logOut;

    Subscription accountGoogleRx;

    ProgressBar syncPB;
    FrameLayout layoutFrg;

    private final static String G_PLUS_SCOPE =
            "oauth2:https://www.googleapis.com/auth/plus.me";
    private final static String USERINFO_SCOPE =
            "https://www.googleapis.com/auth/userinfo.profile";
    private final static String EMAIL_SCOPE =
            "https://www.googleapis.com/auth/userinfo.email";
    private final static String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE + " " + EMAIL_SCOPE;
    private static final String TAG = "SignIn";

    Button signIn;

    TextView editNickName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_settings, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Настройки профиля");

        syncPB = (ProgressBar) getActivity().findViewById(R.id.syncPB);
        layoutFrg = (FrameLayout) getActivity().findViewById(R.id.trackingsFrg);

        userMail =(TextView) getActivity().findViewById(R.id.mail);
        userNickName = (TextView) getActivity().findViewById(R.id.nickname);
        logOut = (Button) getActivity().findViewById(R.id.logout);
        editNickName = (TextView) getActivity().findViewById(R.id.editNickName);
        signIn = (Button) getActivity().findViewById(R.id.offlineSignIn);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        userMail.setText(sharedPreferences.getString("UserId", ""));
        userNickName.setText(sharedPreferences.getString("Nick", ""));

        if(sharedPreferences.getString("UserId", "").equals("Offline")){
            logOut.setVisibility(View.INVISIBLE);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                            false, null, null, null, null);
                    startActivityForResult(intent, 228);
                }
            });
        }else {
            signIn.setVisibility(View.INVISIBLE);
            logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogOutDailogFragment logout = new LogOutDailogFragment();
                    logout.show(getFragmentManager(), "Logout");
                }
            });
        }
        editNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNicknameDialogFragment dialogFragment = new EditNicknameDialogFragment();
                dialogFragment.show(getFragmentManager(), "editNickName");
            }
        });

    }

    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data){


        if (requestCode == 228 && resultCode == getActivity().RESULT_OK) {
            showLoading();
            final String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            AsyncTask<Void, Void, String> getToken = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {

                    String idToken = "";

                    try {
                        idToken = GoogleAuthUtil.getToken(getActivity().getApplicationContext(), accountName,
                                SCOPES);
                        return idToken;

                    } catch (UserRecoverableAuthException userAuthEx) {
                        startActivityForResult(userAuthEx.getIntent(), 228);
                    } catch (IOException ioEx) {
                        Log.e(TAG, "IOException");
                        this.cancel(true);
                        hideLoading();
                        Toast.makeText(getActivity().getApplicationContext(),"IOException",Toast.LENGTH_SHORT).show();
                    } catch (GoogleAuthException fatalAuthEx) {
                        this.cancel(true);
                        hideLoading();
                        Log.e(TAG, "Fatal Authorization Exception" + fatalAuthEx.getLocalizedMessage());
                        Toast.makeText(getActivity().getApplicationContext(),"Fatal Authorization Exception" + fatalAuthEx.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

        Log.e(TAG, "Токен получен");

        accountGoogleRx = ItHappenedApplication.getApi().SignUp(idToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RegistrationResponse>() {
                    @Override
                    public void call(RegistrationResponse registrationResponse) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UserId", registrationResponse.getUserId());
                        editor.putString("Nick", registrationResponse.getUserNickname());
                        editor.putString("Url", registrationResponse.getPicUrl());
                        editor.putLong("NickDate", registrationResponse.getNicknameDateOfChange().getTime());
                        editor.commit();

                        SynchronizationRequest synchronizationRequest = new SynchronizationRequest(registrationResponse.getUserNickname(),
                                new Date(sharedPreferences.getLong("NickDate", 0)),
                                new StaticInMemoryRepository(getActivity().getApplicationContext(), sharedPreferences.getString("UserId", "")).getInstance().GetTrackingCollection());

                        ItHappenedApplication.
                                getApi().SynchronizeData(registrationResponse.getUserId(), synchronizationRequest)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<SynchronizationRequest>() {
                                    @Override
                                    public void call(SynchronizationRequest sync) {
                                        saveDataToDb(sync.getTrackingCollection());

                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putLong("NickDate", sync.getNicknameDateOfChange().getTime());
                                        editor.commit();

                                        Toast.makeText(getActivity().getApplicationContext(), "Синхронизировано", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity().getApplicationContext(), UserActionsActivity.class);
                                        startActivity(intent);
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        hideLoading();
                                        Log.e("RxSync", ""+throwable);
                                        Toast.makeText(getActivity().getApplicationContext(), "Синхронизация не прошла!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideLoading();
                        Log.e("Reg", ""+throwable);
                        Toast.makeText(getActivity().getApplicationContext(), "Разорвано подключение!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void saveDataToDb(List<Tracking> trackings){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        ITrackingRepository trackingRepository = new StaticInMemoryRepository(getActivity().getApplicationContext(), sharedPreferences.getString("UserId", "")).getInstance();
        trackingRepository.SaveTrackingCollection(trackings);
    }

    private void showLoading(){
        layoutFrg.setVisibility(View.INVISIBLE);
        syncPB.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        layoutFrg.setVisibility(View.VISIBLE);
        syncPB.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(accountGoogleRx!=null)
            accountGoogleRx.unsubscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        userMail.setText(sharedPreferences.getString("UserId", ""));
        userNickName.setText(sharedPreferences.getString("Nick", ""));
    }
}
