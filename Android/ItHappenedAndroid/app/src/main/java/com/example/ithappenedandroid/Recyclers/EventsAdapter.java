package com.example.ithappenedandroid.Recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ithappenedandroid.Activities.EventDetailsActivity;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> events;
    private Context context;
    private int state = 0;

    public EventsAdapter(List<Event> events, Context context, int state) {
        this.events = events;
        this.context = context;
        this.state = state;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventsAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Event event = events.get(position);

        ITrackingRepository trackingRepository = StaticInMemoryRepository.getInstance();


        UUID trackingId = event.GetTrackingId();

        holder.trackingTitle.setText(trackingRepository.GetTracking(trackingId).GetTrackingName());

        holder.itemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("trackingId", event.GetTrackingId().toString());
                intent.putExtra("eventId", event.GetEventId().toString());
                context.startActivity(intent);
            }
        });

        Date eventDate = event.GetEventDate();

        holder.eventDate.setText(eventDate.toLocaleString());

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trackingTitle;
        TextView eventDate;
        RelativeLayout itemLL;

        public ViewHolder(View itemView) {
            super(itemView);
            eventDate = (TextView) itemView.findViewById(R.id.eventDate);
            trackingTitle = (TextView) itemView.findViewById(R.id.TrackingTitle);
            itemLL = (RelativeLayout) itemView.findViewById(R.id.eventRL);

        }
    }
}
