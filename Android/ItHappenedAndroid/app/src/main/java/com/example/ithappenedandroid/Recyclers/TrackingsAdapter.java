package com.example.ithappenedandroid.Recyclers;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Fragments.EventsForTrackingFragment;
import com.example.ithappenedandroid.R;

import java.util.List;

public class TrackingsAdapter extends RecyclerView.Adapter<TrackingsAdapter.ViewHolder> {

    private List<Tracking> trackings;
    private Context context;
    FragmentManager fManage;
    FragmentTransaction fTrans;
    EventsForTrackingFragment eventsForTrackFrg;


    public TrackingsAdapter(List<Tracking> trackings, Context context) {
        this.trackings = trackings;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Tracking tracking = trackings.get(position);
        holder.trackingTitle.setText(tracking.GetTrackingName());

        holder.itemLL.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                eventsForTrackFrg = new EventsForTrackingFragment();
                final Tracking tracking = trackings.get(position);
                String id = tracking.GetTrackingID().toString();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                eventsForTrackFrg.setArguments(bundle);
                FragmentManager manager = ((Activity) context).getFragmentManager();
                fTrans = manager.beginTransaction();
                fTrans.replace(R.id.trackingsFrg, eventsForTrackFrg);
                fTrans.commit();

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
            trackingTitle = (TextView) itemView.findViewById(R.id.TracingTitle);
            itemLL = (CardView) itemView.findViewById(R.id.itemLL);
        }
    }

}
