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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;

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

    InMemoryFactRepository factRepository;

    Button editEvent;
    Button deleteEvent;

    UUID trackingId;
    UUID eventId;
    ITrackingRepository collection;
    TrackingService trackingSercvice;

    CardView valuesCard;
    CardView nullsCard;

    ImageView commentHint;
    ImageView scaleHint;

    TextView commentValue;
    TextView scaleValue;
    TextView dateValue;
    TextView dateValueNulls;
    RatingBar ratingValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        YandexMetrica.reportEvent("Пользователь зашел в детали события");

        valuesCard = findViewById(R.id.valuesCard);
        nullsCard = findViewById(R.id.nullsCard);

        commentHint = findViewById(R.id.commentHint);
        scaleHint = findViewById(R.id.scaleHint);

        commentValue = findViewById(R.id.commentValue);
        scaleValue = findViewById(R.id.scaleValue);
        ratingValue = findViewById(R.id.ratingValue);
        dateValue = findViewById(R.id.dateValue);
        dateValueNulls = findViewById(R.id.dateValueNulls);

        editEvent = findViewById(R.id.editEventButton);
        deleteEvent = findViewById(R.id.deleteEventButton);

        factRepository = StaticFactRepository.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        collection = new StaticInMemoryRepository(getApplicationContext(), sharedPreferences.getString("UserId" , "")).getInstance();
        trackingSercvice = new TrackingService(sharedPreferences.getString("UserId", ""), collection);

        Intent intent = getIntent();
        trackingId = UUID.fromString(intent.getStringExtra("trackingId"));
        eventId = UUID.fromString(intent.getStringExtra("eventId"));

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


        if ((thisTracking.GetCommentCustomization()==TrackingCustomization.None
                && thisTracking.GetScaleCustomization()==TrackingCustomization.None
                && thisTracking.GetRatingCustomization()==TrackingCustomization.None)
                ||
                ((thisTracking.GetCommentCustomization()==TrackingCustomization.Optional&&thisEvent.GetComment()==null)
                &&(thisTracking.GetScaleCustomization()==TrackingCustomization.Optional&&thisEvent.GetScale()==null)
                &&(thisTracking.GetRatingCustomization()==TrackingCustomization.Optional&&thisEvent.GetRating()==null)
                )
                ){
            valuesCard.setVisibility(View.GONE);
            nullsCard.setVisibility(View.VISIBLE);
            Date thisDate = thisEvent.GetEventDate();

            Locale loc = new Locale("ru");
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);

            dateValueNulls.setText(format.format(thisDate));

        }else{

            valuesCard.setVisibility(View.VISIBLE);
            nullsCard.setVisibility(View.GONE);

            Date thisDate = thisEvent.GetEventDate();

            Locale loc = new Locale("ru");
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);

            dateValue.setText(format.format(thisDate));

            if(thisEvent.GetRating()!=null) {
                ratingValue.setRating(thisEvent.GetRating().getRating()/2.0f);
            }else {
                ratingValue.setVisibility(View.GONE);
            }

            if(thisEvent.GetComment()!=null) {
                commentValue.setText(thisEvent.GetComment());
            }else {
                commentValue.setVisibility(View.GONE);
                commentHint.setVisibility(View.GONE);
            }

            if(thisEvent.GetScale()!=null) {
                scaleValue.setText(thisEvent.GetScale().toString()+" "+thisTracking.getScaleName());
            }else {
                scaleValue.setVisibility(View.GONE);
                scaleHint.setVisibility(View.GONE);
            }
        }
        TrackingCustomization commentCustomization = thisTracking.GetCommentCustomization();
        TrackingCustomization scaleCustomization = thisTracking.GetScaleCustomization();
        TrackingCustomization ratingCustomization = thisTracking.GetRatingCustomization();
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
        YandexMetrica.reportEvent("Пользователь удалил событие");
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
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent("Пользователь вышел из деталей события");
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
