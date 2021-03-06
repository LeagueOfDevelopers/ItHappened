package ru.lod_misis.ithappened.ui.recyclers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.ui.activities.EventDetailsActivity;
import ru.lod_misis.ithappened.ui.activities.EventsRefActivity;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.models.DayTimeFactModel;
import ru.lod_misis.ithappened.domain.statistics.facts.models.FrequentEventsFactModel;
import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustartionModel;
import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustrationType;
import ru.lod_misis.ithappened.domain.statistics.facts.models.WeekDaysFactModel;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    private List<Fact> factCollection;
    private Context context;

    private List<Integer> PASTEL_COLORS = new ArrayList<>();

    private static final int[] PASTEL_COLORS_FOR_PIE_CHART = {
            Color.rgb(64, 89, 128),
            Color.rgb(149, 165, 124),
            Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134),
            Color.rgb(179, 48, 80),
            Color.rgb(245, 124, 0),
            Color.rgb(84, 110, 122)
    };


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

        holder.factDescription.setText(Html.fromHtml(fact.textDescription()), TextView.BufferType.SPANNABLE);

        holder.pieChart.setVisibility(View.GONE);
        holder.lineChart.setVisibility(View.GONE);
        holder.barChart.setVisibility(View.GONE);
        holder.eventRef.setVisibility(View.GONE);

        createIllustration(fact,
                holder.pieChart,
                holder.barChart,
                holder.lineChart,
                holder.eventRef,
                holder.eventHistoryRef);

    }

    public void createIllustration(final Fact fact,
                                   PieChart pieChart,
                                   BarChart barChart,
                                   LineChart lineChart,
                                   TextView eventRef,
                                   TextView eventsHistoryRef) {


        if (fact.getIllustration() != null) {
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
                        PASTEL_COLORS = fact.getIllustration().getColors();
                        BarDataSet dataSet = new BarDataSet(entires, "Факт");
                        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
                        BarData data = new BarData(new ArrayList<String>(), dataSet);
                        barChart.setData(data);

                    }
                    List<FrequentEventsFactModel> dataList = fact.getIllustration().getFrequentEventsList();
                    if (dataList != null) {
                        PASTEL_COLORS = new ArrayList<>();
                        List<Double> frequentData = new ArrayList<>();
                        List<String> frequentTrackings = new ArrayList<>();
                        for (int i = 0; i < dataList.size(); i++) {
                            frequentData.add(dataList.get(i).getPeriod());
                            PASTEL_COLORS.add(dataList.get(i).getColor());

                            switch (dataList.size()) {
                                case 1:
                                    if (dataList.get(i).getTrackingName().length() >= 10) {
                                        frequentTrackings.add(dataList.get(i).getTrackingName().substring(0, 10) + "...");
                                    } else {
                                        frequentTrackings.add(dataList.get(i).getTrackingName());
                                    }
                                    break;
                                case 2:
                                    if (dataList.get(i).getTrackingName().length() >= 7) {
                                        frequentTrackings.add(dataList.get(i).getTrackingName().substring(0, 7) + "...");
                                    } else {
                                        frequentTrackings.add(dataList.get(i).getTrackingName());
                                    }
                                    break;
                                case 3:
                                    if (dataList.get(i).getTrackingName().length() >= 5) {
                                        frequentTrackings.add(dataList.get(i).getTrackingName().substring(0, 5) + "...");
                                    } else {
                                        frequentTrackings.add(dataList.get(i).getTrackingName());
                                    }
                                    break;
                                case 4:
                                    if (dataList.get(i).getTrackingName().length() >= 5) {
                                        frequentTrackings.add(dataList.get(i).getTrackingName().substring(0, 5) + "...");
                                    } else {
                                        frequentTrackings.add(dataList.get(i).getTrackingName());
                                    }
                                    break;
                            }

                        }
                        barChart.setVisibility(View.VISIBLE);
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        for (int i = 0; i < frequentData.size(); i++) {
                            if (frequentData.get(i) == 0.0) {
                                if (frequentData.size() == 3)
                                    frequentTrackings.set(i, "Мало событий");
                                if (frequentData.size() == 5)
                                    frequentTrackings.set(i, "Мало с...");
                                if (frequentData.size() == 2)
                                    frequentTrackings.set(i, "Мало событий");

                            }
                            entries.add(new BarEntry(frequentData.get(i).floatValue(), i));
                        }

                        BarDataSet dataSet = new BarDataSet(entries, "Отслеживания");
                        dataSet.setColors(PASTEL_COLORS);
                        BarData data = new BarData(frequentTrackings, dataSet);
                        barChart.setData(data);
                        barChart.setPinchZoom(false);
                        barChart.setDoubleTapToZoomEnabled(false);
                        barChart.setScaleEnabled(false);
                        barChart.setDescription("");

                    }
                    break;

                case EVENTSETREF:
                    final IllustartionModel illustartionSet = fact.getIllustration();
                    if(illustartionSet!=null) {
                        eventsHistoryRef.setVisibility(View.VISIBLE);
                        eventsHistoryRef.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, EventsRefActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("dateFrom", illustartionSet.getEventHistoryRef().get(0).getTime());
                                intent.putExtra("dateTo", illustartionSet.getEventHistoryRef().get(1).getTime());
                                context.startActivity(intent);
                            }
                        });
                    }else{
                        eventsHistoryRef.setVisibility(View.GONE);
                    }
                    break;


                case EVENTREF:
                    eventRef.setVisibility(View.VISIBLE);
                    eventRef.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            IllustartionModel illustration = fact.getIllustration();
                            EventV1 eventV1 = illustration.getEventV1Ref();
                            Intent intent = new Intent(context, EventDetailsActivity.class);
                            intent.putExtra("trackingId", eventV1.getTrackingId().toString());
                            intent.putExtra("eventId", eventV1.getEventId().toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                    break;
                case PIE:
                    IllustartionModel illustartionModel = fact.getIllustration();
                    if (illustartionModel != null) {
                        if (illustartionModel.getWeekDaysFactList() != null) {
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
                            PieDataSet pieDataSet = new PieDataSet(entries, null);
                            pieDataSet.setColors(PASTEL_COLORS_FOR_PIE_CHART);
                            pieDataSet.setValueTextSize(10);
                            PieData pieData = new PieData(weeksTitles, pieDataSet);
                            pieChart.setData(pieData);
                            pieChart.setDescription("");
                            pieChart.setRotationEnabled(false);
                            pieChart.setCenterText("Дни недели");
                        }


                        if (illustartionModel.getDayTimeFactList() != null) {
                            pieChart.setVisibility(View.VISIBLE);
                            List<DayTimeFactModel> data = illustartionModel.getDayTimeFactList();

                            List<String> weeksTitles = new ArrayList<>();
                            List<Double> percentage = new ArrayList<>();

                            for (DayTimeFactModel model : data) {
                                switch (model.getDayTime()) {
                                    case NIGHT:
                                        weeksTitles.add("Ночь");
                                        break;
                                    case MORNING:
                                        weeksTitles.add("Утро");
                                        break;
                                    case AFTERNOON:
                                        weeksTitles.add("День");
                                        break;
                                    case EVENING:
                                        weeksTitles.add("Вечер");
                                        break;
                                }

                                percentage.add(model.getPercetage());
                            }
                            List<Entry> entries = new ArrayList<>();
                            for (int i = 0; i < percentage.size(); i++) {
                                entries.add(new Entry(percentage.get(i).floatValue(), i));
                            }
                            PieDataSet pieDataSet = new PieDataSet(entries, null);
                            pieDataSet.setColors(PASTEL_COLORS_FOR_PIE_CHART);
                            pieDataSet.setValueTextSize(15);
                            PieData pieData = new PieData(weeksTitles, pieDataSet);
                            pieChart.setData(pieData);
                            pieChart.setCenterText("Время суток");
                            pieChart.setDescription("");
                            pieChart.setRotationEnabled(false);
                        }

                    }
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

        @BindView(R.id.textFactForTracking)
        TextView factDescription;
        @BindView(R.id.pieFact)
        PieChart pieChart;
        @BindView(R.id.graphFact)
        LineChart lineChart;
        @BindView(R.id.barFact)
        BarChart barChart;
        @BindView(R.id.eventRefBtn)
        TextView eventRef;
        @BindView(R.id.eventHistoryRefBtn)
        TextView eventHistoryRef;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
