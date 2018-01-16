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
import android.widget.TextView;

import com.example.ithappenedandroid.Activities.AddNewEventActivity;
import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Recyclers.EventsAdapter;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.List;
import java.util.UUID;

public class EventsForTrackingFragment extends Fragment {

    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;

    TextView hintForEvents;

    FloatingActionButton addNewEvent;
    Tracking thisTracking;

    List<Event> events;
    UUID trackingId;

    ITrackingRepository trackingsCollection = StaticInMemoryRepository.getInstance();;
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
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = this.getArguments();

        hintForEvents = (TextView) getActivity().findViewById(R.id.hintForEventsFragment);

        if(trackingsCollection.FilterEvents(null, null, null, null, null, null, null).size()!=0){
            hintForEvents.setVisibility(View.INVISIBLE);
        }

        if(bundle!=null){
            trackingId = UUID.fromString(bundle.getString("id"));
        }

        trackingService = new TrackingService("testUser", trackingsCollection);

        thisTracking = trackingsCollection.GetTracking(trackingId);

        events = thisTracking.GetEventCollection();

        getActivity().setTitle(thisTracking.GetTrackingName());

        eventsRecycler = (RecyclerView) getActivity().findViewById(R.id.eventsForTrackingRV);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsAdpt = new EventsAdapter(events , getActivity(), 0);
        eventsRecycler.setAdapter(eventsAdpt);


        addNewEvent = (FloatingActionButton) getActivity().findViewById(R.id.addNewEvent);

        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddNewEventActivity.class);
                intent.putExtra("trackingId", thisTracking.GetTrackingID().toString());

                startActivity(intent);

            }
        });
    }
}
