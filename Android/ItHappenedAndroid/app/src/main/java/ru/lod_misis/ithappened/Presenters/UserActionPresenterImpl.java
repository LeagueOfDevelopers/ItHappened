package ru.lod_misis.ithappened.Presenters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.AccountPicker;
import com.yandex.metrica.YandexMetrica;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import ru.lod_misis.ithappened.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Models.RefreshModel;
import ru.lod_misis.ithappened.Models.RegistrationResponse;
import ru.lod_misis.ithappened.Models.SynchronizationRequest;
import ru.lod_misis.ithappened.Retrofit.ItHappenedApplication;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UserActionPresenterImpl implements UserActionContract.UserActionPresenter {

    private static final String TAG = "Registration";
    UserActionContract.UserActionView userActionView;
    Context context;
    SharedPreferences sharedPreferences;
    ITrackingRepository repository;
    boolean isTokenFailed = false;

    public UserActionPresenterImpl(UserActionContract.UserActionView userActionView,
                                   Context context,
                                   SharedPreferences sharedPreferences,
                                   ITrackingRepository repository) {
        this.userActionView = userActionView;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.repository = repository;
    }


    @Override
    public void getGoogleToken() {
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        ((UserActionsActivity) context).startActivityForResult(intent, 228);
    }

    @Override
    public void registrate(String idToken) {


        Log.e(TAG, "Токен получен");

        ItHappenedApplication.getApi().SignUp(idToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RegistrationResponse>() {
                    @Override
                    public void call(RegistrationResponse registrationResponse) {

                        String lastId = sharedPreferences.getString("LastId", "");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.putString("UserId", registrationResponse.getUserId());
                        editor.putString("Nick", registrationResponse.getUserNickname());
                        editor.putString("Url", registrationResponse.getPicUrl());
                        editor.putString("Token", registrationResponse.getToken());
                        editor.putLong("NickDate", registrationResponse.getNicknameDateOfChange().getTime());
                        editor.putString("refreshToken", registrationResponse.getRefreshToken());
                        editor.commit();

                        if (!lastId.equals(sharedPreferences.getString("UserId", ""))) {
                            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
                            repository = StaticInMemoryRepository.getInstance();
                        }

                        SynchronizationRequest synchronizationRequest = new SynchronizationRequest(registrationResponse.getUserNickname(),
                                new Date(sharedPreferences.getLong("NickDate", 0)),
                                repository.GetTrackingCollection());

                        ItHappenedApplication.
                                getApi().SynchronizeData("Bearer " + registrationResponse.getToken(), synchronizationRequest)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<SynchronizationRequest>() {
                                    @Override
                                    public void call(SynchronizationRequest sync) {
                                        List<TrackingV1> trackingV1s = sync.getTrackingV1Collection();
                                        for (TrackingV1 trackingV1 : trackingV1s) {
                                            if (trackingV1.getColor() == null)
                                                trackingV1.setColor("11119017");
                                        }
                                        saveDataToDb(trackingV1s);

                                        SharedPreferences sharedPreferences = context.getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putLong("NickDate", sync.getNicknameDateOfChange().getTime());
                                        editor.commit();
                                        userActionView.showMessage("Синхронизировано");
                                        Intent intent = new Intent(context, UserActionsActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                });

                    }
                }
//                , new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        userActionView.hideLoading();
//                        Log.e("Reg", "" + throwable);
//                        userActionView.showMessage("Разорвано подключение!");
//                    }
//                }
                );

    }

    @Override
    public void onDestroy() {
        userActionView = null;
    }

    @Override
    public void syncronization() {

        userActionView.startMenuAnimation();

        ItHappenedApplication.getApi()
                .Refresh(sharedPreferences.getString("refreshToken", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RefreshModel>() {
                               @Override
                               public void call(RefreshModel model) {
                                   SharedPreferences.Editor editor = context.getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).edit();
                                   editor.putString("Token", model.getAccessToken());
                                   editor.putString("refreshToken", model.getRefreshToken());
                                   editor.commit();

                                   final SynchronizationRequest synchronizationRequest = new SynchronizationRequest(sharedPreferences.getString("Nick", ""),
                                           new java.util.Date(sharedPreferences.getLong("NickDate", 0)),
                                           StaticInMemoryRepository.getInstance().GetTrackingCollection());

                                   ItHappenedApplication.
                                           getApi().
                                           SynchronizeData("Bearer " + sharedPreferences.getString("Token", ""),
                                                   synchronizationRequest)
                                           .subscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread())
                                           .subscribe(new Action1<SynchronizationRequest>() {
                                               @Override
                                               public void call(SynchronizationRequest request) {
                                                   List<TrackingV1> trackingV1s = request.getTrackingV1Collection();
                                                   for (TrackingV1 trackingV1 : trackingV1s) {
                                                       if (trackingV1.getColor() == null)
                                                           trackingV1.setColor("11119017");
                                                   }
                                                   saveDataToDb(trackingV1s);
                                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                                   editor.putString("Nick", synchronizationRequest.getUserNickname());
                                                   editor.putLong("NickDate", synchronizationRequest.getNicknameDateOfChange().getTime());
                                                   userActionView.finishActivity();
                                                   userActionView.stopMenuAnimation();
                                                   userActionView.startActivity();
                                                   YandexMetrica.reportEvent("Пользователь синхронизировался");
                                                   userActionView.showMessage("Синхронизировано");
                                               }
                                           });
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e("Токен упал", throwable + "");
                            }
                        });
    }

    public void testSync(){
        userActionView.startMenuAnimation();
        final SynchronizationRequest synchronizationRequest = new SynchronizationRequest("Кек",
                Calendar.getInstance().getTime(),
                StaticInMemoryRepository.getInstance().GetTrackingCollection());

        ItHappenedApplication.
                getApi().
                TestSync("kennytmb.3run@gmail.com", synchronizationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SynchronizationRequest>() {
                    @Override
                    public void call(SynchronizationRequest request) {
                        List<TrackingV1> trackingV1s = request.getTrackingV1Collection();
                        /*for (TrackingV1 trackingV1 : trackingV1s) {
                            if (trackingV1.getColor() == null)
                                trackingV1.setColor("11119017");
                        }*/
                        saveDataToDb(trackingV1s);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Nick", synchronizationRequest.getUserNickname());
                        editor.putLong("NickDate", synchronizationRequest.getNicknameDateOfChange().getTime());
                        userActionView.finishActivity();
                        userActionView.stopMenuAnimation();
                        userActionView.startActivity();
                        YandexMetrica.reportEvent("Пользователь синхронизировался");
                        userActionView.showMessage("Синхронизировано");
                        userActionView.stopMenuAnimation();
                    }
                });
    }

    @Override
    public boolean updateToken() {

        ItHappenedApplication.getApi().Refresh(sharedPreferences.getString("refreshToken", ""))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RefreshModel>() {
                               @Override
                               public void call(RefreshModel model) {
                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                   editor.putString("Token", model.getAccessToken());
                                   editor.putString("refreshToken", model.getRefreshToken());
                                   editor.commit();
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                isTokenFailed = true;
                            }
                        });

        return isTokenFailed;
    }

    private void saveDataToDb(List<TrackingV1> trackingV1s) {
        repository.SaveTrackingCollection(trackingV1s);
    }


}
