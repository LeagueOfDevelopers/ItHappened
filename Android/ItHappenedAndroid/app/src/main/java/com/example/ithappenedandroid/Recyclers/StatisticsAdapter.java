package com.example.ithappenedandroid.Recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;
import com.github.mikephil.charting.charts.LineChart;

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

        public ViewHolder(View itemView) {
            super(itemView);
            scaleChart = (LineChart) itemView.findViewById(R.id.graphForScale);
            trackingTitle = (TextView) itemView.findViewById(R.id.TrackingTitleForStatistics);
            commentCount = (TextView) itemView.findViewById(R.id.countOfComments);
            avrgRating = (TextView) itemView.findViewById(R.id.avrgRating);
        }
    }

}
