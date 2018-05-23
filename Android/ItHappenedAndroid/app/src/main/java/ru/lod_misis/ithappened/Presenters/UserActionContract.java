package ru.lod_misis.ithappened.Presenters;

import rx.Subscription;

/**
 * Created by Пользователь on 23.05.2018.
 */

public interface UserActionContract {

    interface UserActionView{
        void showLoading();
        void hideLoading();
        void stopMenuAnimation();
        void startMenuAnimation();
        void finishActivity();
        void startActivity();
        void showMessage(String message);
    }

    interface UserActionPresenter{
        void getGoogleToken();
        Subscription registrate(String idToken);
        void syncronization();
    }

}
