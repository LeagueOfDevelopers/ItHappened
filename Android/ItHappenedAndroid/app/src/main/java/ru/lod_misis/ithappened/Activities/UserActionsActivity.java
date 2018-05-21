package ru.lod_misis.ithappened.Activities;

import android.accounts.AccountManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.yandex.metrica.YandexMetrica;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.ConnectionReciver;
import ru.lod_misis.ithappened.ConnectionReciver.ConnectionReciverListener;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Fragments.EventsFragment;
import ru.lod_misis.ithappened.Fragments.ProfileSettingsFragment;
import ru.lod_misis.ithappened.Fragments.StatisticsFragment;
import ru.lod_misis.ithappened.Fragments.TrackingsFragment;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.Models.RefreshModel;
import ru.lod_misis.ithappened.Models.RegistrationResponse;
import ru.lod_misis.ithappened.Models.SynchronizationRequest;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Retrofit.ItHappenedApplication;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UserActionsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectionReciverListener {

    private DrawerLayout mDrawerLayout;

    boolean isTrackingHistory = true;
    boolean isEventsHistory = false;
    boolean isStatistics = false;
    boolean isProfileSettings = false;

    NavigationView navigationView;

    private final static String G_PLUS_SCOPE =
            "oauth2:https://www.googleapis.com/auth/plus.me";
    private final static String USERINFO_SCOPE =
            "https://www.googleapis.com/auth/userinfo.profile";
    private final static String EMAIL_SCOPE =
            "https://www.googleapis.com/auth/userinfo.email";
    private final static String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE + " " + EMAIL_SCOPE;
    private static final String TAG = "SignIn";

    private boolean isTokenFailed = false;

    InMemoryFactRepository factRepository;
    ITrackingRepository trackingRepository;

    Subscription accountGoogleRx;

    TextView userNick;
    TrackingsFragment trackFrg;
    FragmentTransaction fTrans;
    FrameLayout layoutFrg;

    CircleImageView urlUser;

    ProfileSettingsFragment profileStgsFrg;

    ProgressBar syncPB;
    TextView lable;

    Subscription mainSync;
    TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Fabric.with(this, new Crashlytics());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.hideOverflowMenu();
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);


        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("UserId", "").isEmpty()){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserId", "Offline");
            editor.putString("Nick", "Offline");
            editor.commit();
        }

        if(!sharedPreferences.getString("UserId", "").equals("Offline")){
            ItHappenedApplication.getApi().Refresh(sharedPreferences.getString("refreshToken",""))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<RefreshModel>() {
                                   @Override
                                   public void call(RefreshModel model) {
                                       SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).edit();
                                       editor.putString("Token", model.getAccessToken());
                                       editor.putString("refreshToken", model.getRefreshToken());
                                       editor.commit();
                                   }
                               },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    isTokenFailed = true;
                                    logout();
                                }
                            });
        }else{
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.setNavigationItemSelectedListener(this);
        }

        navigationView.setNavigationItemSelectedListener(this);

        trackFrg = new TrackingsFragment();
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.trackingsFrg, trackFrg).addToBackStack(null);
        fTrans.commit();

        syncPB = (ProgressBar) findViewById(R.id.syncPB);
        layoutFrg = (FrameLayout) findViewById(R.id.trackingsFrg);

        factRepository = StaticFactRepository.getInstance();
        if(sharedPreferences.getString("LastId","").isEmpty()) {
            StaticInMemoryRepository.setInstance(getApplicationContext(),
                    sharedPreferences.getString("UserId", ""));
            trackingRepository = StaticInMemoryRepository.getInstance();
        }else{
            StaticInMemoryRepository.setInstance(getApplicationContext(), sharedPreferences.getString("LastId", ""));
            trackingRepository = StaticInMemoryRepository.getInstance();
        }

        factRepository.calculateAllTrackingsFacts(trackingRepository.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculate");
                    }
                });

        factRepository.calculateOneTrackingFacts(trackingRepository.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculateOneTrackingFact");
                    }
                });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            isTrackingHistory = true;
            isEventsHistory = false;
            isProfileSettings = false;
            isStatistics = false;

                setTitle("Что произошло?");
                trackFrg = new TrackingsFragment();



                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.trackingsFrg, trackFrg).addToBackStack(null);
                fTrans.commit();

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        userNick = (TextView) findViewById(R.id.userNickname);
        userNick.setText(sharedPreferences.getString("Nick",""));
        loginButton = (TextView) findViewById(R.id.loginButton);
        urlUser = (CircleImageView) findViewById(R.id.imageView);
        lable = (TextView) findViewById(R.id.menuTitle);
        if(!sharedPreferences.getString("UserId", "").equals("Offline")) {
            loginButton.setVisibility(View.GONE);
            new DownLoadImageTask(urlUser).execute(sharedPreferences.getString("Url", ""));
        }else{

            loginButton.setVisibility(View.VISIBLE);
            lable.setVisibility(View.GONE);
            userNick.setVisibility(View.GONE);
            urlUser.setVisibility(View.GONE);


            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                            false, null, null, null, null);
                    startActivityForResult(intent, 228);
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            });

        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.my_events){
            item.setCheckable(false);
            if(!isTrackingHistory) {

                isTrackingHistory = true;
                isEventsHistory = false;
                isProfileSettings = false;
                isStatistics = false;

                TrackingsFragment newTrackFrg = new TrackingsFragment();


                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.trackingsFrg, newTrackFrg).addToBackStack(null);
                fTrans.commit();

                setTitle("Что произошло?");
            }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

        }

        if (id == R.id.events_history) {
            item.setCheckable(false);

            if(!isEventsHistory) {
                setTitle("История событий");
                isTrackingHistory = false;
                isEventsHistory = true;
                isProfileSettings = false;
                isStatistics = false;

                EventsFragment eventsFrg = new EventsFragment();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.trackingsFrg, eventsFrg, "EVENTS_HISTORY").addToBackStack(null);
                fTrans.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        if(id == R.id.statistics){
            item.setCheckable(false);
            setTitle("Статистика");
            if(!isStatistics) {
                StatisticsFragment statFrg = new StatisticsFragment();

                isTrackingHistory = false;
                isEventsHistory = false;
                isProfileSettings = false;
                isStatistics = true;

                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.trackingsFrg, statFrg).addToBackStack(null);
                fTrans.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        if(id == R.id.synchronisation){
            item.setCheckable(false);
            if(getApplicationContext().getSharedPreferences("MAIN_KEYS",Context.MODE_PRIVATE).getString("UserId", "").equals("Offline")){
                Toast.makeText(getApplicationContext(),"Привяжите аккаунт к GOOGLE для синхронизации", Toast.LENGTH_SHORT).show();
            }else {
                final Animation animationRotateCenter = AnimationUtils.loadAnimation(
                        this, R.anim.rotate);
                item.setActionView(new ProgressBar(this));
                item.getActionView().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                    }
                }, 1000);

                final SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

                ItHappenedApplication.getApi()
                        .Refresh(sharedPreferences.getString("refreshToken",""))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<RefreshModel>() {
                                       @Override
                                       public void call(RefreshModel model) {
                                           SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).edit();
                                           editor.putString("Token", model.getAccessToken());
                                           editor.putString("refreshToken", model.getRefreshToken());
                                           editor.commit();


                                           final SynchronizationRequest synchronizationRequest = new SynchronizationRequest(sharedPreferences.getString("Nick", ""),
                                                   new java.util.Date(sharedPreferences.getLong("NickDate", 0)),
                                                   StaticInMemoryRepository.getInstance().GetTrackingCollection());

                                           mainSync = ItHappenedApplication.
                                                   getApi().
                                                   SynchronizeData("Bearer " + sharedPreferences.getString("Token", ""),
                                                           synchronizationRequest)
                                                   .subscribeOn(Schedulers.io())
                                                   .observeOn(AndroidSchedulers.mainThread())
                                                   .subscribe(new Action1<SynchronizationRequest>() {
                                                       @Override
                                                       public void call(SynchronizationRequest request) {
                                                           saveDataToDb(request.getTrackingCollection());
                                                           SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                                                           SharedPreferences.Editor editor = sharedPreferences.edit();
                                                           editor.putString("Nick", synchronizationRequest.getUserNickname());
                                                           editor.putLong("NickDate", synchronizationRequest.getNicknameDateOfChange().getTime());
                                                           finish();
                                                           item.setActionView(null);
                                                           startActivity(getIntent());
                                                           YandexMetrica.reportEvent("Пользователь синхронизировался");
                                                           Toast.makeText(getApplicationContext(), "Синхронизировано", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }, new Action1<Throwable>() {
                                                       @Override
                                                       public void call(Throwable throwable) {
                                                           Log.e("RxSync", "" + throwable);
                                                           DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                                           drawer.closeDrawer(GravityCompat.START);
                                                           item.setActionView(null);
                                                           Toast.makeText(getApplicationContext(), "Подключение разорвано!", Toast.LENGTH_SHORT).show();
                                                       }
                                                   });
                                      }
                                   },
                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        //Toast.makeText(getApplicationContext(), "Токен упал(", Toast.LENGTH_SHORT).show();
                                        Log.e("Токен упал", throwable+"");
                                    }
                                });


                item.getActionView().clearAnimation();
            }
        }

           if(id == R.id.proile_settings){
               item.setCheckable(false);
               String userId = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE).getString("UserId", "");
               if(userId.equals("Offline")){
                   item.setVisible(false);
                   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                   drawer.closeDrawer(GravityCompat.START);
               }else {

                   if(!isProfileSettings) {

                       isTrackingHistory = false;
                       isEventsHistory = false;
                       isProfileSettings = true;
                       isStatistics = false;

                       profileStgsFrg = new ProfileSettingsFragment();
                       fTrans = getFragmentManager().beginTransaction();
                       fTrans.replace(R.id.trackingsFrg, profileStgsFrg).addToBackStack(null);
                       fTrans.commit();
                   }
                   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                   drawer.closeDrawer(GravityCompat.START);
               }

           }
        return true;
    }


    public void okClicked(UUID trackingId) {

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        TrackingService trackingService = new TrackingService("", StaticInMemoryRepository.getInstance());
        trackingService.RemoveTracking(trackingId);
        factRepository.onChangeCalculateOneTrackingFacts(trackingService.GetTrackingCollection(), trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculateOneTrackingFact");
                    }
                });
        factRepository.calculateAllTrackingsFacts(trackingService.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculateOneTrackingFact");
                    }
                });
        YandexMetrica.reportEvent("Пользователь удалил отслеживание");
        Toast.makeText(this, "Отслеживание удалено", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, UserActionsActivity.class);
        startActivity(intent);

    }

    public void cancelClicked() {
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /*private void saveDataToDb(List<Tracking> trackings){
        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        ITrackingRepository trackingRepository = new StaticInMemoryRepository(getApplicationContext(), sharedPreferences.getString("UserId", "")).getInstance();
        trackingRepository.SaveTrackingCollection(trackings);
    }*/

    private void showLoading(){
        layoutFrg.setVisibility(View.INVISIBLE);
        syncPB.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        layoutFrg.setVisibility(View.VISIBLE);
        syncPB.setVisibility(View.INVISIBLE);
    }

    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data){


        if (requestCode == 228 && resultCode == RESULT_OK) {
            showLoading();
            final String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            AsyncTask<Void, Void, String> getToken = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {

                    String idToken = "";

                    try {
                        idToken = GoogleAuthUtil.getToken(getApplicationContext(), accountName,
                                SCOPES);
                        return idToken;

                    } catch (UserRecoverableAuthException userAuthEx) {
                        startActivityForResult(userAuthEx.getIntent(), 228);
                    } catch (IOException ioEx) {
                        Log.e(TAG, "IOException");
                        this.cancel(true);
                        hideLoading();
                        Toast.makeText(getApplicationContext(),"IOException",Toast.LENGTH_SHORT).show();
                    } catch (GoogleAuthException fatalAuthEx) {
                        this.cancel(true);
                        hideLoading();
                        Log.e(TAG, "Fatal Authorization Exception" + fatalAuthEx.getLocalizedMessage());
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

        Log.e(TAG, "Токен получен");

        accountGoogleRx = ItHappenedApplication.getApi().SignUp(idToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RegistrationResponse>() {
                    @Override
                    public void call(RegistrationResponse registrationResponse) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

                        ITrackingRepository collection;

                        if(sharedPreferences.getString("LastId","").isEmpty()) {
                            StaticInMemoryRepository.setUserId(sharedPreferences.getString("Offline", ""));
                            collection = StaticInMemoryRepository.getInstance();
                        }else{
                            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
                            collection = StaticInMemoryRepository.getInstance();
                        }

                        String lastId = sharedPreferences.getString("LastId", "");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.putString("UserId", registrationResponse.getUserId());
                        editor.putString("Nick", registrationResponse.getUserNickname());
                        editor.putString("Url", registrationResponse.getPicUrl());
                        editor.putString("Token", registrationResponse.getToken());
                        editor.putLong("NickDate", registrationResponse.getNicknameDateOfChange().getTime());
                        editor.putString("refreshToken", registrationResponse.getRefreshToken());
                        editor.commit();

                        if(!lastId.equals(sharedPreferences.getString("UserId", ""))){
                            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
                            collection = StaticInMemoryRepository.getInstance();
                        }

                        SynchronizationRequest synchronizationRequest = new SynchronizationRequest(registrationResponse.getUserNickname(),
                                new Date(sharedPreferences.getLong("NickDate", 0)),
                                collection.GetTrackingCollection());

                        ItHappenedApplication.
                                getApi().SynchronizeData("Bearer "+registrationResponse.getToken(), synchronizationRequest)
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
                                        YandexMetrica.reportEvent("Пользователь привязал аккаунт к google");
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

    private void saveDataToDb(List<Tracking> trackings){
        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        ITrackingRepository trackingRepository = StaticInMemoryRepository.getInstance();
        trackingRepository.SaveTrackingCollection(trackings);
    }

    public void logout(){
        if(!isTokenFailed)
        ProfileSettingsFragment.showProgressBar();
        final SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        ItHappenedApplication.getApi().Refresh(sharedPreferences.getString("refreshToken",""))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RefreshModel>() {
                    @Override
                    public void call(RefreshModel model) {
                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).edit();
                        editor.putString("Token", model.getAccessToken());
                        editor.putString("refreshToken", model.getRefreshToken());
                        editor.commit();

                        final SynchronizationRequest synchronizationRequest = new SynchronizationRequest(sharedPreferences.getString("Nick", ""),
                                new java.util.Date(sharedPreferences.getLong("NickDate", 0)), StaticInMemoryRepository.getInstance().GetTrackingCollection());

                        ItHappenedApplication.getApi().SynchronizeData("Bearer "+sharedPreferences.getString("Token", ""), synchronizationRequest).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<SynchronizationRequest>() {
                                    @Override
                                    public void call(SynchronizationRequest request) {
                                        saveDataToDb(request.getTrackingCollection());
                                        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        String lastId = sharedPreferences.getString("UserId", "");
                                        editor.clear();
                                        editor.putString("LastId", lastId);
                                        editor.putBoolean("LOGOUT", true);
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(), UserActionsActivity.class);
                                        startActivity(intent);
                                        YandexMetrica.reportEvent("Пользователь вышел из профиля");
                                        ProfileSettingsFragment.hideProgressBar();
                                        Toast.makeText(getApplicationContext(), "До скорой встречи!", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Log.e("RxSync", ""+throwable);
                                        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        String lastId = sharedPreferences.getString("UserId", "");
                                        editor.clear();
                                        editor.putString("LastId", lastId);
                                        editor.putBoolean("LOGOUT", true);
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(), UserActionsActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "До скорой встречи!", Toast.LENGTH_SHORT).show();
                                        ProfileSettingsFragment.hideProgressBar();
                                        YandexMetrica.reportEvent("Пользователь вышел из профиля");
                                    }
                                });
                    }
                },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e("RxSync", ""+throwable);
                                SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String lastId = sharedPreferences.getString("UserId", "");
                                editor.clear();
                                editor.putString("LastId", lastId);
                                editor.putBoolean("LOGOUT", true);
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), UserActionsActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "До скорой встречи!", Toast.LENGTH_SHORT).show();

                                YandexMetrica.reportEvent("Пользователь вышел из профиля");
                                Log.e("Токен упал", throwable+"");
                                if(!isTokenFailed)
                                ProfileSettingsFragment.hideProgressBar();
                            }
                        });






    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectionReciver connectivityReceiver = new ConnectionReciver();
        registerReceiver(connectivityReceiver, intentFilter);

        ItHappenedApplication.getInstance().setConnectionListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mainSync!=null)
            mainSync.unsubscribe();
    }

    public void cancelLogout(){}

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected) {

            Toast.makeText(getApplicationContext(), "Подключено", Toast.LENGTH_SHORT).show();
            Log.e("Connect", "Vkl");

        }else{

            Toast.makeText(getApplicationContext(), "Отключено", Toast.LENGTH_SHORT).show();
            Log.e("Connect", "Vikl");
        }
    }


    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        CircleImageView imageView;

        public DownLoadImageTask(CircleImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }



        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

}
