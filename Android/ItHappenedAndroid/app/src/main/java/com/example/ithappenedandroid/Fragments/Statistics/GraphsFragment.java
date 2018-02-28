package com.example.ithappenedandroid.Fragments.Statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ithappenedandroid.Domain.Event;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
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

    List<Tracking> all;

    Button dateFromStatististics;
    Button dateToStatistics;
    TextView dateFromText;
    TextView dateToText;

    GraphStatisticsHelper helper;
    GraphTimeTypes timeTypes;
    Button addParams;
    LineChart graph;
    Spinner graphType;
    MultiSpinner trackingsSpinner;
    ITrackingRepository collection;

    List<Tracking> trackingsForFilter;

    RelativeLayout userPeriodLayout;

    ScrollView visibility;
    TextView hint;
    TextView hintForTrackingsSpinner;

    List<Boolean> flags;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graphs, null);
    }

    @Override
    public void onResume() {
        super.onResume();

        final UUID trackingId = UUID.fromString(getActivity().getIntent().getStringExtra("id"));

        all = new ArrayList<>();
        trackingsForFilter = new ArrayList<>();
        flags = new ArrayList<>();

        graph = (LineChart) getActivity().findViewById(R.id.graph);
        graphType = (Spinner) getActivity().findViewById(R.id.typeSpinner);
        trackingsSpinner = (MultiSpinner) getActivity().findViewById(R.id.trackingsGraphSpinner);
        addParams = (Button) getActivity().findViewById(R.id.addParams);
        dateFromStatististics = (Button) getActivity().findViewById(R.id.dateFromGraphButton) ;
        dateToStatistics = (Button) getActivity().findViewById(R.id.dateToGraphButton);
        dateFromText = (TextView) getActivity().findViewById(R.id.dateFromGraph);
        dateToText = (TextView) getActivity().findViewById(R.id.dateToGraph);
        userPeriodLayout = (RelativeLayout) getActivity().findViewById(R.id.visibilityLayout);
        userPeriodLayout.setVisibility(View.INVISIBLE);
        collection = new StaticInMemoryRepository(getActivity().getApplicationContext()).getInstance();
        hint = (TextView) getActivity().findViewById(R.id.hintForGraphFragment);
        visibility = (ScrollView) getActivity().findViewById(R.id.visibilityScrollView);
        hintForTrackingsSpinner = (TextView) getActivity().findViewById(R.id.trackingsGraphSpinnerHint);
        hintForTrackingsSpinner.setVisibility(View.VISIBLE);
        trackingsSpinner.setVisibility(View.INVISIBLE);

        for(int i = 0;i< collection.GetTrackingCollection().size();i++){
            if(!collection.GetTrackingCollection().get(i).GetStatus()){
                all.add(collection.GetTrackingCollection().get(i));
            }
        }

        if(collection.GetTracking(UUID.fromString(getActivity().getIntent().getStringExtra("id"))).GetEventCollection().size()==0){
            visibility.setVisibility(View.INVISIBLE);
            hint.setVisibility(View.VISIBLE);
            addParams.setVisibility(View.INVISIBLE);
        }


        for(Event event : collection.GetTracking(UUID.fromString(getActivity().getIntent().getStringExtra("id"))).GetEventCollection()){
            if(!event.GetStatus()){
                hint.setVisibility(View.INVISIBLE);
                visibility.setVisibility(View.VISIBLE);
            }
        }

        graph.getLegend().setEnabled(false);
        graph.setNoDataText("Нет данных");
        graph.setNoDataTextColor(getResources().getColor(R.color.colorPrimaryDark));

            for (int i = 0;i<all.size();i++){
                if(all.get(i).GetTrackingID().equals(trackingId)){
                    all.remove(i);
                    break;
                }
            }
            timeTypes = GraphTimeTypes.ALLTIME;

            final Tracking tracking = collection.GetTracking(UUID.fromString(getActivity().getIntent().getStringExtra("id")));

            helper = new GraphStatisticsHelper(tracking);
            initLineChart(graph, timeTypes, tracking, 1);

            ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    spinnerHints);

            final List<UUID> uuids = new ArrayList<>();
            final List<String> strings = new ArrayList<>();

            for (int i = 0; i < all.size(); i++) {
                if (!all.get(i).GetStatus()) {
                    strings.add(all.get(i).GetTrackingName());
                    uuids.add(all.get(i).GetTrackingID());
                    flags.add(false);
                }
            }

            graphType.setAdapter(adapter);

            graphType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (spinnerTypes[i].equals(GraphTimeTypes.USERTYPE)) {
                        userPeriodLayout.setVisibility(View.VISIBLE);
                    }else {
                        userPeriodLayout.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            dateFromStatististics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    DatePickerSatisticsFragment picker = new DatePickerSatisticsFragment(dateFromText);
                    picker.show(fragmentManager, "from");
                }
            });

            dateToStatistics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    DatePickerSatisticsFragment picker = new DatePickerSatisticsFragment(dateToText);
                    picker.show(fragmentManager, "to");
                }
            });



                List<UUID> trackings = new ArrayList<>();
                List<String> trackingNames = new ArrayList<>();

                for (Tracking trackingForHint : all) {
                    if (!trackingForHint.GetStatus()) {
                        trackings.add(trackingForHint.GetTrackingID());
                        trackingNames.add(trackingForHint.GetTrackingName());
                    }
                }

                String allText = "";
                for (int i = 0; i < trackingNames.size(); i++) {
                    if (i != trackingNames.size()) {
                        allText += trackingNames.get(i) + ", ";
                    }
                }

                final List<String> filteredTrackingsTitles = new ArrayList<>();
                final List<UUID> filteredTrackingsUuids = new ArrayList<>();

                for (int i = 0; i < all.size(); i++) {
                    filteredTrackingsUuids.add(uuids.get(i));
                }

        if(all.size()!=0) {
                    hintForTrackingsSpinner.setVisibility(View.INVISIBLE);
                    trackingsSpinner.setVisibility(View.VISIBLE);
                trackingsSpinner.setItems(trackingNames, allText.substring(0, allText.length() - 2), new MultiSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                        for (int i = 0; i < selected.length; i++) {

                            Log.e("FILTER", selected[i] + "");
                            if (selected[i]) {
                                filteredTrackingsTitles.add(strings.get(i));
                                if (flags.get(i)) {
                                    filteredTrackingsUuids.add(uuids.get(i));
                                }
                            }
                            if (!selected[i]) {
                                filteredTrackingsUuids.remove(uuids.get(i));
                                flags.set(i, true);
                            }
                        }
                    }
                });
            }

            addParams.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final long selectedItemId = graphType.getSelectedItemId();

                    graph.clear();
                    initLineChart(graph, spinnerTypes[(int) graphType.getSelectedItemId()], tracking, 1);

                    for(int i = 0;i<filteredTrackingsUuids.size();i++){
                        trackingsForFilter.add(collection.GetTracking(filteredTrackingsUuids.get(i)));
                    }

                    if(trackingsForFilter.size()!=0) {
                        for (int i = 0; i < trackingsForFilter.size(); i++) {
                            initLineChart(graph, spinnerTypes[(int) graphType.getSelectedItemId()], trackingsForFilter.get(i), 2);
                        }
                    }

                    trackingsForFilter.clear();
                }
            });
        }

    private void initLineChart(LineChart graph, GraphTimeTypes timeTypes, Tracking tracking , int state){

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
                if(state == 1) {
                    createLineChart(graph, entries);
                }else{
                    createMultiGraph(graph, entries, null);
                }
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

                if(state == 1) {
                    createLineChart(graph, entriesHalfYear);
                }else{
                    createMultiGraph(graph, entriesHalfYear, null);
                }

                break;

            case LASTWEEK:
                LinkedHashMap<Date, Integer> lastWeekGraphData = new GraphStatisticsHelper(tracking).lastWeekGraphData();
                if(lastWeekGraphData.size()!=0) {
                    ArrayList<Date> xDataLastWeek = new ArrayList<>(lastWeekGraphData.keySet());
                    ArrayList<Integer> yDataLastWeek = new ArrayList<>(lastWeekGraphData.values());
                    ArrayList<Entry> entriesLastWeek = new ArrayList<>();

                    for (int i = 0; i < xDataLastWeek.size(); i++) {
                        entriesLastWeek.add(new Entry(new Long(xDataLastWeek.get(i).getTime()).floatValue(), yDataLastWeek.get(i)));
                    }

                    Collections.sort(entriesLastWeek, new EntryXComparator());

                    if (state == 1) {
                        createLineChart(graph, entriesLastWeek);
                    } else {
                        createMultiGraph(graph, entriesLastWeek, null);
                    }
                }

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

                if(state == 1) {
                    createLineChart(graph, entriesLastYear);
                }else{
                    createMultiGraph(graph, entriesLastYear, null);
                }

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

                if(state == 1) {
                    createLineChart(graph, entriesLastMonth);
                }else{
                    createMultiGraph(graph, entriesLastMonth, null);
                }

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

                if(state == 1) {
                createLineChart(graph, entriesThreeMonth);
            }else{
                createMultiGraph(graph, entriesThreeMonth, null);
            }

                break;

            case USERTYPE:

                if(dateFromText.getText().toString().equals("Начальная дата")||dateToText.getText().toString().equals("Конечная дата")){
                    Toast.makeText(getActivity().getApplicationContext(), "Задайте период", Toast.LENGTH_SHORT).show();
                }else{

                    Date dateFrom = new Date();
                    Date dateTo = new Date();

                    Locale locale = new Locale("ru");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);
                    try {
                        dateFrom = simpleDateFormat.parse(dateFromText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        dateTo = simpleDateFormat.parse(dateToText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    LinkedHashMap<Date, Integer> userPeriodGraphData = new GraphStatisticsHelper(tracking).userPeriodGraphData(dateFrom, dateTo);
                    ArrayList<Date> xDataUserPeriod = new ArrayList<>(userPeriodGraphData.keySet());
                    ArrayList<Integer> yDataUserPeriod = new ArrayList<>(userPeriodGraphData.values());
                    ArrayList<Entry> entriesUserPeriod = new ArrayList<>();

                    for(int i =0;i<xDataUserPeriod.size();i++){
                        entriesUserPeriod.add(new Entry(new Long(xDataUserPeriod.get(i).getTime()).floatValue(), yDataUserPeriod.get(i)));
                    }

                    Collections.sort(entriesUserPeriod, new EntryXComparator());

                    if(state == 1) {
                        createLineChart(graph, entriesUserPeriod);
                    }else{
                        createMultiGraph(graph, entriesUserPeriod, null);
                    }

                }

                break;


                default:
                    break;
        };

    }

    private void initLineChartMultiTrackings(LineChart graph, GraphTimeTypes timeTypes){

    }

    private void createLineChart(LineChart graph, ArrayList<Entry> entries) {


        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorAccent));

        if (entries.size() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "Данных нет", Toast.LENGTH_SHORT).show();
        } else {

            LineDataSet dataSet = new LineDataSet(entries, "Данное отслеживание");
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

    private void createMultiGraph(LineChart graph, ArrayList<Entry> entries, List<Integer> color) {
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorAccent));

        if (entries.size() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "Данных нет", Toast.LENGTH_SHORT).show();
        } else {

            List<ILineDataSet> graphData = new ArrayList<>();
            if (graph.getData() != null) {
                for (int i = 0; i < graph.getData().getDataSetCount(); i++)
                    graphData.add(graph.getData().getDataSetByIndex(i));
            }
            LineDataSet newSet = new LineDataSet(entries, "Выбранные отслеживания");

            Random rnd = new Random();
            int colorSet = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            newSet.setColor(colorSet);
            graphData.add(newSet);

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

            LineData data = new LineData(graphData);
            graph.setData(data);
            graph.fitScreen();

            graph.getDescription().setEnabled(false);
            graph.invalidate();
        }
    }

}
