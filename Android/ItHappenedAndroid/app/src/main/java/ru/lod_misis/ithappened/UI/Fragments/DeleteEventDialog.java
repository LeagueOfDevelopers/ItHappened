package ru.lod_misis.ithappened.UI.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import ru.lod_misis.ithappened.UI.Activities.EventDetailsActivity;

public class DeleteEventDialog extends DialogFragment {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы болше не сможете восстановить это событие!")
                .setTitle("Вы действительно хотите удалить это событие?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((EventDetailsActivity) getActivity()).okClicked();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((EventDetailsActivity) getActivity()).cancelClicked();
                    }
                });

        return builder.create();

    }


}
