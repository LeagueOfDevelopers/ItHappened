package ru.lod_misis.ithappened.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import ru.lod_misis.ithappened.Activities.UserActionsActivity;

import java.util.UUID;

/**
 * Created by Пользователь on 30.01.2018.
 */

@SuppressLint("ValidFragment")
public class DeleteTrackingFragment extends DialogFragment {

    UUID trackingId;

    @SuppressLint("ValidFragment")
    public DeleteTrackingFragment(UUID trackingId){
        this.trackingId = trackingId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы больше не сможете восстановить это отслеживание!")
                .setTitle("Вы действительно хотите удалить это отслеживание?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((UserActionsActivity) getActivity()).okClicked(trackingId);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((UserActionsActivity) getActivity()).cancelClicked();
                    }
                });

        return builder.create();

    }

}
