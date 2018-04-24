package ru.lod_misis.ithappened.Retrofit;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

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

/**
 * Created by Пользователь on 19.01.2018.
 */

public class ItHappenedApplication extends Application {

    private static ItHappenedApi itHappenedApi;
    private Retrofit retrofit;
    private final String API_KEY = "18db6cc1-8c43-408e-8298-a8f3b04bb595";
    boolean isFirts = false;

    public String getAPI_KEY() {
        return API_KEY;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        YandexMetricaConfig.Builder metrikaBuilder = YandexMetricaConfig.newConfigBuilder(API_KEY);

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        String lastId = sharedPreferences.getString("lastId", "");

        if(lastId.isEmpty()){
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

        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        itHappenedApi = retrofit.create(ItHappenedApi.class);
    }

    public static ItHappenedApi getApi(){
        return itHappenedApi;
    }
}
