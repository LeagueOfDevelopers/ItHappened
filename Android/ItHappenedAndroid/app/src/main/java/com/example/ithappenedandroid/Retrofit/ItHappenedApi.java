package com.example.ithappenedandroid.Retrofit;

import com.example.ithappenedandroid.Domain.Tracking;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface ItHappenedApi {

    @POST("{idToken}")
    Observable<String> SignUp(@Path("idToken")String idToken);

    @POST("synchronization/{userId}")
    Observable<List<Tracking>> SynchronizeData(@Path("userId")String userId, @Body List<Tracking> trackingColletion);

}
