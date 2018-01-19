package com.example.ithappenedandroid.Retrofit;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Пользователь on 19.01.2018.
 */

public class ItHappenedApplication extends Application {

    private static ItHappenedApi itHappenedApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient.Builder client = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client.addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://base_url")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itHappenedApi = retrofit.create(ItHappenedApi.class);
    }

    public static ItHappenedApi getApi(){
        return itHappenedApi;
    }
}
