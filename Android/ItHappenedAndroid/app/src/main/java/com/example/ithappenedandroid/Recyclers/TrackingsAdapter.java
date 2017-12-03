package com.example.ithappenedandroid.Recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ithappenedandroid.Activities.TrackingActivity;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Tracking;

import org.w3c.dom.Text;

import java.util.List;

public class TrackingsAdapter extends RecyclerView.Adapter<TrackingsAdapter.ViewHolder> {

    private List<Tracking> trackings;
    private Context context;


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
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Tracking tracking = trackings.get(position);

        holder.trackingTitle.setText(tracking.getTitle());

    }

    @Override
    public int getItemCount() {
        return trackings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trackingTitle;
        LinearLayout itemLL;

        public ViewHolder(View itemView) {
            super(itemView);
            trackingTitle = (TextView) itemView.findViewById(R.id.TracingTitle);
            itemLL = (LinearLayout) itemView.findViewById(R.id.itemLL);
        }
    }

}
