package ru.lod_misis.ithappened.Activities;

import android.app.FragmentManager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Fragments.DeleteEventDialog;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.Presenters.EventDetailsContract;
import ru.lod_misis.ithappened.Presenters.EventDetailsPresenterImpl;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;
import ru.lod_misis.ithappened.Utils.UserDataUtils;
import ru.lod_misis.ithappened.WorkWithFiles.IWorkWithFIles;
import ru.lod_misis.ithappened.WorkWithFiles.WorkWithFiles;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class EventDetailsActivity extends AppCompatActivity implements EventDetailsContract.EventDetailsView {

    InMemoryFactRepository factRepository;

    @BindView(R.id.editEventButton)
    Button editEvent;
    @BindView(R.id.deleteEventButton)
    Button deleteEvent;

    UUID trackingId;
    UUID eventId;
    ITrackingRepository collection;
    TrackingService trackingSercvice;

    @BindView(R.id.valuesCard)
    CardView valuesCard;
    @BindView(R.id.nullsCard)
    CardView nullsCard;

    @BindView(R.id.commentHint)
    ImageView commentHint;
    @BindView(R.id.scaleHint)
    ImageView scaleHint;

    @BindView(R.id.commentValue)
    TextView commentValue;
    @BindView(R.id.scaleValue)
    TextView scaleValue;
    @BindView(R.id.dateValue)
    TextView dateValue;
    @BindView(R.id.dateValueNulls)
    TextView dateValueNulls;
    @BindView(R.id.ratingValue)
    RatingBar ratingValue;

    @BindView(R.id.geoposition_title)
    TextView geoposition_title;
    SupportMapFragment supportMapFragment;
    GoogleMap map;

    @BindView(R.id.photo_title)
    TextView photo_title;
    @BindView(R.id.photo)
    ImageView photo;

    Double lotitude;
    Double longitude;
    IWorkWithFIles workWithFIles;

    TrackingV1 thisTrackingV1;
    EventV1 thisEventV1;
    Date thisDate;
    SimpleDateFormat format;

    SharedPreferences sharedPreferences;

    Intent intent;

    EventDetailsContract.EventDetailsPresenter eventDetailsPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        intent = getIntent();
        ButterKnife.bind(this);

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_event_details));
        sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);

        StaticInMemoryRepository.setInstance(getApplicationContext(), sharedPreferences.getString("UserId", ""));

        eventDetailsPresenter=new EventDetailsPresenterImpl(sharedPreferences,intent);
        eventDetailsPresenter.attachView(this);
        eventDetailsPresenter.init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventDetailsPresenter.editEvent();
            }
        });

        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventDetailsPresenter.deleteEvent();
            }
        });
    }

    public void okClicked() {
        eventDetailsPresenter.okClicked();

        YandexMetrica.reportEvent("Пользователь удалил событие");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void cancelClicked() {
        eventDetailsPresenter.canselClicked();
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

    @Override
    public void startConfigurationView() {
        supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        showOrNotNullCard();

        if(thisEventV1.GetRating()!=null) {
            ratingValue.setVisibility(View.VISIBLE);
            nullsCard.setVisibility(View.GONE);
            valuesCard.setVisibility(View.VISIBLE);
            ratingValue.setRating(thisEventV1.GetRating().getRating()/2.0f);
        }else {
            ratingValue.setVisibility(View.GONE);
        }
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
            getSupportFragmentManager().beginTransaction().hide(supportMapFragment).commit();
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
    }

    @Override
    public void startedConfiguration(ITrackingRepository collection,UUID trackingId, UUID eventId) {

        setTitle(collection.GetTracking(trackingId).GetTrackingName());
        this.eventId=eventId;
        this.trackingId=trackingId;
        thisEventV1 = collection.GetTracking(trackingId).GetEvent(eventId);
        thisTrackingV1 = collection.GetTracking(trackingId);
        thisEventV1 = thisTrackingV1.GetEvent(eventId);
        thisDate = thisEventV1.GetEventDate();

        Locale loc = new Locale("ru");
        format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());




        TrackingCustomization commentCustomization = thisTrackingV1.GetCommentCustomization();
        TrackingCustomization scaleCustomization = thisTrackingV1.GetScaleCustomization();
        TrackingCustomization ratingCustomization = thisTrackingV1.GetRatingCustomization();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishDetailsEventActivity() {
        eventDetailsPresenter.detachView();
        finish();

    }

    @Override
    public void deleteEvent() {
        DeleteEventDialog delete = new DeleteEventDialog();
        delete.show(getFragmentManager(), "DeleteEvent");
    }

    @Override
    public void editEvent() {
        Intent intent = new Intent(getApplicationContext(), EditEventActivity.class);
        intent.putExtra("eventId", eventId.toString());
        intent.putExtra("trackingId", trackingId.toString());
        startActivity(intent);
        eventDetailsPresenter.detachView();
    }
    private void showOrNotNullCard(){
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
        }
    }
}
