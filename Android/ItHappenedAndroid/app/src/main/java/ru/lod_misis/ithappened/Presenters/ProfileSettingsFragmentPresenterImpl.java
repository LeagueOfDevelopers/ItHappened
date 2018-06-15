package ru.lod_misis.ithappened.Presenters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.yandex.metrica.YandexMetrica;

import java.util.List;

import ru.lod_misis.ithappened.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Models.RefreshModel;
import ru.lod_misis.ithappened.Models.SynchronizationRequest;
import ru.lod_misis.ithappened.Retrofit.ItHappenedApplication;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Пользователь on 23.05.2018.
 */

public class ProfileSettingsFragmentPresenterImpl implements ProfileSettingsFragmentContract.ProfileSettingsFragmentPresenter {

    SharedPreferences sharedPreferences;
    Context context;
    ProfileSettingsFragmentContract.ProfileSettingsFragmentView view;

    public ProfileSettingsFragmentPresenterImpl(ProfileSettingsFragmentContract.ProfileSettingsFragmentView view,
                                                SharedPreferences sharedPreferences,
                                                Context context) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
        this.view = view;
    }

    @Override
    public void logout() {

        view.showLoading();

        ItHappenedApplication.getApi().Refresh(sharedPreferences.getString("refreshToken",""))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RefreshModel>() {
                               @Override
                               public void call(RefreshModel model) {
                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                   editor.putString("Token", model.getAccessToken());
                                   editor.putString("refreshToken", model.getRefreshToken());
                                   editor.commit();

                                   final SynchronizationRequest synchronizationRequest = new SynchronizationRequest(sharedPreferences.getString("Nick", ""),
                                           new java.util.Date(sharedPreferences.getLong("NickDate", 0)), StaticInMemoryRepository.getInstance().GetTrackingCollection());

                                   ItHappenedApplication.getApi().SynchronizeData("Bearer "+sharedPreferences.getString("Token", ""), synchronizationRequest).subscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread())
                                           .subscribe(new Action1<SynchronizationRequest>() {
                                               @Override
                                               public void call(SynchronizationRequest request) {
                                                   saveDataToDb(request.getNewTrackingCollection());
                                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                                   String lastId = sharedPreferences.getString("UserId", "");
                                                   editor.clear();
                                                   editor.putString("LastId", lastId);
                                                   editor.putBoolean("LOGOUT", true);
                                                   editor.commit();
                                                   Intent intent = new Intent(context, UserActionsActivity.class);
                                                   context.startActivity(intent);
                                                   YandexMetrica.reportEvent("Пользователь вышел из профиля");
                                                   view.hideLoading();
                                                   view.showMessage("До скорой встречи!");
                                               }
                                           }, new Action1<Throwable>() {
                                               @Override
                                               public void call(Throwable throwable) {
                                                   Log.e("RxSync", ""+throwable);
                                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                                   String lastId = sharedPreferences.getString("UserId", "");
                                                   editor.clear();
                                                   editor.putString("LastId", lastId);
                                                   editor.putBoolean("LOGOUT", true);
                                                   editor.commit();
                                                   Intent intent = new Intent(context, UserActionsActivity.class);
                                                   context.startActivity(intent);
                                                   view.showMessage("До скорой встречи!");
                                                   view.hideLoading();
                                                   YandexMetrica.reportEvent("Пользователь вышел из профиля");
                                               }
                                           });
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e("RxSync", ""+throwable);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String lastId = sharedPreferences.getString("UserId", "");
                                editor.clear();
                                editor.putString("LastId", lastId);
                                editor.putBoolean("LOGOUT", true);
                                editor.commit();
                                Intent intent = new Intent(context, UserActionsActivity.class);
                                context.startActivity(intent);
                                view.showMessage("До скорой встречи!");

                                YandexMetrica.reportEvent("Пользователь вышел из профиля");
                                Log.e("Токен упал", throwable+"");
                            }
                        });

    }

        private void saveDataToDb(List<NewTracking> newTrackings){
            ITrackingRepository trackingRepository = StaticInMemoryRepository.getInstance();
            trackingRepository.SaveTrackingCollection(newTrackings);
        }

    @Override
    public void cancelLogout() {

    }
}
