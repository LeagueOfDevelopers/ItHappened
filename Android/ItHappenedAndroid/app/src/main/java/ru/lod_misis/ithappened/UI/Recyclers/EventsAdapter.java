package ru.lod_misis.ithappened.UI.Recyclers;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.Data.Repositories.ITrackingRepository;
import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.StringParse;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.UI.Activities.EventDetailsActivity;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    ITrackingRepository trackingRepository;
    //TODO переписать
    private List<EventV1> eventV1s;
    private List<EventV1> deletedEventV1;
    private Context context;
    private int state = 0;

    public EventsAdapter (List<EventV1> eventV1s , Context context , int state , ITrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
        this.eventV1s = eventV1s;
        deletedEventV1 = new ArrayList<>();
        for (EventV1 eventV1 : deletedEventV1) {
            if ( eventV1.GetStatus() )
                deletedEventV1.add(eventV1);
        }
        if ( eventV1s != null )
            eventV1s.removeAll(deletedEventV1);
        this.context = context;
        this.state = state;
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent , int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(ru.lod_misis.ithappened.R.layout.event_item , parent , false);
        return new EventsAdapter.ViewHolder(v);

    }

    public void refreshData (List<EventV1> eventV1s) {

        this.eventV1s.clear();
        for (EventV1 eventV1 : eventV1s) {
            if ( !eventV1.GetStatus() ) {
                this.eventV1s.add(eventV1);
            }
        }
        notifyDataSetChanged();

    }

    public Context getContext () {
        return context;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder , int position) {

        final EventV1 eventV1 = eventV1s.get(position);

        SharedPreferences sharedPreferences = context.getSharedPreferences("MAIN_KEYS" , Context.MODE_PRIVATE);

        UUID trackingId = eventV1.GetTrackingId();

        holder.trackingColor.setCardBackgroundColor(Integer.parseInt(trackingRepository.GetTracking(trackingId).getColor()));

        if ( eventV1.GetComment() != null && state == 0 ) {
            holder.trackingTitle.setText(eventV1.GetComment());
        } else {
            holder.trackingTitle.setText(trackingRepository.GetTracking(trackingId).GetTrackingName());
        }

        if ( eventV1.GetScale() != null && trackingRepository.GetTracking(trackingId).getScaleName() != null ) {
            String type = trackingRepository.GetTracking(trackingId).getScaleName();
            if ( type != null ) {
                holder.scaleValue.setVisibility(View.VISIBLE);
                if ( type.length() >= 10 && eventV1.GetScale() > 1000000 && eventV1.GetRating() != null ) {
                    holder.scaleValue.setText(StringParse.parseDouble(eventV1.GetScale().doubleValue()) + " " + type.substring(0 , 3) + ".");
                } else {
                    holder.scaleValue.setText(StringParse.parseDouble(eventV1.GetScale().doubleValue()) + " " + type);
                }
            }
        } else {
            holder.scaleValue.setVisibility(View.GONE);
        }

        if ( eventV1.GetRating() != null ) {
            DecimalFormat format = new DecimalFormat("#.#");
            holder.ratingValue.setText(format.format(eventV1.GetRating().getRating() / 2.0f) + "");
            holder.starIcon.setVisibility(View.VISIBLE);
            holder.ratingValue.setVisibility(View.VISIBLE);
        } else {
            if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP )
                holder.scaleValue.setPadding(holder.trackingTitle.getPaddingLeft() , holder.eventDate.getTotalPaddingTop() , 7 , 7);
            holder.ratingValue.setVisibility(View.GONE);
            holder.starIcon.setVisibility(View.GONE);
        }

        holder.itemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(context , EventDetailsActivity.class);
                intent.putExtra("trackingId" , eventV1.GetTrackingId().toString());
                intent.putExtra("eventId" , eventV1.GetEventId().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        Date eventDate = eventV1.GetEventDate();

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm" , loc);
        format.setTimeZone(TimeZone.getDefault());
        holder.eventDate.setText(format.format(eventDate));


    }

    public List<EventV1> getEventV1s () {
        return eventV1s;
    }

    @Override
    public int getItemCount () {
        return eventV1s.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.TrackingTitle)
        TextView trackingTitle;
        @BindView(R.id.eventDate)
        TextView eventDate;
        @BindView(R.id.eventRL)
        RelativeLayout itemLL;

        @BindView(R.id.scaleValue)
        TextView scaleValue;
        @BindView(R.id.ratingValue)
        TextView ratingValue;
        @BindView(R.id.starIcon)
        ImageView starIcon;
        @BindView(R.id.trackingColorEvent)
        CardView trackingColor;

        ImageView deleteEvent;
        ImageView editEvent;

        public ViewHolder (View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);

        }
    }
}
