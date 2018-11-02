package ru.lod_misis.ithappened.UI.Presenters;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by Пользователь on 23.05.2018.
 */

public class ProfileSettingsFragmentPresenterImpl implements ProfileSettingsFragmentContract.ProfileSettingsFragmentPresenter {

    SharedPreferences sharedPreferences;
    Context context;
    ProfileSettingsFragmentContract.ProfileSettingsFragmentView view;

    @Inject
    public ProfileSettingsFragmentPresenterImpl(SharedPreferences sharedPreferences,
                                                Context context) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }

    @Override
    public void logout() {

        view.showLoading();

        /*ItHappenedApplication.getApi().Refresh(sharedPreferences.getString("refreshToken", ""))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RefreshModel>() {
                               @Override
                               public void call(RefreshModel model) {
                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                   editor.putString("Token", model.getAccessToken());
                                   editor.putString("refreshToken", model.getRefreshToken());
                                   editor.commit();
                                   //TODO выпилить

                                   /*final SynchronizationRequest synchronizationRequest = new SynchronizationRequest(sharedPreferences.getString("Nick", ""),
                                           new java.util.Date(sharedPreferences.getLong("NickDate", 0)), StaticInMemoryRepository.getInstance().GetTrackingCollection());

                                   ItHappenedApplication.getApi().SynchronizeData("Bearer " + sharedPreferences.getString("Token", ""), synchronizationRequest).subscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread())
                                           .subscribe(new Action1<SynchronizationRequest>() {
                                               @Override
                                               public void call(SynchronizationRequest request) {
                                                   saveDataToDb(request.getTrackingV1Collection());
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
                                                   Log.e("RxSync", "" + throwable);
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
                                                   YandexMetrica.reportEvent(context.getString(R.string.metrica_user_logout));
                                               }
                                           });
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e("RxSync", "" + throwable);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String lastId = sharedPreferences.getString("UserId", "");
                                editor.clear();
                                editor.putString("LastId", lastId);
                                editor.putBoolean("LOGOUT", true);
                                editor.commit();
                                Intent intent = new Intent(context, UserActionsActivity.class);
                                context.startActivity(intent);
                                view.showMessage("До скорой встречи!");

                                YandexMetrica.reportEvent(context.getString(R.string.metrica_user_logout));
                                Log.e("Токен упал", throwable + "");
                            }
                        });*/

    }

//    private void saveDataToDb(List<TrackingV1> trackingV1s) {
//        ITrackingRepository trackingRepository = StaticInMemoryRepository.getInstance();
//        trackingRepository.SaveTrackingCollection(trackingV1s);
//    }

    @Override
    public void onViewAttach(ProfileSettingsFragmentContract.ProfileSettingsFragmentView view) {
        this.view = view;
    }

    @Override
    public void onViewDettach() {
        view = null;
    }

    @Override
    public void cancelLogout() {

    }
}
