package ru.lod_misis.ithappened.Retrofit;

import retrofit2.http.Header;
import ru.lod_misis.ithappened.Models.RegistrationResponse;
import ru.lod_misis.ithappened.Models.SynchronizationRequest;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface ItHappenedApi {

    @POST("{idToken}")
    Observable<RegistrationResponse> SignUp(@Path("idToken")String idToken);

    @POST("synchronization/synchronize")
    Observable<SynchronizationRequest> SynchronizeData(@Header("Authorization") String token, @Body SynchronizationRequest synchronizationRequest);

    @POST("synchronization/refresh")
    Observable<String> Refresh(@Header("Authorization") String token);

}
