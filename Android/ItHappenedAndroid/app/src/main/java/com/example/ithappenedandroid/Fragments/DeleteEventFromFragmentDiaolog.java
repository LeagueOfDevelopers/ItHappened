package com.example.ithappenedandroid.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ithappenedandroid.Activities.UserActionsActivity;
import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.UUID;

/**
 * Created by Пользователь on 10.02.2018.
 */

public class DeleteEventFromFragmentDiaolog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы болше не сможете восстановить это событие!")
                .setTitle("Вы действительно хотите удалить это событие?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = getArguments();
                        UUID trackingId = UUID.fromString(bundle.getString("trackingId"));
                        UUID eventId = UUID.fromString(bundle.getString("eventId"));

                        ITrackingRepository collection = new StaticInMemoryRepository(getActivity().getApplicationContext()).getInstance();
                        TrackingService trackingSercvice = new TrackingService("testUser", collection);

                        trackingSercvice.RemoveEvent(trackingId, eventId);
                        Toast.makeText(getActivity().getApplicationContext(), "Событие удалено", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplicationContext(), UserActionsActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();

    }

}
