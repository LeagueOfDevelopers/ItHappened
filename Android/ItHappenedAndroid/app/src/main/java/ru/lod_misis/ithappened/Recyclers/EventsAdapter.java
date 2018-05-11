package ru.lod_misis.ithappened.Recyclers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Activities.EventDetailsActivity;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> events;
    private List<Event> deletedEvent;
    private Context context;
    private int state = 0;

    public EventsAdapter(List<Event> events, Context context, int state) {
        this.events = events;
        deletedEvent = new ArrayList<>();
        for(Event event : deletedEvent){
            if(event.GetStatus())
                deletedEvent.add(event);
        }
        if(events!=null)
        events.removeAll(deletedEvent);
        this.context = context;
        this.state = state;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(ru.lod_misis.ithappened.R.layout.event_item, parent, false);
        return new EventsAdapter.ViewHolder(v);

    }

    public void refreshData(List<Event> events){

        this.events.clear();
        for(Event event : events){
            if(!event.GetStatus()){
                this.events.add(event);
            }
        }
        notifyDataSetChanged();

    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Event event = events.get(position);

        ITrackingRepository trackingRepository;

        SharedPreferences sharedPreferences = context.getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("LastId","").isEmpty()) {
            trackingRepository = new StaticInMemoryRepository(context,
                    sharedPreferences.getString("UserId", "")).getInstance();
        }else{
            trackingRepository = new StaticInMemoryRepository(context,
                    sharedPreferences.getString("LastId", "")).getInstance();
        }


        UUID trackingId = event.GetTrackingId();

        if(event.GetComment()!=null && state==0){
            holder.trackingTitle.setText(event.GetComment());
        }else {
            holder.trackingTitle.setText(trackingRepository.GetTracking(trackingId).GetTrackingName());
        }

        if(event.GetScale()!=null && trackingRepository.GetTracking(trackingId).getScaleName()!=null ){
            String type = trackingRepository.GetTracking(trackingId).getScaleName();
            if(type!=null) {
                holder.scaleValue.setVisibility(View.VISIBLE);
                if (type.length() >= 10 && event.GetScale()>1000000 && event.GetRating()!=null) {
                    holder.scaleValue.setText(StringParse.parseDouble(event.GetScale().doubleValue())+" "+type.substring(0, 3) + ".");
                } else {
                    holder.scaleValue.setText(StringParse.parseDouble(event.GetScale().doubleValue())+" "+type);
                }
            }
        }else{
            holder.scaleValue.setVisibility(View.GONE);
        }

        if(event.GetRating()!=null ){
            DecimalFormat format = new DecimalFormat("#.#");
            holder.ratingValue.setText(format.format(event.GetRating().getRating()/2.0f)+"");
            holder.starIcon.setVisibility(View.VISIBLE);
            holder.ratingValue.setVisibility(View.VISIBLE);
        }else{
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            holder.scaleValue.setPadding(holder.trackingTitle.getPaddingLeft(),holder.eventDate.getTotalPaddingTop(),7,7);
            holder.ratingValue.setVisibility(View.GONE);
            holder.starIcon.setVisibility(View.GONE);
        }

        holder.itemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("trackingId", event.GetTrackingId().toString());
                String eventId = event.GetEventId().toString();
                intent.putExtra("eventId", event.GetEventId().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        /*holder.editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditEventActivity.class);
                intent.putExtra("trackingId", event.GetTrackingId().toString());
                String eventId = event.GetEventId().toString();
                intent.putExtra("eventId", event.GetEventId().toString());
                context.startActivity(intent);
            }
        });*/

        /*holder.deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteEventFromFragmentDiaolog delete = new DeleteEventFromFragmentDiaolog();
                Bundle bundle = new Bundle();
                bundle.putString("trackingId", event.GetTrackingId().toString());
                bundle.putString("eventId" , event.GetEventId().toString());
                delete.setArguments(bundle);
                delete.show(((Activity) context).getFragmentManager(), "DeleteEvent");
            }
        });*/

        Date eventDate = event.GetEventDate();

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());
        holder.eventDate.setText(format.format(eventDate));

    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trackingTitle;
        TextView eventDate;
        RelativeLayout itemLL;

        TextView scaleValue;
        TextView ratingValue;
        ImageView starIcon;

        ImageView deleteEvent;
        ImageView editEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            /*deleteEvent = (ImageView) itemView.findViewById(ru.lod_misis.ithappened.R.id.deleteEventIcn);
            editEvent = (ImageView) itemView.findViewById(ru.lod_misis.ithappened.R.id.editEventIcn);  */
            scaleValue = (TextView) itemView.findViewById(R.id.scaleValue);
            ratingValue = (TextView) itemView.findViewById(R.id.ratingValue);
            starIcon = (ImageView) itemView.findViewById(R.id.starIcon);
            eventDate = (TextView) itemView.findViewById(ru.lod_misis.ithappened.R.id.eventDate);
            trackingTitle = (TextView) itemView.findViewById(ru.lod_misis.ithappened.R.id.TrackingTitle);
            itemLL = (RelativeLayout) itemView.findViewById(ru.lod_misis.ithappened.R.id.eventRL);

        }
    }
}
