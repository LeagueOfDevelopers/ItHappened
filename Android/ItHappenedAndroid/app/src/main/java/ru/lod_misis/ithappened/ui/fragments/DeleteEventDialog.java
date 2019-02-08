package ru.lod_misis.ithappened.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.yandex.metrica.YandexMetrica;

import ru.lod_misis.ithappened.ui.activities.EventDetailsActivity;
import ru.lod_misis.ithappened.ui.presenters.EventDetailsContract;

public class DeleteEventDialog extends DialogFragment {

    private EventDetailsContract.EventDetailsPresenter eventDetailsPresenter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы болше не сможете восстановить это событие!")
                .setTitle("Вы действительно хотите удалить это событие?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        okClicked();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelClicked();
                    }
                });

        return builder.create();

    }

    public void setEventDetailsPresenter(EventDetailsContract.EventDetailsPresenter eventDetailsPresenter) {
        this.eventDetailsPresenter = eventDetailsPresenter;
    }

    public void okClicked() {
        eventDetailsPresenter.okClicked();
        YandexMetrica.reportEvent("Пользователь удалил событие");
    }

    public void cancelClicked() {
        eventDetailsPresenter.canselClicked();
    }

}
