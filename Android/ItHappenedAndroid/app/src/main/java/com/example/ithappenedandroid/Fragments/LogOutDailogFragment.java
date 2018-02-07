package com.example.ithappenedandroid.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.ithappenedandroid.Activities.UserActionsActivity;

/**
 * Created by Пользователь on 07.02.2018.
 */

public class LogOutDailogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Вы действительно хотите выйти?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((UserActionsActivity) getActivity()).logout();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((UserActionsActivity) getActivity()).cancelLogout();
                    }
                });

        return builder.create();

    }
    }
