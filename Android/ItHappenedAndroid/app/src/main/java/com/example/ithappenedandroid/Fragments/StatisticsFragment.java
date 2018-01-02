package com.example.ithappenedandroid.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Recyclers.StatisticsAdapter;
import com.example.ithappenedandroid.StaticInMemoryRepository;

public class StatisticsFragment extends Fragment {

    ITrackingRepository trackingCollection = StaticInMemoryRepository.getInstance();
    RecyclerView statisticsRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statisticsRecycler = (RecyclerView) view.findViewById(R.id.statisticsRV);
        statisticsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        StatisticsAdapter adapter = new StatisticsAdapter(trackingCollection.GetTrackingCollection(), getActivity());
        statisticsRecycler.setAdapter(adapter);

    }
}
