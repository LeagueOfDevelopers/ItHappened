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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
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
import ru.lod_misis.ithappened.Statistics.Facts.Models.WeekDaysFactModel;

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
                            if(frequentData.get(i)==0.0){
                                frequentTrackings.set(i,frequentData.get(i)+" (слишком мало событий)");
                            }
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
                    IllustartionModel illustartionModel = fact.getIllustration();
                    if(illustartionModel!=null) {
                        pieChart.setVisibility(View.VISIBLE);
                        List<WeekDaysFactModel> data = illustartionModel.getWeekDaysFactList();

                        List<String> weeksTitles = new ArrayList<>();
                        List<Double> percentage = new ArrayList<>();

                        for (WeekDaysFactModel model : data) {
                            switch (model.getWeekDay()) {
                                case MONDAY:
                                    weeksTitles.add("Понедельник");
                                    break;
                                case TUESDAY:
                                    weeksTitles.add("Вторник");
                                    break;
                                case WEDNESDAY:
                                    weeksTitles.add("Среда");
                                    break;
                                case THURSDAY:
                                    weeksTitles.add("Четверг");
                                    break;
                                case FRIDAY:
                                    weeksTitles.add("Пятница");
                                    break;
                                case SATURDAY:
                                    weeksTitles.add("Суббота");
                                    break;
                                case SUNDAY:
                                    weeksTitles.add("Воскресенье");
                                    break;
                                default:
                                    break;
                            }


                            percentage.add(model.getPercetage());
                        }
                        List<Entry> entries = new ArrayList<>();
                        for (int i = 0; i < percentage.size(); i++) {
                            entries.add(new Entry(percentage.get(i).floatValue(), i));
                        }
                        PieDataSet pieDataSet = new PieDataSet(entries, "Дни недели");
                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        PieData pieData = new PieData(weeksTitles, pieDataSet);
                        pieChart.setData(pieData);
                        pieChart.setDescription("Дни недели");
                    }
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
