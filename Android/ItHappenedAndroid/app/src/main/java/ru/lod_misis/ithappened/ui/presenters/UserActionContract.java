package ru.lod_misis.ithappened.ui.presenters;

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
        void attachView(UserActionView view);
        void getGoogleToken();
        void registrate(String idToken);
        void syncronization();
        boolean updateToken();
        void dettachView();
    }

}
