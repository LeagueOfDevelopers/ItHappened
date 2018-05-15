package ru.lod_misis.ithappened.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Activities.AddNewTrackingActivity;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.TrackingsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;

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
        getActivity().setTitle("Что произошло?");
        hintForTrackings = (TextView) getActivity().findViewById(R.id.hintForTrackingsFragment);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("LastId","").isEmpty()) {
            trackingCollection = StaticInMemoryRepository.getInstance();
        }else{
            trackingCollection = StaticInMemoryRepository.getInstance();
        }
        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), trackingCollection);




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