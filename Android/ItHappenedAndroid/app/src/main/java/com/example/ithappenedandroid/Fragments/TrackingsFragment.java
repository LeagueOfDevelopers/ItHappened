package com.example.ithappenedandroid.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ithappenedandroid.Activities.AddNewTrackingActivity;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Recyclers.TrackingsAdapter;
import com.example.ithappenedandroid.TrackingLoader;

public class TrackingsFragment extends Fragment {

    FragmentTransaction fTrans;
    RecyclerView trackingsRecycler;
    TrackingsAdapter trackAdpt;
    TrackingLoader trackLoad;
    FloatingActionButton addTracking;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trackings, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trackingsRecycler = (RecyclerView)getActivity().findViewById(R.id.tracingsRV);
        trackingsRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        trackLoad = new TrackingLoader();
        trackAdpt = new TrackingsAdapter(trackLoad.loadingData(),getActivity());
        trackingsRecycler.setAdapter(trackAdpt);

        addTracking = (FloatingActionButton) getActivity().findViewById(R.id.addNewTracking);
        addTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddNewTrackingActivity.class);
                startActivity(intent);

            }
        });
    }
}
