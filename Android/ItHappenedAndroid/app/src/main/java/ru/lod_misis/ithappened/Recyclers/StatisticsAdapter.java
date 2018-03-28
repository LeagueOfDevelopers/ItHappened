package ru.lod_misis.ithappened.Recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Activities.EventDetailsActivity;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.FrequentEventsFactModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    private List<Fact> factCollection;
    private Context context;


    public StatisticsAdapter(List<Fact> factCollection, Context context) {
        this.factCollection = factCollection;
        this.context = context;
    }

    @Override
    public StatisticsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistics_item, parent, false);
        return new StatisticsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StatisticsAdapter.ViewHolder holder, final int position) {

        final Fact fact = factCollection.get(position);

        holder.factDescription.setText(fact.textDescription());

        holder.pieChart.setVisibility(View.GONE);
        holder.lineChart.setVisibility(View.GONE);
        holder.barChart.setVisibility(View.GONE);
        holder.eventRef.setVisibility(View.GONE);

        createIllustration(fact, holder.pieChart, holder.barChart,holder.lineChart, holder.eventRef);



    }

    public void createIllustration(final Fact fact, PieChart pieChart, BarChart barChart, LineChart lineChart, TextView eventRef){


        if(fact.getIllustration()!=null) {
            IllustrationType type = fact.getIllustration().getType();
            switch (type) {
                case BAR:
                    List<Double> barData = fact.getIllustration().getBarData();
                    if (barData != null) {
                        barChart.setVisibility(View.VISIBLE);
                        ArrayList<BarEntry> entires = new ArrayList<>();
                        for (int i = 0; i < barData.size(); i++) {
                            entires.add(new BarEntry(barData.get(i).floatValue(), i));
                        }

                        BarDataSet dataSet = new BarDataSet(entires, "Факт");
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                       BarData data = new BarData(new ArrayList<String>(),dataSet);
                       barChart.setData(data);

                    }
                    List<FrequentEventsFactModel> dataList = fact.getIllustration().getFrequentEventsList();
                    if(dataList!=null){
                        List<Double> frequentData = new ArrayList<>();
                        List<String> frequentTrackings = new ArrayList<>();
                        for(int i=0;i<dataList.size();i++){
                            frequentData.add(dataList.get(i).getPeriod());
                            frequentTrackings.add(dataList.get(i).getTrackingName());
                        }
                        barChart.setVisibility(View.VISIBLE);
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        for (int i = 0; i < frequentData.size(); i++) {
                            entries.add(new BarEntry(frequentData.get(i).floatValue(), i));
                        }

                        BarDataSet dataSet = new BarDataSet(entries, "Отслеживания");
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        BarData data = new BarData(frequentTrackings, dataSet);
                        barChart.setData(data);
                        barChart.setDescription("");

                    }
                    break;

                case EVENTREF:
                    eventRef.setVisibility(View.VISIBLE);
                    eventRef.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            IllustartionModel illustration = fact.getIllustration();
                            Event event = illustration.getEventRef();
                            Intent intent = new Intent(context, EventDetailsActivity.class);
                            intent.putExtra("trackingId", event.GetTrackingId().toString());
                            intent.putExtra("eventId", event.GetEventId().toString());
                            context.startActivity(intent);
                        }
                    });
                    break;
                case PIE:
                    break;
                case GRAPH:
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return factCollection.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView factDescription;
        PieChart pieChart;
        LineChart lineChart;
        BarChart barChart;
        TextView eventRef;

        public ViewHolder(View itemView) {
            super(itemView);
            factDescription = (TextView) itemView.findViewById(R.id.textFactForTracking);
            lineChart = (LineChart) itemView.findViewById(R.id.graphFact);
            pieChart = (PieChart) itemView.findViewById(R.id.pieFact);
            barChart = (BarChart) itemView.findViewById(R.id.barFact);
            eventRef = (TextView) itemView.findViewById(R.id.eventRefBtn);
        }
    }


}
