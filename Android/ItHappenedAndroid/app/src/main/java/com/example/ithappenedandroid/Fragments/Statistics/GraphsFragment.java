package com.example.ithappenedandroid.Fragments.Statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
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

    String[] spinnerHints = {"За все время", "За год", "За пол года", "За месяц", "За неделю", "За три месяца", "Ваш период"};
    GraphTimeTypes[] spinnerTypes = {
            GraphTimeTypes.ALLTIME,
            GraphTimeTypes.LASTYEAR,
            GraphTimeTypes.HALFYEAR,
            GraphTimeTypes.LASTMONTH,
            GraphTimeTypes.LASTWEEK,
            GraphTimeTypes.THREEMONT,
            GraphTimeTypes.USERTYPE
    };

    GraphStatisticsHelper helper;
    GraphTimeTypes timeTypes;
    Button addParams;
    LineChart graph;
    Spinner graphType;
    MultiSpinner trackingsSpinner;
    ITrackingRepository collection;

    RelativeLayout userPeriodLayout;

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
        userPeriodLayout = (RelativeLayout) getActivity().findViewById(R.id.visibilityLayout);
        userPeriodLayout.setVisibility(View.INVISIBLE);
        collection = new StaticInMemoryRepository(getActivity().getApplicationContext()).getInstance();

        timeTypes = GraphTimeTypes.ALLTIME;

        final Tracking tracking = collection.GetTracking(UUID.fromString(getActivity().getIntent().getStringExtra("id")));

        helper = new GraphStatisticsHelper(tracking);
        initLineChart(graph, timeTypes, tracking);

        ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(),
                R.layout.support_simple_spinner_dropdown_item,
                spinnerHints);

        graphType.setAdapter(adapter);

        graphType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerTypes[i].equals(GraphTimeTypes.USERTYPE)){
                    userPeriodLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<UUID> trackings  = new ArrayList<>();
        List<String> trackingNames = new ArrayList<>();

        for(Tracking trackingForHint : collection.GetTrackingCollection()){
            if(!trackingForHint.GetStatus()){
                trackings.add(trackingForHint.GetTrackingID());
                trackingNames.add(trackingForHint.GetTrackingName());
            }
        }

        String allText = "";
        for(int i=0;i<trackingNames.size();i++) {
            if (i != trackingNames.size()) {
                allText += trackingNames.get(i) + ", ";
            }
        }

        trackingsSpinner.setItems(trackingNames, allText.substring(0, allText.length() - 2), new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                
            }
        });

        addParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final long selectedItemId = graphType.getSelectedItemId();
                initLineChart(graph, spinnerTypes[(int) graphType.getSelectedItemId()], tracking);
            }
        });
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

            case HALFYEAR:
                LinkedHashMap<Date, Integer> halfYearGraphData = new GraphStatisticsHelper(tracking).halfYearGraphData();
                ArrayList<Date> xDataHalfYear = new ArrayList<>(halfYearGraphData.keySet());
                ArrayList<Integer> yDataHalfYear = new ArrayList<>(halfYearGraphData.values());
                ArrayList<Entry> entriesHalfYear = new ArrayList<>();

                for(int i =0;i<xDataHalfYear.size();i++){
                    entriesHalfYear.add(new Entry(new Long(xDataHalfYear.get(i).getTime()).floatValue(), yDataHalfYear.get(i)));
                }

                Collections.sort(entriesHalfYear, new EntryXComparator());

                createLineChart(graph, entriesHalfYear);

                break;

            case LASTWEEK:
                LinkedHashMap<Date, Integer> lastWeekGraphData = new GraphStatisticsHelper(tracking).lastWeekGraphData();
                ArrayList<Date> xDataLastWeek = new ArrayList<>(lastWeekGraphData.keySet());
                ArrayList<Integer> yDataLastWeek = new ArrayList<>(lastWeekGraphData.values());
                ArrayList<Entry> entriesLastWeek = new ArrayList<>();

                for(int i =0;i<xDataLastWeek.size();i++){
                    entriesLastWeek.add(new Entry(new Long(xDataLastWeek.get(i).getTime()).floatValue(), yDataLastWeek.get(i)));
                }

                Collections.sort(entriesLastWeek, new EntryXComparator());

                createLineChart(graph, entriesLastWeek);

                break;

            case LASTYEAR:
                LinkedHashMap<Date, Integer> lastYearGraphData = new GraphStatisticsHelper(tracking).lastYearGraphData();
                ArrayList<Date> xDataLastYear = new ArrayList<>(lastYearGraphData.keySet());
                ArrayList<Integer> yDataLastYear = new ArrayList<>(lastYearGraphData.values());
                ArrayList<Entry> entriesLastYear = new ArrayList<>();

                for(int i =0;i<xDataLastYear.size();i++){
                    entriesLastYear.add(new Entry(new Long(xDataLastYear.get(i).getTime()).floatValue(), yDataLastYear.get(i)));
                }

                Collections.sort(entriesLastYear, new EntryXComparator());

                createLineChart(graph, entriesLastYear);

                break;

            case LASTMONTH:
                LinkedHashMap<Date, Integer> lastMonthGraphData = new GraphStatisticsHelper(tracking).lastMonthGraphData();
                ArrayList<Date> xDataLastMonth = new ArrayList<>(lastMonthGraphData.keySet());
                ArrayList<Integer> yDataLastMonth = new ArrayList<>(lastMonthGraphData.values());
                ArrayList<Entry> entriesLastMonth = new ArrayList<>();

                for(int i =0;i<xDataLastMonth.size();i++){
                    entriesLastMonth.add(new Entry(new Long(xDataLastMonth.get(i).getTime()).floatValue(), yDataLastMonth.get(i)));
                }

                Collections.sort(entriesLastMonth, new EntryXComparator());

                createLineChart(graph, entriesLastMonth);

                break;

            case THREEMONT:
                LinkedHashMap<Date, Integer> threeMonthGraphData = new GraphStatisticsHelper(tracking).lastThreeMonthGraphData();
                ArrayList<Date> xDataThreeMonth = new ArrayList<>(threeMonthGraphData.keySet());
                ArrayList<Integer> yDataThreeMonth = new ArrayList<>(threeMonthGraphData.values());
                ArrayList<Entry> entriesThreeMonth = new ArrayList<>();

                for(int i =0;i<xDataThreeMonth.size();i++){
                    entriesThreeMonth.add(new Entry(new Long(xDataThreeMonth.get(i).getTime()).floatValue(), yDataThreeMonth.get(i)));
                }

                Collections.sort(entriesThreeMonth, new EntryXComparator());

                createLineChart(graph, entriesThreeMonth);

                break;

            case USERTYPE:
                break;


                default:
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
