package com.example.ithappenedandroid.Fragments.Statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Gui.MultiSpinner;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;
import com.example.ithappenedandroid.StatisticsHelpers.GraphStatisticsHelper;
import com.github.mikephil.charting.charts.LineChart;

import java.util.UUID;

public class GraphsFragment extends Fragment {

    GraphStatisticsHelper helper;
    GraphTimeTypes timeTypes;
    Button addParams;
    LineChart graph;
    Spinner graphType;
    MultiSpinner trackingsSpinner;
    ITrackingRepository collection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graphs, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        graph = (LineChart) getActivity().findViewById(R.id.graph);
        graphType = (Spinner) getActivity().findViewById(R.id.typeSpinner);
        trackingsSpinner = (MultiSpinner) getActivity().findViewById(R.id.trackingsGraphSpinner);
        addParams = (Button) getActivity().findViewById(R.id.addParams);
        collection = new StaticInMemoryRepository(getActivity().getApplicationContext()).getInstance();

        timeTypes = GraphTimeTypes.ALLTIME;

        Tracking tracking = collection.GetTracking(UUID.fromString(getActivity().getIntent().getStringExtra("id")));

        helper = new GraphStatisticsHelper(tracking);

    }

    private void initLineChart(LineChart graph, GraphTimeTypes timeTypes){

        switch (timeTypes){
            case ALLTIME:
        }

    }

    private void intLineChartMultiTrackings(LineChart graph, GraphTimeTypes timeTypes){

    }
}
