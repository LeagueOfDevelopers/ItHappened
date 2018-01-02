package com.example.ithappenedandroid.Recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.R;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    List<Tracking> trackings;
    List<Tracking> trackingsWithScale;
    List<Tracking> trackingsWithRating;
    Context context;


    public StatisticsAdapter(List<Tracking> trackings, Context context) {
        this.trackings = trackings;
        for(Tracking tracking : trackings){
            if(tracking.GetRatingCustomization()== TrackingCustomization.Required || tracking.GetRatingCustomization()== TrackingCustomization.Optional){
                trackingsWithRating.add(tracking);
            }
            if(tracking.GetScaleCustomization()== TrackingCustomization.Required || tracking.GetScaleCustomization()== TrackingCustomization.Optional){
                trackingsWithScale.add(tracking);
            }
        }
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

    }

    @Override
    public int getItemCount() {
        return trackings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trackingTitle;
        LineChart scaleChart;

        public ViewHolder(View itemView) {
            super(itemView);
            scaleChart = (LineChart) itemView.findViewById(R.id.graphForScale);
            trackingTitle = (TextView) itemView.findViewById(R.id.TracingTitle);
        }
    }

}
