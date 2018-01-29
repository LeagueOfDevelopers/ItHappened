package com.example.ithappenedandroid.Retrofit;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://85.143.104.47:1080/")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        itHappenedApi = retrofit.create(ItHappenedApi.class);
    }

    public static ItHappenedApi getApi(){
        return itHappenedApi;
    }
}
