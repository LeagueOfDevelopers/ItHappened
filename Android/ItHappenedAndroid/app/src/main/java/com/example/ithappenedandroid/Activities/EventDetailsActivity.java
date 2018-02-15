package com.example.ithappenedandroid.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Fragments.DeleteEventDialog;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.UUID;


public class EventDetailsActivity extends AppCompatActivity {

    TextView yourComment;
    TextView yourScale;
    RatingBar yourRating;

    Button editEvent;
    Button deleteEvent;

    UUID trackingId;
    UUID eventId;
    ITrackingRepository collection;
    TrackingService trackingSercvice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        collection = new StaticInMemoryRepository(getApplicationContext()).getInstance();
        trackingSercvice = new TrackingService("testUser", collection);

        Intent intent = getIntent();
        trackingId = UUID.fromString(intent.getStringExtra("trackingId"));
        eventId = UUID.fromString(intent.getStringExtra("eventId"));

        yourComment = (TextView) findViewById(R.id.yourComment);
        yourScale = (TextView) findViewById(R.id.yourScale);
        yourRating = (RatingBar) findViewById(R.id.yourRating);
        editEvent = (Button) findViewById(R.id.editEventButton);
        deleteEvent = (Button) findViewById(R.id.deleteEventButton);

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditEventActivity.class);
                intent.putExtra("eventId", eventId.toString());
                intent.putExtra("trackingId", trackingId.toString());
                startActivity(intent);
            }
        });

        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteEventDialog delete = new DeleteEventDialog();
                delete.show(getFragmentManager(), "DeleteEvent");
            }
        });


        Tracking thisTracking = collection.GetTracking(trackingId);
        Event thisEvent = thisTracking.GetEvent(eventId);

        TrackingCustomization commentCustomization = thisTracking.GetCommentCustomization();
        TrackingCustomization scaleCustomization = thisTracking.GetScaleCustomization();
        TrackingCustomization ratingCustomization = thisTracking.GetRatingCustomization();

        if((commentCustomization == TrackingCustomization.None)||((commentCustomization == TrackingCustomization.Optional)&&(thisEvent.GetComment()==null))){
            yourComment.setText("У этого события нет комментария");
        }else {
            if (thisEvent.GetComment() != null) {
                yourComment.setText(thisEvent.GetComment());
            }else{
                yourComment.setText("У этого события нет комментария");
            }
        }

        if((scaleCustomization == TrackingCustomization.None)||((scaleCustomization == TrackingCustomization.Optional)&&(thisEvent.GetScale()==null))){
            yourScale.setText("У этого события нет шкалы");
        }else{
            if(thisEvent.GetScale()!=null) {
                yourScale.setText(thisEvent.GetScale().toString());
            }else{
                yourScale.setText("У этого события нет шкалы");
            }
            }


        if((ratingCustomization == TrackingCustomization.None)||((ratingCustomization == TrackingCustomization.Optional)&&(thisEvent.GetRating()==null))){
            yourRating.setRating(0);
        }else {
            if (thisEvent.GetRating() != null) {
                float value = thisEvent.GetRating().GetRatingValue()/2.0f;
                yourRating.setRating(value);
            }else{
                yourRating.setRating(0);
            }
        }


    }

    public void okClicked() {

        trackingSercvice.RemoveEvent(trackingId, eventId);
        Toast.makeText(this, "Событие удалено", Toast.LENGTH_SHORT).show();
        this.finish();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(collection.GetTracking(trackingId).GetTrackingName());
    }

    public void cancelClicked() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}
