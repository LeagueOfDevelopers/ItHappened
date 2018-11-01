package ru.lod_misis.ithappened.Data.Retrofit;

import retrofit2.http.GET;
import retrofit2.http.Header;
import ru.lod_misis.ithappened.Data.DTOModels.RefreshModel;
import ru.lod_misis.ithappened.Data.DTOModels.RegistrationResponse;
import ru.lod_misis.ithappened.Data.DTOModels.SynchronizationRequest;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface ItHappenedApi {

    @POST("registration")
    Observable<RegistrationResponse> SignUp(@Header("GoogleToken") String idToken);

    @POST("synchronization/synchronize")
    Observable<SynchronizationRequest> SynchronizeData(@Header("Authorization") String token, @Body SynchronizationRequest synchronizationRequest);

    @GET("synchronization/refresh/{refreshToken}")
    Observable<RefreshModel> Refresh(@Path("refreshToken") String refreshToken);

    @POST("synchronization/{UserId}")
    Observable<SynchronizationRequest> TestSync(@Path("UserId") String userId, @Body SynchronizationRequest synchronizationRequest);

}
