package ru.lod_misis.ithappened.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.ui.recyclers.EventsAdapter;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Пользователь on 10.02.2018.
 */

public class DeleteEventFromFragmentDialog extends DialogFragment {

    @Inject
    FactService factService;
    @Inject
    TrackingService trackingService;
    @Inject
    TrackingDataSource trackingRepository;

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {

        ItHappenedApplication.getAppComponent().inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы болше не сможете восстановить это событие!")
                .setTitle("Вы действительно хотите удалить это событие?")
                .setPositiveButton("Да" , new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog , int id) {
                        Bundle bundle = getArguments();
                        UUID trackingId = UUID.fromString(bundle.getString("trackingId"));
                        UUID eventId = UUID.fromString(bundle.getString("eventId"));


                        trackingService.RemoveEvent(eventId);
                        factService.onChangeCalculateOneTrackingFacts(trackingService.GetTrackingCollection() , trackingId)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call (Fact fact) {
                                        Log.d("statistics" , "calculateOneTrackingFact");
                                    }
                                });
                        factService.calculateAllTrackingsFacts(trackingService.GetTrackingCollection())
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call (Fact fact) {
                                        Log.d("statistics" , "calculateOneTrackingFact");
                                    }
                                });
                        EventsFragment eventsFragment = ( EventsFragment ) getActivity().getFragmentManager().findFragmentByTag("EVENTS_HISTORY");
                        List<EventV1> eventV1s = eventsFragment.eventsAdpt.getEventV1s();
                        for (int i = 0; i < eventV1s.size(); i++) {
                            if ( eventV1s.get(i).getEventId().equals(eventId) ) {
                                eventV1s.remove(i);
                                break;
                            }
                        }
                        if ( eventV1s.size() == 0 ) {
                            eventsFragment.hintForEventsHistory.setVisibility(View.VISIBLE);
                        }
                        eventsFragment.eventsRecycler.setAdapter(new EventsAdapter(eventV1s , getActivity() , 1 , trackingRepository));
                        Toast.makeText(getActivity().getApplicationContext() , "Событие удалено" , Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена" , new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog , int id) {
                    }
                });

        return builder.create();

    }

}