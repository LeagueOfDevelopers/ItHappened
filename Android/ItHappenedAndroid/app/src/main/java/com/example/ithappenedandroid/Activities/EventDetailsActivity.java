package com.example.ithappenedandroid.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.UUID;


public class EventDetailsActivity extends AppCompatActivity {

    TextView yourComment;
    TextView yourScale;
    RatingBar yourRating;

    Button editEvent;

    UUID trackingId;
    UUID eventId;
    ITrackingRepository collection = StaticInMemoryRepository.getInstance();
    TrackingService trackingSercvice = new TrackingService("testUser", collection);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Intent intent = getIntent();
        trackingId = UUID.fromString(intent.getStringExtra("trackingId"));
        eventId = UUID.fromString(intent.getStringExtra("eventId"));

        yourComment = (TextView) findViewById(R.id.yourComment);
        yourScale = (TextView) findViewById(R.id.yourScale);
        yourRating = (RatingBar) findViewById(R.id.yourRating);
        editEvent = (Button) findViewById(R.id.editEventButton);

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditEventActivity.class);
                intent.putExtra("eventId", eventId.toString());
                intent.putExtra("trackingId", trackingId.toString());
                startActivity(intent);
            }
        });


        Tracking thisTracking = collection.GetTracking(trackingId);
        Event thisEvent = trackingSercvice.GetEvent(trackingId, eventId);

        TrackingCustomization commentCustomization = thisTracking.GetCommentCustomization();
        TrackingCustomization scaleCustomization = thisTracking.GetScaleCustomization();
        TrackingCustomization ratingCustomization = thisTracking.GetRatingCustomization();

        if((commentCustomization == TrackingCustomization.None)||((commentCustomization == TrackingCustomization.Optional)&&(thisEvent.GetComment()==null))){
            yourComment.setText("У этого события нет комментария");
        }else{
            yourComment.setText(thisEvent.GetComment());
        }

        if((scaleCustomization == TrackingCustomization.None)||((scaleCustomization == TrackingCustomization.Optional)&&(thisEvent.GetScale()==null))){
            yourScale.setText("У этого события нет шкалы");
        }else{
            yourScale.setText(thisEvent.GetScale().toString());
        }

        if((ratingCustomization == TrackingCustomization.None)||((ratingCustomization == TrackingCustomization.Optional)&&(thisEvent.GetRating()==null))){
        }else{
            yourRating.setRating(thisEvent.GetRating().GetRatingValue()/2);
        }


    }
}
