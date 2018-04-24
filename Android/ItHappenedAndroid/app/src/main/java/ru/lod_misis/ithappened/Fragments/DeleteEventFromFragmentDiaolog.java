package ru.lod_misis.ithappened.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.Recyclers.EventsAdapter;
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

                        ITrackingRepository collection;

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS",Context.MODE_PRIVATE);

                        if(sharedPreferences.getString("LastId","").isEmpty()) {
                            collection = new StaticInMemoryRepository(getActivity().getApplicationContext(),
                                    sharedPreferences.getString("UserId", "")).getInstance();
                        }else{
                            collection = new StaticInMemoryRepository(getActivity().getApplicationContext(),
                                    sharedPreferences.getString("LastId", "")).getInstance();
                        }
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
                        EventsFragment eventsFragment = (EventsFragment) getActivity().getFragmentManager().findFragmentByTag("EVENTS_HISTORY");
                        List<Event> events = eventsFragment.eventsAdpt.getEvents();
                        for(int i=0;i<events.size();i++){
                            if(events.get(i).GetEventId().equals(eventId)){
                                events.remove(i);
                                break;
                            }
                        }
                        if(events.size()==0){
                            eventsFragment.hintForEventsHistory.setVisibility(View.VISIBLE);
                        }
                        eventsFragment.eventsRecycler.setAdapter(new EventsAdapter(events, getActivity(), 1));
                        Toast.makeText(getActivity().getApplicationContext(), "Событие удалено", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();

    }

}
