package ru.lod_misis.ithappened.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import ru.lod_misis.ithappened.Presenters.ProfileSettingsFragmentPresenterImpl;

public class LogOutDailogFragment extends DialogFragment {

    ProfileSettingsFragmentPresenterImpl logoutPresenter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Вы действительно хотите выйти?")
                .setMessage("Если вы не подключены к интернету, то можете потерять часть данных!")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logoutPresenter.logout();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logoutPresenter.cancelLogout();
                    }
                });

        return builder.create();

    }

    public void setLogoutPresenter(ProfileSettingsFragmentPresenterImpl logoutPresenter) {
        this.logoutPresenter = logoutPresenter;
    }
}
