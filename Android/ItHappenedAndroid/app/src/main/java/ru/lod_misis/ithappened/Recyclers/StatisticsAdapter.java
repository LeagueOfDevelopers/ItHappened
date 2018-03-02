package ru.lod_misis.ithappened.Recyclers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.lod_misis.ithappened.Activities.TrackingStatisticsActivity;
import ru.lod_misis.ithappened.Domain.Tracking;

import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    private List<Tracking> trackings;
    private Context context;
    FragmentManager fManage;
    FragmentTransaction fTrans;


    public StatisticsAdapter(List<Tracking> trackings, Context context) {
        this.trackings = trackings;
        this.context = context;
    }

    @Override
    public StatisticsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(ru.lod_misis.ithappened.R.layout.recycler_item, parent, false);
        return new StatisticsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StatisticsAdapter.ViewHolder holder, final int position) {

        final Tracking tracking = trackings.get(position);
        holder.trackingTitle.setText(tracking.GetTrackingName());

        holder.itemLL.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                final Tracking tracking = trackings.get(position);
                String id = tracking.GetTrackingID().toString();
                Intent intent = new Intent(context, TrackingStatisticsActivity.class);
                String trackId = tracking.GetTrackingID().toString();
                intent.putExtra("id", trackId);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return trackings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trackingTitle;
        CardView itemLL;

        public ViewHolder(View itemView) {
            super(itemView);
            trackingTitle = (TextView) itemView.findViewById(ru.lod_misis.ithappened.R.id.TracingTitle);
            itemLL = (CardView) itemView.findViewById(ru.lod_misis.ithappened.R.id.itemLL);
        }
    }


}
