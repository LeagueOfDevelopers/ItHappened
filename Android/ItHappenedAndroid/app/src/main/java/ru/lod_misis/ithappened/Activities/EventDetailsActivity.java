package ru.lod_misis.ithappened.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.yandex.metrica.YandexMetrica;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Fragments.DeleteEventDialog;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;
import ru.lod_misis.ithappened.WorkWithFiles.IWorkWithFIles;
import ru.lod_misis.ithappened.WorkWithFiles.WorkWithFiles;
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

    TextView geoposition_title;
    SupportMapFragment supportMapFragment;
    GoogleMap map;

    TextView photo_title;
    ImageView photo;

    Double lotitude;
    Double longitude;
    IWorkWithFIles workWithFIles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_event_details));

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

        supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        geoposition_title=findViewById(R.id.geoposition_title);

        photo_title=findViewById(R.id.photo_title);
        photo=findViewById(R.id.photo);

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        StaticInMemoryRepository.setInstance(getApplicationContext(), sharedPreferences.getString("UserId", ""));
        if(sharedPreferences.getString("LastId","").isEmpty()) {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
            collection = StaticInMemoryRepository.getInstance();
        }else{
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
            collection = StaticInMemoryRepository.getInstance();
        }
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


        TrackingV1 thisTrackingV1 = collection.GetTracking(trackingId);
        EventV1 thisEventV1 = thisTrackingV1.GetEvent(eventId);


        if ((thisTrackingV1.GetCommentCustomization()==TrackingCustomization.None
                && thisTrackingV1.GetScaleCustomization()==TrackingCustomization.None
                && thisTrackingV1.GetRatingCustomization()==TrackingCustomization.None)
                ||
                ((thisTrackingV1.GetCommentCustomization()==TrackingCustomization.Optional&& thisEventV1.GetComment()==null)
                &&(thisTrackingV1.GetScaleCustomization()==TrackingCustomization.Optional&& thisEventV1.GetScale()==null)
                &&(thisTrackingV1.GetRatingCustomization()==TrackingCustomization.Optional&& thisEventV1.GetRating()==null)
                )
                ){
            valuesCard.setVisibility(View.GONE);
            nullsCard.setVisibility(View.VISIBLE);
            Date thisDate = thisEventV1.GetEventDate();

            Locale loc = new Locale("ru");
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
            format.setTimeZone(TimeZone.getDefault());

            dateValueNulls.setText(format.format(thisDate));

        }

            Date thisDate = thisEventV1.GetEventDate();

            Locale loc = new Locale("ru");
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
            format.setTimeZone(TimeZone.getDefault());

            dateValue.setText(format.format(thisDate));

            if(thisEventV1.GetRating()!=null) {
                ratingValue.setVisibility(View.VISIBLE);
                nullsCard.setVisibility(View.GONE);
                valuesCard.setVisibility(View.VISIBLE);
                ratingValue.setRating(thisEventV1.GetRating().getRating()/2.0f);
            }else {
                ratingValue.setVisibility(View.GONE);
            }

            if(thisEventV1.GetComment()!=null) {
                nullsCard.setVisibility(View.GONE);
                valuesCard.setVisibility(View.VISIBLE);
                commentValue.setVisibility(View.VISIBLE);
                commentValue.setText(thisEventV1.GetComment());
            }else {
                commentValue.setVisibility(View.GONE);
                commentHint.setVisibility(View.GONE);
            }

            if(thisEventV1.GetScale()!=null) {
                nullsCard.setVisibility(View.GONE);
                valuesCard.setVisibility(View.VISIBLE);
                scaleValue.setVisibility(View.VISIBLE);
                scaleValue.setText(StringParse.parseDouble(thisEventV1.GetScale().doubleValue())+" "+ thisTrackingV1.getScaleName());
            }else {
                scaleValue.setVisibility(View.GONE);
                scaleHint.setVisibility(View.GONE);
            }
            if(thisEventV1.getLongitude()!=null &&thisEventV1.getLotitude()!=null){

                this.lotitude=thisEventV1.getLotitude();
                this.longitude=thisEventV1.getLongitude();
                nullsCard.setVisibility(View.GONE);
                valuesCard.setVisibility(View.VISIBLE);
                scaleValue.setVisibility(View.GONE);
                scaleHint.setVisibility(View.GONE);
                commentValue.setVisibility(View.GONE);
                commentHint.setVisibility(View.GONE);
                ratingValue.setVisibility(View.GONE);

                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        CameraUpdate cameraUpdate;
                        map=googleMap;
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        map.addMarker(new MarkerOptions().position(new LatLng(lotitude,longitude)));
                        cameraUpdate= CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(new LatLng(lotitude,longitude))
                                        .zoom(5)
                                        .build()
                        );
                        map.moveCamera(cameraUpdate);
                    }
                });
            }else{
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(supportMapFragment);
                fragmentTransaction.commit();
                geoposition_title.setVisibility(View.GONE);
            }

        if(thisEventV1.getPhoto()!=null){
            workWithFIles=new WorkWithFiles(getApplication(),this);
            photo.setImageBitmap(workWithFIles.loadImage(thisEventV1.getPhoto()));
            nullsCard.setVisibility(View.GONE);
            valuesCard.setVisibility(View.VISIBLE);
            scaleValue.setVisibility(View.GONE);
            scaleHint.setVisibility(View.GONE);
            commentValue.setVisibility(View.GONE);
            commentHint.setVisibility(View.GONE);
            ratingValue.setVisibility(View.GONE);

        }else{
            photo_title.setVisibility(View.GONE);
            photo.setVisibility(View.GONE);
        }
        TrackingCustomization commentCustomization = thisTrackingV1.GetCommentCustomization();
        TrackingCustomization scaleCustomization = thisTrackingV1.GetScaleCustomization();
        TrackingCustomization ratingCustomization = thisTrackingV1.GetRatingCustomization();
        TrackingCustomization photoCustomization = thisTrackingV1.GetPhotoCustomization();
    }

    public void okClicked() {

        trackingSercvice.RemoveEvent(eventId);
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
        EventV1 thisEventV1 = collection.GetTracking(trackingId).GetEvent(eventId);
        if(thisEventV1.GetRating()!=null) {
            ratingValue.setVisibility(View.VISIBLE);
            nullsCard.setVisibility(View.GONE);
            valuesCard.setVisibility(View.VISIBLE);
            ratingValue.setRating(thisEventV1.GetRating().getRating()/2.0f);
        }else {
            ratingValue.setVisibility(View.GONE);
        }
    }

    public void cancelClicked() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_event_details));
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
