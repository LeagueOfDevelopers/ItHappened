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
import android.view.ViewTreeObserver;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import ru.lod_misis.ithappened.ConnectionReciver;
import ru.lod_misis.ithappened.ConnectionReciver.ConnectionReciverListener;
import ru.lod_misis.ithappened.Fragments.EventsFragment;
import ru.lod_misis.ithappened.Fragments.ProfileSettingsFragment;
import ru.lod_misis.ithappened.Fragments.StatisticsFragment;
import ru.lod_misis.ithappened.Fragments.TrackingsFragment;
import ru.lod_misis.ithappened.Presenters.UserActionContract;
import ru.lod_misis.ithappened.Presenters.UserActionPresenterImpl;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Retrofit.ItHappenedApplication;
import ru.lod_misis.ithappened.Statistics.FactCalculator;
import rx.Subscription;

public class UserActionsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectionReciverListener, UserActionContract.UserActionView {

    private DrawerLayout mDrawerLayout;

    boolean isTrackingHistory = true;
    boolean isEventsHistory = false;
    boolean isStatistics = false;
    boolean isProfileSettings = false;
    boolean connectionToken = false;

    MenuItem syncItem;

    @Inject
    UserActionPresenterImpl userActionPresenter;

    @BindView(R.id.nav_view)
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

    TextView userNick;
    TrackingsFragment trackFrg;
    FragmentTransaction fTrans;
    FrameLayout layoutFrg;
    @Inject
    FactCalculator factCalculator;
    CircleImageView urlUser;
    ProfileSettingsFragment profileStgsFrg;
    ProgressBar syncPB;
    TextView lable;
    @Inject
    SharedPreferences sharedPreferences;
    Subscription mainSync;
    TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        ItHappenedApplication.getAppComponent().inject(this);
        userActionPresenter.attachView(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.hideOverflowMenu();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        connectionToken = ConnectionReciver.isConnected();

        if (sharedPreferences.getString("UserId", "").isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserId", "Offline");
            editor.putString("Nick", "Offline");
            editor.commit();
        }

        if (!sharedPreferences.getString("UserId", "").equals("Offline") && connectionToken) {
            isTokenFailed = userActionPresenter.updateToken();
        } else {
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.setNavigationItemSelectedListener(this);
        }

        navigationView.setNavigationItemSelectedListener(this);

        trackFrg = new TrackingsFragment();
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.trackingsFrg, trackFrg).addToBackStack(null);
        fTrans.commit();

        syncPB = findViewById(R.id.syncPB);
        layoutFrg = findViewById(R.id.trackingsFrg);

        factCalculator.calculateFacts();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!isTrackingHistory) {
                isTrackingHistory = true;
                isEventsHistory = false;
                isProfileSettings = false;
                isStatistics = false;
                setTitle("Что произошло?");
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.trackingsFrg, trackFrg).addToBackStack(null);
                fTrans.commit();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ViewTreeObserver vto = navigationView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener
                (new ViewTreeObserver.OnGlobalLayoutListener() {@Override public void onGlobalLayout() {
                    sharedPreferences = getApplicationContext().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
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
                                userActionPresenter.getGoogleToken();
                                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        });
                    }

                } });
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.my_events) {
            item.setCheckable(false);
            if (!isTrackingHistory) {

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

            if (!isEventsHistory) {
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

        if (id == R.id.statistics) {
            item.setCheckable(false);
            setTitle("Статистика");
            if (!isStatistics) {
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

        if (id == R.id.synchronisation) {
            syncItem = item;
            item.setCheckable(false);
            if (getApplicationContext().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", "").equals("Offline")) {
                Toast.makeText(getApplicationContext(), "Привяжите аккаунт к GOOGLE для синхронизации", Toast.LENGTH_SHORT).show();
            } else {
                userActionPresenter.syncronization();
            }
        }

        if (id == R.id.proile_settings) {
            item.setCheckable(false);
            String userId = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE).getString("UserId", "");
            if (userId.equals("Offline")) {
                item.setVisible(false);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            } else {

                if (!isProfileSettings) {

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


    @Override
    public void stopMenuAnimation() {
        syncItem.setActionView(null);
    }

    @Override
    public void startMenuAnimation() {
        final Animation animationRotateCenter = AnimationUtils.loadAnimation(
                this, R.anim.rotate);
        syncItem.setActionView(new ProgressBar(this));
        syncItem.getActionView().postDelayed(new Runnable() {

            @Override
            public void run() {
            }
        }, 1000);
    }


    @Override
    public void showLoading() {
        layoutFrg.setVisibility(View.INVISIBLE);
        syncPB.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        layoutFrg.setVisibility(View.VISIBLE);
        syncPB.setVisibility(View.INVISIBLE);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void startActivity() {
        startActivity(getIntent());
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {


        if (requestCode == 228 && resultCode == RESULT_OK) {
            showLoading();
            final String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            AsyncTask<Void, Void, String> getToken = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {

                    String idToken = "";

                    try {
                        idToken = GoogleAuthUtil.getToken(getApplicationContext(), accountName, SCOPES);
                        return idToken;

                    } catch (UserRecoverableAuthException userAuthEx) {
                        startActivityForResult(userAuthEx.getIntent(), 228);
                    } catch (IOException ioEx) {
                        Log.e(TAG, "IOException");
                        this.cancel(true);
                        hideLoading();
                        Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
                    } catch (GoogleAuthException fatalAuthEx) {
                        this.cancel(true);
                        hideLoading();
                        Log.e(TAG, "Fatal Authorization Exception" + fatalAuthEx.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), "Fatal Authorization Exception" + fatalAuthEx.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return idToken;
                }

                @Override
                protected void onPostExecute(String idToken) {
                    userActionPresenter.registrate(idToken);
                }
            };

            getToken.execute(null, null, null);
        }
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
        if (mainSync != null)
            mainSync.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userActionPresenter.dettachView();
    }

    public void cancelLogout() {
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        if (!sharedPreferences.getString("UserId", "").equals("Offline")) {

            navigationView.getMenu().getItem(4).setEnabled(isConnected);
            navigationView.setNavigationItemSelectedListener(this);
        }

    }


    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView imageView;

        public DownLoadImageTask(CircleImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return logo;
        }


        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}
