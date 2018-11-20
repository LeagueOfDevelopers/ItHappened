package ru.lod_misis.ithappened.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractor;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractorImpl;
import ru.lod_misis.ithappened.domain.statistics.facts.StringParse;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.ui.activities.mapactivity.MapActivity;
import ru.lod_misis.ithappened.ui.fragments.DeleteEventDialog;
import ru.lod_misis.ithappened.ui.presenters.EventDetailsContract;

public class EventDetailsActivity extends AppCompatActivity implements EventDetailsContract.EventDetailsView {
    @BindView(R.id.editEventButton)
    Button editEvent;
    @BindView(R.id.deleteEventButton)
    Button deleteEvent;

    UUID trackingId;
    UUID eventId;

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
    @BindView(R.id.adress)
    TextView adress;
    @BindView(R.id.ratingValue)
    RatingBar ratingValue;

    @BindView(R.id.geoposition_title)
    TextView geoposition_title;

    @BindView(R.id.photo_title)
    TextView photo_title;
    @BindView(R.id.photo)
    ImageView photo;

    Double latitude;
    Double longitude;
    PhotoInteractor workWithFIles;
    TrackingV1 thisTrackingV1;
    EventV1 thisEventV1;
    Date thisDate;
    SimpleDateFormat format;

    Intent intent;

    Activity activity;
    @Inject
    EventDetailsContract.EventDetailsPresenter eventDetailsPresenter;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        intent = getIntent();
        ButterKnife.bind(this);
        activity = this;

        ItHappenedApplication.getAppComponent().inject(this);

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_event_details));

        eventDetailsPresenter.attachView(this ,
                UUID.fromString(getIntent().getStringExtra("trackingId")) ,
                UUID.fromString(getIntent().getStringExtra("eventId")));

        eventDetailsPresenter.init();
        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                eventDetailsPresenter.editEvent();
            }
        });
        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                MapActivity.toMapActivity(activity , 2 , latitude , longitude);
            }
        });
        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                eventDetailsPresenter.deleteEvent();
            }
        });
    }

    public void okClicked () {
        eventDetailsPresenter.okClicked();

        YandexMetrica.reportEvent("Пользователь удалил событие");
    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void cancelClicked () {
        eventDetailsPresenter.canselClicked();
    }

    @Override
    protected void onPause () {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_event_details));
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch ( item.getItemId() ) {
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
    protected void onRestart () {
        super.onRestart();
        recreate();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void startConfigurationView () {
        if (thisEventV1.getRating() != null) {
            ratingValue.setVisibility(View.VISIBLE);
            nullsCard.setVisibility(View.GONE);
            valuesCard.setVisibility(View.VISIBLE);
            ratingValue.setRating(thisEventV1.getRating().getRating() / 2.0f);
        } else {
            ratingValue.setVisibility(View.GONE);
        }
        dateValue.setText(format.format(thisDate));

        if (thisEventV1.getRating() != null) {
            ratingValue.setVisibility(View.VISIBLE);
            nullsCard.setVisibility(View.GONE);
            valuesCard.setVisibility(View.VISIBLE);
            ratingValue.setRating(thisEventV1.getRating().getRating() / 2.0f);
        } else {
            ratingValue.setVisibility(View.GONE);
        }

        if (thisEventV1.getComment() != null) {
            nullsCard.setVisibility(View.GONE);
            valuesCard.setVisibility(View.VISIBLE);
            commentValue.setVisibility(View.VISIBLE);
            commentValue.setText(thisEventV1.getComment());
        } else {
            commentValue.setVisibility(View.GONE);
            commentHint.setVisibility(View.GONE);
        }

        if (thisEventV1.getScale() != null) {
            nullsCard.setVisibility(View.GONE);
            valuesCard.setVisibility(View.VISIBLE);
            scaleValue.setVisibility(View.VISIBLE);
            scaleValue.setText(StringParse.parseDouble(thisEventV1.getScale()) + " " + thisTrackingV1.getScaleName());
        } else {
            scaleValue.setVisibility(View.GONE);
            scaleHint.setVisibility(View.GONE);
        }
        if (thisEventV1.getLongitude() != null && thisEventV1.getLotitude() != null) {

            this.latitude = thisEventV1.getLotitude();
            this.longitude = thisEventV1.getLongitude();
            nullsCard.setVisibility(View.GONE);
            valuesCard.setVisibility(View.VISIBLE);
            try {
                adress.setText(getAddress(this.latitude , this.longitude));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            adress.setVisibility(View.GONE);
            geoposition_title.setVisibility(View.GONE);
        }
        if (thisEventV1.getPhoto() != null) {
            workWithFIles = new PhotoInteractorImpl(this);
            photo.setImageBitmap(workWithFIles.loadImage(thisEventV1.getPhoto()));
            nullsCard.setVisibility(View.GONE);

        } else {
            photo_title.setVisibility(View.GONE);
            photo.setVisibility(View.GONE);
        }
    }

    @Override
    public void startedConfiguration (TrackingService collection , UUID trackingId , UUID eventId) {
        setTitle(collection.GetTrackingById(trackingId).getTrackingName());
        this.eventId = eventId;
        this.trackingId = trackingId;
        thisEventV1 = collection.GetTrackingById(trackingId).getEvent(eventId);
        thisTrackingV1 = collection.GetTrackingById(trackingId);
        thisEventV1 = thisTrackingV1.getEvent(eventId);
        thisDate = thisEventV1.getEventDate();

        Locale loc = new Locale("ru");
        format = new SimpleDateFormat("dd.MM.yyyy HH:mm" , loc);
        format.setTimeZone(TimeZone.getDefault());
    }

    @Override
    public void showMessage (String message) {
        Toast.makeText(this , message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishDetailsEventActivity () {
        eventDetailsPresenter.detachView();
        finish();
    }

    @Override
    public void deleteEvent () {
        DeleteEventDialog delete = new DeleteEventDialog();
        delete.show(getFragmentManager() , "DeleteEvent");
    }

    @Override
    public void editEvent () {
        Intent intent = new Intent(getApplicationContext() , EditEventActivity.class);
        intent.putExtra("eventId" , eventId.toString());
        intent.putExtra("trackingId" , trackingId.toString());
        startActivity(intent);
        eventDetailsPresenter.detachView();
    }

    private String getAddress (Double latitude , Double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this , Locale.getDefault());
        return geocoder.getFromLocation(latitude , longitude , 1).get(0).getAddressLine(0);
    }

    @Override
    protected void onActivityResult (int requestCode , int resultCode , Intent data) {
        if (requestCode == MapActivity.MAP_ACTIVITY_REQUEST_CODE)
            if (resultCode == RESULT_OK)

                super.onActivityResult(requestCode , resultCode , data);
    }
}