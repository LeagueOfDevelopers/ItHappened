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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
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
        initLineChart(graph, timeTypes, tracking);

    }

    private void initLineChart(LineChart graph, GraphTimeTypes timeTypes, Tracking tracking){

        switch (timeTypes){
            case ALLTIME:
                LinkedHashMap<Date, Integer> allTimesGraphData = new GraphStatisticsHelper(tracking).allTimesGraphData();
                ArrayList<Date> xData = new ArrayList<>(allTimesGraphData.keySet());
                ArrayList<Integer> yData = new ArrayList<>(allTimesGraphData.values());
                ArrayList<Entry> entries = new ArrayList<>();

                for(int i =0;i<xData.size();i++){
                    entries.add(new Entry(new Long(xData.get(i).getTime()).floatValue(), yData.get(i)));
                }

                Collections.sort(entries, new EntryXComparator());

                createLineChart(graph, entries);

                break;
        };

    }

    private void initLineChartMultiTrackings(LineChart graph, GraphTimeTypes timeTypes){

    }

    private void createLineChart(LineChart graph, ArrayList<Entry> entries){
        if(graph.getData()!=null) {
            graph.clearValues();
            graph.clear();
        }

        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorAccent));

        LineDataSet dataSet = new LineDataSet(entries, "Кек");
        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(5f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.yyyy");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                Date s = new Date((long) (value));
                return mFormat.format(s);
            }
        });

        dataSet.setColors(colors);
        LineData data = new LineData(dataSet);
        graph.setData(data);
        graph.fitScreen();

        graph.getDescription().setEnabled(false);
        graph.invalidate();
    }
}
