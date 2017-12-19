package com.example.ithappenedandroid.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ithappenedandroid.Activities.AddNewEventActivity;
import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Recyclers.EventsAdapter;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.List;

/**
 * Created by Пользователь on 16.12.2017.
 */

public class EventsForTrackingFragment extends Fragment {

    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;

    FloatingActionButton addNewEvent;

    List<Event> events;

    ITrackingRepository trackingsCollection;
    TrackingService trackingService;
    int trackingPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_history_for_tracking, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();

        if(bundle!=null){
            trackingPosition = bundle.getInt("position");
        }

        trackingsCollection = StaticInMemoryRepository.getInstance();
        trackingService = new TrackingService("testUser", trackingsCollection);

        Tracking thisTracking = trackingsCollection.GetTrackingCollection().get(trackingPosition);

        events = trackingsCollection.GetTrackingCollection().get(trackingPosition).GetEventCollection();

        eventsRecycler = (RecyclerView) view.findViewById(R.id.eventsForTrackingRV);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsAdpt = new EventsAdapter(events , getActivity());
        eventsRecycler.setAdapter(eventsAdpt);


        addNewEvent = (FloatingActionButton) getActivity().findViewById(R.id.addNewEvent);

        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddNewEventActivity.class);
                intent.putExtra("tracking", trackingPosition);

                startActivity(intent);

            }
        });

    }

}
