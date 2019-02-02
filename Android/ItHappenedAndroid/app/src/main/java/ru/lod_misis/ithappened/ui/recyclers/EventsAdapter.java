package ru.lod_misis.ithappened.ui.recyclers;

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
import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractor;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractorImpl;
import ru.lod_misis.ithappened.domain.statistics.facts.StringParse;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.ui.activities.EventDetailsActivity;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    public static final int TYPE_WITHOUT_PHOTO = 2;
    public static final int TYPE_WITH_PHOTO = 1;

    TrackingDataSource trackingRepository;
    //TODO переписать
    private List<EventV1> eventV1s;
    private List<EventV1> deletedEventV1;
    private Context context;
    private int state = 0;

    public EventsAdapter(List<EventV1> eventV1s, Context context, int state, TrackingDataSource trackingRepository) {
        this.trackingRepository = trackingRepository;
        this.eventV1s = eventV1s;
        deletedEventV1 = new ArrayList<>();
        for (EventV1 eventV1 : deletedEventV1) {
            if (eventV1.isDeleted())
                deletedEventV1.add(eventV1);
        }
        if (eventV1s != null)
            eventV1s.removeAll(deletedEventV1);
        this.context = context;
        this.state = state;
    }

    @Override
    public int getItemViewType(int position) {
        if (eventV1s.get(position).getPhoto() == null || eventV1s.get(position).getPhoto().equals("")) {
            return TYPE_WITHOUT_PHOTO;
        } else {
            return TYPE_WITH_PHOTO;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_WITH_PHOTO) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(ru.lod_misis.ithappened.R.layout.event_item, parent, false);
            return new ViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(ru.lod_misis.ithappened.R.layout.event_item2, parent, false);
            return new ViewHolder(v);
        }
    }

    public void refreshData(List<EventV1> eventV1s) {

        this.eventV1s.clear();
        for (EventV1 eventV1 : eventV1s) {
            if (!eventV1.isDeleted()) {
                this.eventV1s.add(eventV1);
            }
        }
        notifyDataSetChanged();

    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final EventV1 eventV1 = eventV1s.get(position);

        SharedPreferences sharedPreferences = context.getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        UUID trackingId = eventV1.getTrackingId();

        // holder.trackingColor.setCardBackgroundColor(Integer.parseInt(trackingRepository.getTracking(trackingId).getColor()));

        if (eventV1.getPhoto() == null || eventV1.getPhoto().equals("")) {
            holder.trackingPhoto.setVisibility(View.GONE);
        } else {
            PhotoInteractor workWithFIles = new PhotoInteractorImpl(context);
            holder.trackingPhoto.setImageBitmap(workWithFIles.loadImage(eventV1.getPhoto()));
        }

        if (eventV1.getComment() != null && state == 0) {
            holder.trackingTitle.setText(eventV1.getComment());
        } else {
            holder.trackingTitle.setText(trackingRepository.getTracking(trackingId).getTrackingName());
        }

        if (eventV1.getScale() != null && trackingRepository.getTracking(trackingId).getScaleName() != null) {
            String type = trackingRepository.getTracking(trackingId).getScaleName();
            if (type != null) {
                holder.scaleValue.setVisibility(View.VISIBLE);
                if (type.length() >= 10 && eventV1.getScale() > 1000000 && eventV1.getRating() != null) {
                    holder.scaleValue.setText(StringParse.parseDouble(eventV1.getScale().doubleValue()) + " " + type.substring(0, 3) + ".");
                } else {
                    holder.scaleValue.setText(StringParse.parseDouble(eventV1.getScale().doubleValue()) + " " + type);
                }
            }
        } else {
            holder.scaleValue.setVisibility(View.GONE);
        }

        if (eventV1.getRating() != null) {
            DecimalFormat format = new DecimalFormat("#.#");
            holder.ratingValue.setText(format.format(eventV1.getRating().getRating() / 2.0f) + "");
            holder.starIcon.setVisibility(View.VISIBLE);
            holder.ratingValue.setVisibility(View.VISIBLE);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                holder.scaleValue.setPadding(holder.trackingTitle.getPaddingLeft(), holder.eventDate.getTotalPaddingTop(), 7, 7);
            holder.ratingValue.setVisibility(View.GONE);
            holder.starIcon.setVisibility(View.GONE);
        }

        holder.itemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("trackingId", eventV1.getTrackingId().toString());
                intent.putExtra("eventId", eventV1.getEventId().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        Date eventDate = eventV1.getEventDate();

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());
        holder.eventDate.setText(format.format(eventDate));


    }

    @Override
    public int getItemCount() {
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
        @BindView(R.id.photo)
        ImageView trackingPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
