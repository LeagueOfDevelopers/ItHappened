package ru.lod_misis.ithappened.Fragments.Statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Gui.MultiSpinner;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.StatisticsHelpers.DiagramsStatisticsHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiagrammsFragment extends android.support.v4.app.Fragment {

    PieChart diagramm;
    MultiSpinner allTrackings;
    ITrackingRepository collection;
    DiagramsStatisticsHelper helper;
    List<Boolean> flags;
    PieData data;
    TextView spinnerHint;

    TextView hint;
    RelativeLayout visibility;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diagramms, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        diagramm = (PieChart) getActivity().findViewById(R.id.diagramm);
        allTrackings = (MultiSpinner) getActivity().findViewById(R.id.diagrammStatisticsTrackings);
        final UUID trackingId = UUID.fromString(getActivity().getIntent().getStringExtra("id"));
        collection = new StaticInMemoryRepository(getActivity().getApplicationContext()).getInstance();
        helper = new DiagramsStatisticsHelper(collection.GetTracking(trackingId));
        visibility = (RelativeLayout) getActivity().findViewById(R.id.visibilityDiagramm);
        hint = (TextView) getActivity().findViewById(R.id.hintForDiagrammFragment);
        spinnerHint = (TextView) getActivity().findViewById(R.id.diagrammHintTrackings);
        spinnerHint.setVisibility(View.VISIBLE);
        allTrackings.setVisibility(View.INVISIBLE);

        List<Tracking> all = new ArrayList<>();
        for (int i = 0; i < collection.GetTrackingCollection().size(); i++) {
            if (!collection.GetTrackingCollection().get(i).GetStatus()) {
                all.add(collection.GetTrackingCollection().get(i));
            }
        }

        if(collection.GetTracking(UUID.fromString(getActivity().getIntent().getStringExtra("id"))).GetEventCollection().size()==0){
            visibility.setVisibility(View.INVISIBLE);
            hint.setVisibility(View.VISIBLE);
        }

        for (Event event : collection.GetTracking(UUID.fromString(getActivity().getIntent().getStringExtra("id"))).GetEventCollection()) {
            if (!event.GetStatus()) {
                hint.setVisibility(View.INVISIBLE);
                visibility.setVisibility(View.VISIBLE);
                break;
            }
        }

        int allEventsCount = helper.getAllEventsCount(all);
        int thisEventCount = helper.getTrackingEventsCount();

        int[] yData = {thisEventCount, allEventsCount};
        String[] xData = {collection.GetTracking(trackingId).GetTrackingName(), "Все отслеживания"};

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();
        entries.add(new PieEntry(yData[0], 0));
        entries.add(new PieEntry(yData[1], 1));

        xEntries.add(xData[0]);
        xEntries.add(xData[1]);
        createPieChart(diagramm, yData, xData);

        if (all.size() != 0) {

            allTrackings.setVisibility(View.VISIBLE);

            spinnerHint.setVisibility(View.INVISIBLE);

            final List<UUID> uuids = new ArrayList<>();
            final List<String> strings = new ArrayList<>();
            flags = new ArrayList<>();

            for (int i = 0; i < all.size(); i++) {
                if (!all.get(i).GetStatus()&&
                        !all.get(i).GetTrackingID().equals(UUID.fromString(getActivity().getIntent().getStringExtra("id")))) {
                    strings.add(all.get(i).GetTrackingName());
                    uuids.add(all.get(i).GetTrackingID());
                    flags.add(false);
                }
            }

            String allText = "";
            for (int i = 0; i < strings.size(); i++) {
                if (i != strings.size()) {
                    allText += strings.get(i) + ", ";
                }
            }

            final List<String> filteredTrackingsTitles = new ArrayList<>();
            final List<UUID> filteredTrackingsUuids = new ArrayList<>();

            allTrackings.setItems(strings, allText.substring(0, allText.length() - 2), new MultiSpinner.MultiSpinnerListener() {

                @Override
                public void onItemsSelected(boolean[] selected) {

                    for (int i = 0; i < selected.length; i++) {
                        Log.e("DIAGRAMM", selected[i] + "");
                        if (!selected[i]) {
                            uuids.set(i, null);
                        }
                    }

                    for (int i = 0; i < uuids.size(); i++) {
                        if (uuids.get(i) != null) {
                            filteredTrackingsUuids.add(uuids.get(i));
                        }
                    }

                    List<Tracking> trackingsForFilter = new ArrayList<>();
                    for (int i = 0; i < filteredTrackingsUuids.size(); i++) {
                        trackingsForFilter.add(collection.GetTracking(filteredTrackingsUuids.get(i)));
                    }

                    diagramm.getData().clearValues();

                    int allCount = helper.getAllEventsCount(trackingsForFilter);

                    int thisEventCount = helper.getTrackingEventsCount();

                    int[] yData = {thisEventCount, allCount};
                    String[] xData = {collection.GetTracking(trackingId).GetTrackingName(), "Выбранные отслеживания"};
                    createPieChart(diagramm, yData, xData);


                    List<Tracking> all = collection.GetTrackingCollection();
                    all.remove(collection.GetTrackingCollection().remove(
                            collection.GetTracking(trackingId)
                    ));


                    filteredTrackingsUuids.clear();

                    uuids.clear();

                    for (int i = 0; i < all.size(); i++) {
                        if (!all.get(i).GetStatus()) {
                            uuids.add(all.get(i).GetTrackingID());
                        }
                    }
                }
            });
        }
    }


    private void createPieChart(PieChart diagramm, int[] yData, String[] xData){
        if(diagramm.getData()!=null) {
            diagramm.clearValues();
            diagramm.clear();
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();
        entries.add(new PieEntry(yData[0], 0));
        entries.add(new PieEntry(yData[1], 1));

        xEntries.add(xData[0]);
        xEntries.add(xData[1]);

        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(getResources().getColor(R.color.colorPrimaryDark));

        PieDataSet dataSet = new PieDataSet(entries, "Фиксации");
        dataSet.setSliceSpace(4);
        dataSet.setValueTextSize(12);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        diagramm.setData(data);

        diagramm.setCenterText("Фиксации событий");
        diagramm.setCenterTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        diagramm.setHoleRadius(25f);
        diagramm.setCenterTextSize(10);
        diagramm.setTransparentCircleAlpha(0);
        diagramm.getDescription().setEnabled(false);
        diagramm.invalidate();
    }
}