package ru.lod_misis.ithappened.Recyclers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
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
import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<NewEvent> newEvents;
    private List<NewEvent> deletedNewEvent;
    private Context context;
    private int state = 0;

    public EventsAdapter(List<NewEvent> newEvents, Context context, int state) {
        this.newEvents = newEvents;
        deletedNewEvent = new ArrayList<>();
        for(NewEvent newEvent : deletedNewEvent){
            if(newEvent.GetStatus())
                deletedNewEvent.add(newEvent);
        }
        if(newEvents !=null)
        newEvents.removeAll(deletedNewEvent);
        this.context = context;
        this.state = state;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(ru.lod_misis.ithappened.R.layout.event_item, parent, false);
        return new EventsAdapter.ViewHolder(v);

    }

    public void refreshData(List<NewEvent> newEvents){

        this.newEvents.clear();
        for(NewEvent newEvent : newEvents){
            if(!newEvent.GetStatus()){
                this.newEvents.add(newEvent);
            }
        }
        notifyDataSetChanged();

    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final NewEvent newEvent = newEvents.get(position);

        ITrackingRepository trackingRepository;

        SharedPreferences sharedPreferences = context.getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("LastId","").isEmpty()) {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
            trackingRepository = StaticInMemoryRepository.getInstance();
        }else{
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
            trackingRepository = StaticInMemoryRepository.getInstance();
        }


        UUID trackingId = newEvent.GetTrackingId();

        holder.trackingColor.setCardBackgroundColor(Integer.parseInt(trackingRepository.GetTracking(trackingId).getColor()));

        if(newEvent.GetComment()!=null && state==0){
            holder.trackingTitle.setText(newEvent.GetComment());
        }else {
            holder.trackingTitle.setText(trackingRepository.GetTracking(trackingId).GetTrackingName());
        }

        if(newEvent.GetScale()!=null && trackingRepository.GetTracking(trackingId).getScaleName()!=null ){
            String type = trackingRepository.GetTracking(trackingId).getScaleName();
            if(type!=null) {
                holder.scaleValue.setVisibility(View.VISIBLE);
                if (type.length() >= 10 && newEvent.GetScale()>1000000 && newEvent.GetRating()!=null) {
                    holder.scaleValue.setText(StringParse.parseDouble(newEvent.GetScale().doubleValue())+" "+type.substring(0, 3) + ".");
                } else {
                    holder.scaleValue.setText(StringParse.parseDouble(newEvent.GetScale().doubleValue())+" "+type);
                }
            }
        }else{
            holder.scaleValue.setVisibility(View.GONE);
        }

        if(newEvent.GetRating()!=null ){
            DecimalFormat format = new DecimalFormat("#.#");
            holder.ratingValue.setText(format.format(newEvent.GetRating().getRating()/2.0f)+"");
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
                intent.putExtra("trackingId", newEvent.GetTrackingId().toString());
                String eventId = newEvent.GetEventId().toString();
                intent.putExtra("eventId", newEvent.GetEventId().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        /*holder.editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditEventActivity.class);
                intent.putExtra("trackingId", newEvent.GetTrackingId().toString());
                String eventId = newEvent.GetEventId().toString();
                intent.putExtra("eventId", newEvent.GetEventId().toString());
                context.startActivity(intent);
            }
        });*/

        /*holder.deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteEventFromFragmentDiaolog delete = new DeleteEventFromFragmentDiaolog();
                Bundle bundle = new Bundle();
                bundle.putString("trackingId", newEvent.GetTrackingId().toString());
                bundle.putString("eventId" , newEvent.GetEventId().toString());
                delete.setArguments(bundle);
                delete.show(((Activity) context).getFragmentManager(), "DeleteEvent");
            }
        });*/

        Date eventDate = newEvent.GetEventDate();

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());
        holder.eventDate.setText(format.format(eventDate));

    }

    public List<NewEvent> getNewEvents() {
        return newEvents;
    }

    @Override
    public int getItemCount() {
        return newEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trackingTitle;
        TextView eventDate;
        RelativeLayout itemLL;

        TextView scaleValue;
        TextView ratingValue;
        ImageView starIcon;

        CardView trackingColor;

        ImageView deleteEvent;
        ImageView editEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            /*deleteEvent = (ImageView) itemView.findViewById(ru.lod_misis.ithappened.R.id.deleteEventIcn);
            editEvent = (ImageView) itemView.findViewById(ru.lod_misis.ithappened.R.id.editEventIcn);  */
            scaleValue = (TextView) itemView.findViewById(R.id.scaleValue);
            ratingValue = (TextView) itemView.findViewById(R.id.ratingValue);
            starIcon = (ImageView) itemView.findViewById(R.id.starIcon);
            trackingColor = itemView.findViewById(R.id.trackingColorEvent);
            eventDate = (TextView) itemView.findViewById(ru.lod_misis.ithappened.R.id.eventDate);
            trackingTitle = (TextView) itemView.findViewById(ru.lod_misis.ithappened.R.id.TrackingTitle);
            itemLL = (RelativeLayout) itemView.findViewById(ru.lod_misis.ithappened.R.id.eventRL);

        }
    }
}
