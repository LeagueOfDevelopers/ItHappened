package com.lod.ithappened.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.lod.ithappened.Activities.UserActionsActivity;

/**
 * Created by Пользователь on 07.02.2018.
 */

public class LogOutDailogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Вы действительно хотите выйти?")
                .setMessage("Если вы не подключены к интернету, то можете потерять часть данных!")
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
