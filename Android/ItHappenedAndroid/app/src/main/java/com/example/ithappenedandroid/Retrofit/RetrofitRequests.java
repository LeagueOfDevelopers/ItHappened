package com.example.ithappenedandroid.Retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.ithappenedandroid.Activities.SplashScreenActivity;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Пользователь on 19.01.2018.
 */

public class RetrofitRequests {

    ITrackingRepository trackingRepository;
    Context context;
    UUID userId;

    public RetrofitRequests(ITrackingRepository trackingRepository, Context context, UUID userId) {
        this.trackingRepository = trackingRepository;
        this.context = context;
        this.userId = userId;
    }

    public void syncData() {

        List<Tracking> trackingCollection = trackingRepository.GetTrackingCollection();

        ItHappenedApplication.getApi().SynchronizeData(userId, trackingCollection).enqueue(new Callback<List<Tracking>>() {
            @Override
            public void onResponse(Call<List<Tracking>> call, Response<List<Tracking>> response) {
                if (response.body() != null) {
                    Toast.makeText(context, "Синхронизация прошла успешно", Toast.LENGTH_SHORT).show();
                }else{

                }
            }

            @Override
            public void onFailure(Call<List<Tracking>> call, Throwable throwable) {
                Toast.makeText(context, "Проверьте подключение к интернету!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String userRegistration(String idToken) {

        final String userId = null;

        ItHappenedApplication.getApi().SignUp(idToken).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    final String id = response.body();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserId", id);
                    editor.commit();
                    Intent intent = new Intent(context, SplashScreenActivity.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
            }
        });

        return userId;

    }

    private void setUserAndReturn(String returned,String id){
        returned = id;
    }
}
