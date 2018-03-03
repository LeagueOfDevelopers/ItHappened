package ru.lod_misis.ithappened.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Recyclers.StatisticsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;


public class StatisticsFragment extends Fragment {

    RecyclerView trackingsRecycler;
    StatisticsAdapter trackAdpt;
    TrackingService service;
    ITrackingRepository collection;

    TextView hint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ru.lod_misis.ithappened.R.layout.fragment_statistics, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Статистика");
        trackingsRecycler = (RecyclerView) getActivity().findViewById(ru.lod_misis.ithappened.R.id.statisticsRV);

        hint = (TextView) getActivity().findViewById(ru.lod_misis.ithappened.R.id.hintForStatisticsFragment);
        trackingsRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        collection = new StaticInMemoryRepository(getActivity().getApplicationContext(), sharedPreferences.getString("USerId", "")).getInstance();
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
