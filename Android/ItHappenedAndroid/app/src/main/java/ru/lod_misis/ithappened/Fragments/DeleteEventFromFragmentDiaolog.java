package ru.lod_misis.ithappened.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

import ru.lod_misis.ithappened.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Пользователь on 10.02.2018.
 */

public class DeleteEventFromFragmentDiaolog extends DialogFragment {
    InMemoryFactRepository factRepository;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы болше не сможете восстановить это событие!")
                .setTitle("Вы действительно хотите удалить это событие?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        factRepository = StaticFactRepository.getInstance();
                        Bundle bundle = getArguments();
                        UUID trackingId = UUID.fromString(bundle.getString("trackingId"));
                        UUID eventId = UUID.fromString(bundle.getString("eventId"));

                        ITrackingRepository collection = new StaticInMemoryRepository(getActivity().getApplicationContext(),
                                getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId","")).getInstance();
                        TrackingService trackingSercvice = new TrackingService("testUser", collection);

                        trackingSercvice.RemoveEvent(trackingId, eventId);
                        factRepository.onChangeCalculateOneTrackingFacts(collection.GetTrackingCollection(), trackingId)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call(Fact fact) {
                                        Log.d("Statistics", "calculateOneTrackingFact");
                                    }
                                });
                        factRepository.calculateAllTrackingsFacts(collection.GetTrackingCollection())
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call(Fact fact) {
                                        Log.d("Statistics", "calculateOneTrackingFact");
                                    }
                                });
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
