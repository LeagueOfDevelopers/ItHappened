package ru.lod_misis.ithappened.UI.Presenters;

/**
 * Created by Пользователь on 23.05.2018.
 */

public interface ProfileSettingsFragmentContract {

    interface ProfileSettingsFragmentView{
        void showLoading();
        void hideLoading();
        void showMessage(String message);
    }

    interface ProfileSettingsFragmentPresenter{
        void onViewAttach(ProfileSettingsFragmentView view);
        void logout();
        void cancelLogout();
        void onViewDettach();
    }

}
