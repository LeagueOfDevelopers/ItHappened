package ru.lod_misis.ithappened.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.Presenters.TrackingsContract;
import ru.lod_misis.ithappened.Retrofit.ItHappenedApplication;

/**
 * Created by Пользователь on 30.01.2018.
 */

@SuppressLint("ValidFragment")
public class DeleteTrackingFragment extends DialogFragment {

    UUID trackingId;
    TrackingsContract.TrackingsPresenter trackingsPresenter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы больше не сможете восстановить это отслеживание!")
                .setTitle("Вы действительно хотите удалить это отслеживание?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        trackingsPresenter.deleteTracking(trackingId);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        trackingsPresenter.cancelDeleting();
                    }
                });

        return builder.create();

    }

    public void setTrackingsPresenter(TrackingsContract.TrackingsPresenter trackingsPresenter){
        this.trackingsPresenter = trackingsPresenter;
    }

    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }
}