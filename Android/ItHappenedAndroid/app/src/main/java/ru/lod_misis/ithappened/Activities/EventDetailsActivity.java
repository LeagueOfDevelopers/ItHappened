package ru.lod_misis.ithappened.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Fragments.DeleteEventDialog;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class EventDetailsActivity extends AppCompatActivity {

    TextView yourComment;
    TextView yourScale;
    RatingBar yourRating;
    InMemoryFactRepository factRepository;

    Button editEvent;
    Button deleteEvent;

    CardView commentContainer;
    CardView ratingContainer;
    CardView scaleContainer;

    Button eventDate;

    TextView commentHint;
    TextView ratingHint;
    TextView scaleHint;

    TextView scaleType;

    UUID trackingId;
    UUID eventId;
    ITrackingRepository collection;
    TrackingService trackingSercvice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        factRepository = StaticFactRepository.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        collection = new StaticInMemoryRepository(getApplicationContext(), sharedPreferences.getString("UserId" , "")).getInstance();
        trackingSercvice = new TrackingService(sharedPreferences.getString("UserId", ""), collection);

        Intent intent = getIntent();
        trackingId = UUID.fromString(intent.getStringExtra("trackingId"));
        eventId = UUID.fromString(intent.getStringExtra("eventId"));

        yourComment = (TextView) findViewById(R.id.yourComment);
        yourScale = (TextView) findViewById(R.id.yourScale);
        yourRating = (RatingBar) findViewById(R.id.yourRating);
        editEvent = (Button) findViewById(R.id.editEventButton);
        deleteEvent = (Button) findViewById(R.id.deleteEventButton);

        commentContainer=(CardView) findViewById(R.id.eventCommentContainer);
        scaleContainer=(CardView) findViewById(R.id.eventScaleContainer);
        ratingContainer=(CardView) findViewById(R.id.eventRatingContainer);

        eventDate = (Button) findViewById(R.id.eventDateValue);

        scaleType = (TextView) findViewById(R.id.hintScaleType);

        commentHint = (TextView)findViewById(R.id.hintComment);
        ratingHint = (TextView)findViewById(R.id.hintRating);
        scaleHint = (TextView)findViewById(R.id.hintScale);

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

        if((thisTracking.GetCommentCustomization()==TrackingCustomization.None)
                ||(thisTracking.GetCommentCustomization()==TrackingCustomization.Optional&&thisEvent.GetComment()==null)){
            commentHint.setVisibility(View.GONE);
            commentContainer.setVisibility(View.GONE);
        }

        if((thisTracking.GetRatingCustomization()==TrackingCustomization.None)
                ||(thisTracking.GetRatingCustomization()==TrackingCustomization.Optional&&thisEvent.GetRating()==null)){
            ratingHint.setVisibility(View.GONE);
            ratingContainer.setVisibility(View.GONE);
        }

        if((thisTracking.GetScaleCustomization()==TrackingCustomization.None)
                ||(thisTracking.GetScaleCustomization()==TrackingCustomization.Optional&&thisEvent.GetScale()==null)){
            scaleHint.setVisibility(View.GONE);
            scaleContainer.setVisibility(View.GONE);
            scaleType.setVisibility(View.GONE);
        }

        if(thisTracking.getScaleName()!=null){
            scaleType.setText(thisTracking.getScaleName());
        }

        Date thisDate = thisEvent.GetEventDate();

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);

        eventDate.setText(format.format(thisDate));


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
        factRepository.onChangeCalculateOneTrackingFacts(collection.GetTrackingCollection(), trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculateOneTrackingFact");
                    }
                });
        factRepository.calculateAllTrackingsFacts(collection.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculateOneTrackingFact");
                    }
                });
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
