package ru.lod_misis.ithappened.Recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import ru.lod_misis.ithappened.Activities.EventDetailsActivity;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;

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
                .inflate(ru.lod_misis.ithappened.R.layout.event_item, parent, false);
        return new EventsAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Event event = events.get(position);

        StaticInMemoryRepository repository = new StaticInMemoryRepository(context, context.getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", ""));

        ITrackingRepository trackingRepository = repository.getInstance();


        UUID trackingId = event.GetTrackingId();

        if(event.GetComment()!=null){
            holder.trackingTitle.setText(event.GetComment());
        }else {
            holder.trackingTitle.setText(trackingRepository.GetTracking(trackingId).GetTrackingName());
        }

        if(event.GetScale()!=null && trackingRepository.GetTracking(trackingId).getScaleName()!=null){
            holder.scaleValue.setText(event.GetScale().toString());
            String type = trackingRepository.GetTracking(trackingId).getScaleName();
            if(type!=null) {
                if (type.length() >= 3) {
                    holder.scaleType.setText(type.substring(0, 2) + ".");
                } else {
                    holder.scaleType.setText(type);
                }
            }
        }else{
            holder.scaleValue.setVisibility(View.GONE);
            holder.scaleType.setVisibility(View.GONE);
        }

        if(event.GetRating()!=null){
            holder.ratingValue.setText(Math.round(event.GetRating().GetRatingValue()/2)+"");
        }else{
            holder.starIcon.setVisibility(View.GONE);
            holder.ratingValue.setVisibility(View.GONE);
            holder.scaleValue.setPadding(holder.trackingTitle.getPaddingLeft(),holder.eventDate.getTotalPaddingTop(),7,7);
        }

        holder.itemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("trackingId", event.GetTrackingId().toString());
                String eventId = event.GetEventId().toString();
                intent.putExtra("eventId", event.GetEventId().toString());
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
        TextView scaleType;
        ImageView starIcon;

        /*ImageView deleteEvent;
        ImageView editEvent;*/

        public ViewHolder(View itemView) {
            super(itemView);
            /*deleteEvent = (ImageView) itemView.findViewById(ru.lod_misis.ithappened.R.id.deleteEventIcn);
            editEvent = (ImageView) itemView.findViewById(ru.lod_misis.ithappened.R.id.editEventIcn);*/
            scaleValue = (TextView) itemView.findViewById(R.id.scaleValue);
            ratingValue = (TextView) itemView.findViewById(R.id.ratingValue);
            scaleType = (TextView) itemView.findViewById(R.id.scaleTypeAdapt);
            starIcon = (ImageView) itemView.findViewById(R.id.starIcon);
            eventDate = (TextView) itemView.findViewById(ru.lod_misis.ithappened.R.id.eventDate);
            trackingTitle = (TextView) itemView.findViewById(ru.lod_misis.ithappened.R.id.TrackingTitle);
            itemLL = (RelativeLayout) itemView.findViewById(ru.lod_misis.ithappened.R.id.eventRL);

        }
    }
}
