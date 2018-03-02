package com.lod.ithappened.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lod.ithappened.Activities.AddNewTrackingActivity;
import com.lod.ithappened.Application.TrackingService;
import com.lod.ithappened.Domain.Tracking;
import com.lod.ithappened.Infrastructure.ITrackingRepository;
import com.lod.ithappened.R;
import com.lod.ithappened.Recyclers.TrackingsAdapter;
import com.lod.ithappened.StaticInMemoryRepository;

import java.util.ArrayList;
import java.util.List;

public class TrackingsFragment extends Fragment {

    TextView hintForTrackings;

    FragmentTransaction fTrans;
    RecyclerView trackingsRecycler;
    TrackingsAdapter trackAdpt;
    FloatingActionButton addTracking;
    ITrackingRepository trackingCollection;
    String userName;
    TrackingService trackingService;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trackings, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Мои отслеживания");
        hintForTrackings = (TextView) getActivity().findViewById(R.id.hintForTrackingsFragment);

        StaticInMemoryRepository repository = new StaticInMemoryRepository(getActivity().getApplicationContext());
        trackingCollection = repository.getInstance();
        userName = "testUser";
        trackingService = new TrackingService(userName, trackingCollection);



        trackingsRecycler = (RecyclerView)getActivity().findViewById(R.id.tracingsRV);
        trackingsRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        List<Tracking> allTrackings = trackingService.GetTrackingCollection();
        List<Tracking> visibleTrackings = new ArrayList<>();

        for(int i =0;i<allTrackings.size();i++){
            if(!allTrackings.get(i).GetStatus()){
                visibleTrackings.add(allTrackings.get(i));
            }
        }

        if(visibleTrackings.size()!=0){
            hintForTrackings.setVisibility(View.INVISIBLE);
        }

        //trackLoad = new TrackingLoader();
        trackAdpt = new TrackingsAdapter(visibleTrackings,getActivity());
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}