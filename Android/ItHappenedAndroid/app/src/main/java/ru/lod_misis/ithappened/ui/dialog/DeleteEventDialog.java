package ru.lod_misis.ithappened.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.yandex.metrica.YandexMetrica;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.ui.activities.EventDetailsActivity;
import ru.lod_misis.ithappened.ui.presenters.DeleteCallback;
import ru.lod_misis.ithappened.ui.presenters.DeleteContract;

public class DeleteEventDialog extends DialogFragment {

    DeleteCallback deleteCallback;

    private UUID trackingId;
    private UUID eventId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().getString(EventDetailsActivity.EVENT_ID) != null && getArguments().getString(EventDetailsActivity.TRACKING_ID) != null) {
            trackingId = UUID.fromString(getArguments().getString(EventDetailsActivity.TRACKING_ID));
            eventId = UUID.fromString(getArguments().getString(EventDetailsActivity.EVENT_ID));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы болше не сможете восстановить это событие!")
                .setTitle("Вы действительно хотите удалить это событие?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        okClicked(trackingId, eventId);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelClicked();
                    }
                });

        return builder.create();

    }

    public void setDeleteCallback(DeleteCallback deleteCallback) {
        this.deleteCallback = deleteCallback;
    }


    private void okClicked(UUID trackingId, UUID eventId) {
        deleteCallback.finishDeleting(trackingId, eventId);
        YandexMetrica.reportEvent("Пользователь удалил событие");
    }

    public void cancelClicked() {
        deleteCallback.cansel();
    }

}
