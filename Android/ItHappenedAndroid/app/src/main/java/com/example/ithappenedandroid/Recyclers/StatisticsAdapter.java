package com.example.ithappenedandroid.Recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    List<Tracking> trackings;
    Context context;

    ITrackingRepository trackingCollection = StaticInMemoryRepository.getInstance();
    TrackingService trackingService = new TrackingService("textUser", trackingCollection);


    public StatisticsAdapter(List<Tracking> trackings, Context context) {
        this.trackings = trackings;
        this.context = context;
    }

    @Override
    public StatisticsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistics_item, parent, false);
        return new StatisticsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final Tracking tracking = trackings.get(position);
        List<Event> events = tracking.GetEventCollection();

        int commentCount = 0;
        double avrgRatingNumber = 0;
        int ratingCount = 0;

        viewHolder.trackingTitle.setText(tracking.GetTrackingName());

        if(tracking.GetCommentCustomization() == TrackingCustomization.None){
            viewHolder.commentCount.setText("У этого отслеживания нет комментариев");
        }else{
            for(int i = 0;i<events.size();i++){
                if(events.get(i).GetComment()!=null){
                    commentCount++;
                }
            }
            viewHolder.commentCount.setText(""+commentCount);
        }


        if(tracking.GetRatingCustomization()==TrackingCustomization.None){
            viewHolder.avrgRating.setText("У этого отслеживания нет оценок");
        }else {
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).GetRating() != null) {
                    ratingCount++;
                    avrgRatingNumber += events.get(i).GetRating().GetRatingValue();
                }
            }
            if (ratingCount == 0) {
                viewHolder.avrgRating.setText("0");
            } else {
                avrgRatingNumber/=ratingCount;
                viewHolder.avrgRating.setText(""+avrgRatingNumber);
            }
        }


        List<Entry> entries = new ArrayList<Entry>();
        int count = 1;

        if(tracking.GetScaleCustomization()!=TrackingCustomization.None) {
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).GetScale() != null) {
                    Entry entry = new Entry(count, (float) events.get(i).GetScale().doubleValue());
                    count++;
                    entries.add(entry);
                }
            }
            LineDataSet data = new LineDataSet(entries, "Шкала");
            data.setFillAlpha(110);
            LineData lineData = new LineData(data);

            LineChart chart = viewHolder.scaleChart;
            chart.setData(lineData);
            chart.invalidate();

        }
        count=0;

    }

    @Override
    public int getItemCount() {
        return trackings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trackingTitle;
        LineChart scaleChart;
        TextView commentCount;
        TextView avrgRating;
        FrameLayout graphLL;

        public ViewHolder(View itemView) {
            super(itemView);
            scaleChart = (LineChart) itemView.findViewById(R.id.graphForScale);
            trackingTitle = (TextView) itemView.findViewById(R.id.TrackingTitleForStatistics);
            commentCount = (TextView) itemView.findViewById(R.id.countOfComments);
            avrgRating = (TextView) itemView.findViewById(R.id.avrgRating);
            graphLL = (FrameLayout) itemView.findViewById(R.id.graphLL);
            scaleChart.setNoDataText("У этого отслеживания нет шкалы");
            scaleChart.setNoDataTextColor(R.color.colorPrimaryDark);
        }
    }

}
