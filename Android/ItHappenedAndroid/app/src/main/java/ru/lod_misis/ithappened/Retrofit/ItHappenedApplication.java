package ru.lod_misis.ithappened.Retrofit;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.lod_misis.ithappened.BuildConfig;
import ru.lod_misis.ithappened.ConnectionReciver;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.TrackingRepository;
import javax.inject.Inject;
import ru.lod_misis.ithappened.MyGeopositionService;
import ru.lod_misis.ithappened.di.components.DaggerMainComponent;
import ru.lod_misis.ithappened.di.components.MainComponent;
import ru.lod_misis.ithappened.di.modules.MainModule;

/**
 * Created by Пользователь on 19.01.2018.
 */

public class ItHappenedApplication extends Application {

    private static MainComponent appComponent;
    private static ItHappenedApi itHappenedApi;
    private static ItHappenedApplication mInstance;
    private final String API_KEY = "18db6cc1-8c43-408e-8298-a8f3b04bb595";
    boolean isFirts = false;
    private Retrofit retrofit;

    public static synchronized ItHappenedApplication getInstance() {
        return mInstance;
    }
    @Inject
    ITrackingRepository repository;
    public String getAPI_KEY() {
        return API_KEY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            stopService(new Intent(this,MyGeopositionService.class));
        }else{
            stopService(new Intent(this, MyGeopositionService.class));
            startService(new Intent(this, MyGeopositionService.class));
        }
        appComponent = DaggerMainComponent.builder().mainModule(new MainModule(this)).build();
        appComponent.inject(this);
        repository.configureRealm();

        YandexMetricaConfig.Builder metrikaBuilder = YandexMetricaConfig.newConfigBuilder(API_KEY);

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        String lastId = sharedPreferences.getString("lastId", "");

        if (lastId.isEmpty()) {
            isFirts = true;
        }

        if (!isFirts) {
            metrikaBuilder.handleFirstActivationAsUpdate(true);
        }
        YandexMetricaConfig extendedConfig = metrikaBuilder.build();

        YandexMetrica.activate(getApplicationContext(), extendedConfig);

        YandexMetrica.enableActivityAutoTracking(this);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client.addInterceptor(loggingInterceptor);

        Gson gson = new GsonBuilder().setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        itHappenedApi = retrofit.create(ItHappenedApi.class);

    }

    public void setConnectionListener(ConnectionReciver.ConnectionReciverListener listener) {
        ConnectionReciver.connectionReciverListener = listener;
    }

    public static MainComponent getAppComponent(){
        return appComponent;
    }

    public static ItHappenedApi getApi(){
        return itHappenedApi;
    }
}
