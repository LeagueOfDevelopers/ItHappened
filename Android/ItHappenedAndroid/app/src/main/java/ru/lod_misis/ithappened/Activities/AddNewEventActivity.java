package ru.lod_misis.ithappened.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.yandex.metrica.YandexMetrica;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddNewEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TrackingService trackingService;
    InMemoryFactRepository factRepository;
    ITrackingRepository trackingCollection;

    int commentState;
    int scaleState;
    int ratingState;
    int geopositionState;

    UUID trackingId;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    //Date time attrs
    int eventYear;
    int eventMonth;
    int eventDay;
    int eventHour;
    int eventMinuets;
    int eventSeconds;

    boolean timeSetFlag = false;


    Date eventDate;

    LinearLayout commentContainer;
    LinearLayout scaleContainer;
    LinearLayout ratingContainer;
    LinearLayout geopositionContainer;

    TextView commentAccess;
    TextView scaleAccess;
    TextView ratingAccess;
    TextView geopositionAccess;

    EditText commentControl;
    EditText scaleControl;
    RatingBar ratingControl;
    Button dateControl;
    SupportMapFragment supportMapFragment;
    GoogleMap map;

    TextView scaleType;

    Button addEvent;

    Double latitude = null;
    Double longitude = null;

    LocationManager locationManager;
    Marker marker;

    TrackingV1 trackingV1;

    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        context=this;
        YandexMetrica.reportEvent("Пользователь вошел в создание события");

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        if (sharedPreferences.getString("LastId", "").isEmpty()) {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
            trackingCollection = StaticInMemoryRepository.getInstance();
        } else {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
            trackingCollection = StaticInMemoryRepository.getInstance();
        }
        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), trackingCollection);

        factRepository = StaticFactRepository.getInstance();
        eventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        trackingId = UUID.fromString(getIntent().getStringExtra("trackingId"));


        commentContainer = (LinearLayout) findViewById(R.id.commentEventContainer);
        ratingContainer = (LinearLayout) findViewById(R.id.ratingEventContainer);
        scaleContainer = (LinearLayout) findViewById(R.id.scaleEventContainer);

        commentAccess = (TextView) findViewById(R.id.commentAccess);
        scaleAccess = (TextView) findViewById(R.id.scaleAccess);
        ratingAccess = (TextView) findViewById(R.id.ratingAccess);
        geopositionAccess = (TextView) findViewById(R.id.geopositionAccess);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        initMap();

        commentControl = (EditText) findViewById(R.id.eventCommentControl);
        scaleControl = (EditText) findViewById(R.id.eventScaleControl);
        ratingControl = (RatingBar) findViewById(R.id.ratingEventControl);
        dateControl = (Button) findViewById(R.id.eventDateControl);

        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleControl.setKeyListener(keyListener);

        scaleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleControl.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(scaleControl, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        scaleType = (TextView) findViewById(R.id.scaleTypeAccess);

        addEvent = (Button) findViewById(R.id.addEvent);

        trackingV1 = trackingCollection.GetTracking(trackingId);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(trackingV1.GetTrackingName());

        commentState = calculateState(trackingV1.GetCommentCustomization());
        ratingState = calculateState(trackingV1.GetRatingCustomization());
        scaleState = calculateState(trackingV1.GetScaleCustomization());
        geopositionState=calculateState(trackingV1.GetGeopositionCustomization());


        calculateUX(commentContainer, commentAccess, commentState);
        calculateUX(ratingContainer, ratingAccess, ratingState);
        calculateUX(scaleContainer, scaleAccess, scaleState);
        calculateUX(geopositionContainer,geopositionAccess,geopositionState);

        if(trackingV1.GetScaleCustomization()!=TrackingCustomization.None && trackingV1.getScaleName()!=null){
                scaleType.setText(trackingV1.getScaleName());
        }

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());

        dateControl.setText(format.format(eventDate).toString());

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        datePickerDialog = new DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(
                this,
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);

        dateControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.create();
                datePickerDialog.show();
            }
        });


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean commentFlag = true;
                boolean scaleFlag = true;
                boolean ratingFlag = true;
                Boolean geopositionFlag=true;

                if(commentState == 2 && commentControl.getText().toString().isEmpty()){
                    commentFlag=false;
                }

                if(ratingState == 2 && ratingControl.getRating() == 0){
                    ratingFlag = false;
                }

                if(scaleState == 2 && scaleControl.getText().toString().isEmpty()){
                    scaleFlag = false;
                }
                if(geopositionState==2 && (latitude==null||longitude==null)){
                    geopositionFlag=false;
                }


                String comment = null;
                Double scale = null;
                Rating rating = null;


                if(commentFlag&&ratingFlag&&scaleFlag&&geopositionFlag){
                    if(!commentControl.getText().toString().isEmpty()&&!commentControl.getText().toString().trim().isEmpty()){
                        comment = commentControl.getText().toString().trim();
                    }
                    if(!(ratingControl.getRating()==0)){
                        rating = new Rating((int) (ratingControl.getRating()*2));
                    }
                    if(!scaleControl.getText().toString().isEmpty()){
                        try {
                            scale = Double.parseDouble(scaleControl.getText().toString().trim());
                            trackingService.AddEvent(trackingId,
                                    new EventV1(UUID.randomUUID(),
                                            trackingId,
                                            eventDate,
                                            scale,
                                            rating,
                                            comment,
                                            latitude,
                                            longitude
                                            ));
                            factRepository.onChangeCalculateOneTrackingFacts(trackingCollection.GetTrackingCollection(), trackingId)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Fact>() {
                                        @Override
                                        public void call(Fact fact) {
                                            Log.d("Statistics", "calculate");
                                        }
                                    });
                            factRepository.calculateAllTrackingsFacts(trackingCollection.GetTrackingCollection())
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Fact>() {
                                        @Override
                                        public void call(Fact fact) {
                                            Log.d("Statistics", "calculate");
                                        }
                                    });
                            YandexMetrica.reportEvent("Пользователь добавил событие");
                            Toast.makeText(getApplicationContext(), "Событие добавлено", Toast.LENGTH_SHORT).show();
                            finish();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Введите число", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if(timeSetFlag) {
                            Locale locale = new Locale("ru");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
                            simpleDateFormat.setTimeZone(TimeZone.getDefault());
                            try {
                                eventDate = simpleDateFormat.parse(dateControl.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        trackingService.AddEvent(trackingId, new EventV1(UUID.randomUUID(), trackingId, eventDate, scale, rating, comment,latitude,longitude));
                        factRepository.onChangeCalculateOneTrackingFacts(trackingCollection.GetTrackingCollection(), trackingId)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call(Fact fact) {
                                        Log.d("Statistics", "calculate");
                                    }
                                });
                        factRepository.calculateAllTrackingsFacts(trackingCollection.GetTrackingCollection())
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call(Fact fact) {
                                        Log.d("Statistics", "calculate");
                                    }
                                });
                        YandexMetrica.reportEvent(getString(R.string.metrica_add_event));
                        Toast.makeText(getApplicationContext(), "Событие добавлено", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Заполните поля с *", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_from_add_event));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        eventYear = year-1900;
        eventMonth = month;
        eventDay = day;
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minuets) {
        eventHour = hour;
        eventMinuets = minuets;
        eventDate = new Date(eventYear, eventMonth, eventDay, hour, minuets);
        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());
        timeSetFlag = true;
        dateControl.setText(format.format(eventDate).toString());

    }

    private int calculateState(TrackingCustomization customization){
        switch (customization){
            case None:
                return 0;
            case Optional:
                return 1;
            case Required:
                return 2;
            default:
                break;
        }
        return 0;
    }

    private void calculateUX(LinearLayout container, TextView access, int state){
        switch (state){
            case 0:
                container.setVisibility(View.GONE);
                break;
            case 1:
                break;
            case 2:
                access.setText(access.getText().toString()+"*");
                break;
            default:
                break;
        }
    }

private void initMap(){
    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            map = googleMap;
            CameraUpdate cameraUpdate;
            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                marker.setPosition(new LatLng(0,0));
            }
            else{
                Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.d("test position",location.getLongitude()+"");
                marker= map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
                cameraUpdate=CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(),location.getLongitude()))
                                .zoom(5)
                                .build()
                );
                map.moveCamera(cameraUpdate);
            }
            marker.setDraggable(true);
            map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    latitude=marker.getPosition().latitude;
                    longitude=marker.getPosition().longitude;
                }
            });
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    latitude=latLng.latitude;
                    longitude=latLng.longitude;
                    marker.setPosition(latLng);

                }
            });
        }
    });
}
}


