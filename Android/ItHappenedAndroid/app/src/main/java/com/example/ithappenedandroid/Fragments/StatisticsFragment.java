package com.example.ithappenedandroid.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Recyclers.StatisticsAdapter;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.ArrayList;
import java.util.List;


public class StatisticsFragment extends Fragment {

    RecyclerView trackingsRecycler;
    StatisticsAdapter trackAdpt;
    TrackingService service;
    ITrackingRepository collection;

    TextView hint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Статистика");
        trackingsRecycler = (RecyclerView) getActivity().findViewById(R.id.statisticsRV);

        hint = (TextView) getActivity().findViewById(R.id.hintForStatisticsFragment);
        trackingsRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        collection = new StaticInMemoryRepository(getActivity().getApplicationContext()).getInstance();
        service = new TrackingService("", collection);


        List<Tracking> allTrackings = service.GetTrackingCollection();
        List<Tracking> visibleTrackings = new ArrayList<>();

        for(int i =0;i<allTrackings.size();i++){
            if(!allTrackings.get(i).GetStatus()){
                visibleTrackings.add(allTrackings.get(i));
            }
        }

        if(visibleTrackings.size()!=0) {
            hint.setVisibility(View.INVISIBLE);
            trackAdpt = new StatisticsAdapter(visibleTrackings, getActivity());
            trackingsRecycler.setAdapter(trackAdpt);
        }
    }

}
