package com.example.ithappenedandroid.Retrofit;

import com.example.ithappenedandroid.Domain.Tracking;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Пользователь on 19.01.2018.
 */

public interface ItHappenedApi {

    @POST("{idToken}")
    Call<String> UserRegistration(@Path("idToken")String idToken);

    @POST("synchronization/{userId}")
    Call<List<Tracking>> SynchronizeData(@Path("userId")UUID userId, @Body List<Tracking> trackingColletion);

}
