package com.example.ithappenedandroid.Fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ithappenedandroid.EventsLoader;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Recyclers.EventsAdapter;

/**
 * Created by Пользователь on 16.12.2017.
 */

public class EventsForTrackingFragment extends Fragment {

    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;
    EventsLoader eventsLoad;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_history_for_tracking, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventsLoad = new EventsLoader();
        eventsRecycler = (RecyclerView) view.findViewById(R.id.eventsForTrackingRV);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        eventsAdpt = new EventsAdapter(eventsLoad.loadingEvents(), getActivity());
        eventsRecycler.setAdapter(eventsAdpt);

    }

}
