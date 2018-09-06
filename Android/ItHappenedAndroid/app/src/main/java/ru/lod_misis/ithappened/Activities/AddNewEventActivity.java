package ru.lod_misis.ithappened.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
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
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;
import com.yandex.metrica.YandexMetrica;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Dialog.ChoicePhotoDialog;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Presenters.AddNewEventContract;
import ru.lod_misis.ithappened.Presenters.AddNewEventPresenterImpl;
import ru.lod_misis.ithappened.R;

import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.WorkWithFiles.IWorkWithFIles;
import ru.lod_misis.ithappened.WorkWithFiles.WorkWithFiles;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class AddNewEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,AddNewEventContract.AddNewEventView {

    TrackingService trackingService;
    InMemoryFactRepository factRepository;
    ITrackingRepository trackingCollection;

    int commentState;
    int scaleState;
    int ratingState;
    int geopositionState;
    int photoState;

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

    @BindView(R.id.commentEventContainer)
    LinearLayout commentContainer;
    @BindView(R.id.scaleEventContainer)
    LinearLayout scaleContainer;
    @BindView(R.id.ratingEventContainer)
    LinearLayout ratingContainer;
    @BindView(R.id.geopositionEventContainer)
    LinearLayout geopositionContainer;
    LinearLayout photoContainer;

    @BindView(R.id.commentAccess)
    TextView commentAccess;
    @BindView(R.id.scaleAccess)
    TextView scaleAccess;
    @BindView(R.id.ratingAccess)
    TextView ratingAccess;
    @BindView(R.id.geopositionAccess)
    TextView geopositionAccess;
    TextView photoAccess;

    @BindView(R.id.eventCommentControl)
    EditText commentControl;
    @BindView(R.id.eventScaleControl)
    EditText scaleControl;
    @BindView(R.id.ratingEventControl)
    RatingBar ratingControl;
    @BindView(R.id.eventDateControl)
    Button dateControl;
    SupportMapFragment supportMapFragment;
    GoogleMap map;

    @BindView(R.id.scaleTypeAccess)
    TextView scaleType;
    @BindView(R.id.addEvent)
    Button addEvent;

    Double latitude = null;
    Double longitude = null;

    IWorkWithFIles workWithFIles;
    String photoPath;
    @BindView(R.id.photo)
    ImageView photo;
    ChoicePhotoDialog dialog;

    LocationManager locationManager;
    Marker marker;

    TrackingV1 trackingV1;

    Context context;
    Activity activity;

    AddNewEventContract.AddNewEventPresenter addNeEventPresenter;

    boolean flagPhoto=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        ButterKnife.bind(this);
        YandexMetrica.reportEvent("Пользователь вошел в создание события");

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        addNeEventPresenter=new AddNewEventPresenterImpl(sharedPreferences);
        addNeEventPresenter.attachView(this);
        addNeEventPresenter.init(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dateControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.create();
                datePickerDialog.show();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workWithFIles=new WorkWithFiles(getApplication(),context);
                dialog=new ChoicePhotoDialog(context,activity,workWithFIles);
                dialog.show();
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNeEventPresenter.addNewEvent();
            }
        });
        scaleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleControl.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(scaleControl, InputMethodManager.SHOW_IMPLICIT);
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
        eventYear = year - 1900;
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

    private int calculateState(TrackingCustomization customization) {
        switch (customization) {
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

    private void calculateUX(LinearLayout container, TextView access, int state) {
        switch (state) {
            case 0:
                container.setVisibility(View.GONE);
                break;
            case 1:
                break;
            case 2:
                access.setText(access.getText().toString() + "*");
                break;
            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    createAndInitMap();
                }else{
                    onBackPressed();
                }
            }
        }
    }

    private void initMap() {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                map = googleMap;
                CameraUpdate cameraUpdate;
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    addNeEventPresenter.requestPermission(1);
                } else {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.d("test position", location.getLongitude() + "");
                    marker = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                    cameraUpdate = CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(5)
                                    .build()
                    );
                    map.moveCamera(cameraUpdate);
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
                            latitude = marker.getPosition().latitude;
                            longitude = marker.getPosition().longitude;
                        }
                    });
                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            latitude = latLng.latitude;
                            longitude = latLng.longitude;
                            marker.setPosition(latLng);

                        }
                    });
                }

            }
        });
    }

    @Override
    public void addNewEvent() {
        addEvent();
    }

    @Override
    public void requestPermissionForGeoposition() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void requestPermissionForCamera() {

    }

    @Override
    public void startConfigurationView() {

        calculateUx();

        createAndInitMap();
        initToolbar();

        if (trackingV1.GetScaleCustomization() != TrackingCustomization.None && trackingV1.getScaleName() != null) {
            scaleType.setText(trackingV1.getScaleName());
        }

        initDate();

        initCalenarDialog();
    }

    @Override
    public void startedConfiguration(UUID trackingId,TrackingV1 trackingV1) {

        eventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleControl.setKeyListener(keyListener);

        this.trackingV1=trackingV1;
        this.trackingId=trackingId;

        context = this;
        activity=this;

        calculateState();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishAddEventActivity() {
        finish();
        addNeEventPresenter.detachView();
    }

    private void addEvent(){
        boolean commentFlag = true;
        boolean scaleFlag = true;
        boolean ratingFlag = true;
        boolean geopositionFlag = true;
        boolean photoFlag = true;

        if (commentState == 2 && commentControl.getText().toString().isEmpty()) {
            commentFlag = false;
        }

        if (ratingState == 2 && ratingControl.getRating() == 0) {
            ratingFlag = false;
        }

        if (scaleState == 2 && scaleControl.getText().toString().isEmpty()) {
            scaleFlag = false;
        }
        if (geopositionState == 2 && (latitude == null || longitude == null)) {
            geopositionFlag = false;
        }
        if (photoState == 2 && photoPath==null) {
            photoFlag = false;
        }

        String comment = null;
        Double scale = null;
        Rating rating = null;


        if (commentFlag && ratingFlag && scaleFlag && geopositionFlag&&photoFlag) {
            if (!commentControl.getText().toString().isEmpty() && !commentControl.getText().toString().trim().isEmpty()) {
                comment = commentControl.getText().toString().trim();}
            if (!(ratingControl.getRating() == 0)) {
                rating = new Rating((int) (ratingControl.getRating() * 2));
            }
            if (!scaleControl.getText().toString().isEmpty()) {
                try {
                    scale = Double.parseDouble(scaleControl.getText().toString().trim());
                    addNeEventPresenter.saveEvent(new EventV1(UUID.randomUUID(), trackingId, eventDate, scale, rating, comment, latitude, longitude,photoPath),trackingId);
                    YandexMetrica.reportEvent("Пользователь добавил событие");


                } catch (Exception e) {
                    showMessage("Введите число");
                }
            } else {
                if (timeSetFlag) {
                    Locale locale = new Locale("ru");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
                    simpleDateFormat.setTimeZone(TimeZone.getDefault());
                    try {
                        eventDate = simpleDateFormat.parse(dateControl.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                addNeEventPresenter.saveEvent(new EventV1(UUID.randomUUID(), trackingId, eventDate, scale, rating, comment, latitude, longitude,photoPath),trackingId);
                YandexMetrica.reportEvent(getString(R.string.metrica_add_event));


            }
        } else {
            showMessage("Заполните поля с *");
        }

}
    private void calculateState(){
        commentState = calculateState(trackingV1.GetCommentCustomization());
        ratingState = calculateState(trackingV1.GetRatingCustomization());
        scaleState = calculateState(trackingV1.GetScaleCustomization());
        geopositionState = calculateState(trackingV1.GetGeopositionCustomization());
    }
    private void calculateUx(){
        calculateUX(commentContainer, commentAccess, commentState);
        calculateUX(ratingContainer, ratingAccess, ratingState);
        calculateUX(scaleContainer, scaleAccess, scaleState);
        calculateUX(geopositionContainer, geopositionAccess, geopositionState);
    }

    private void initToolbar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(trackingV1.GetTrackingName());
    }

    private void initDate(){
        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());

        dateControl.setText(format.format(eventDate).toString());
    }

    private void initCalenarDialog(){
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
    }
    private void createAndInitMap(){
        if (geopositionState == 1 || geopositionState == 2){
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        initMap();
        }
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            dialog.cancel();
            Toast.makeText(getApplicationContext(), "Упс,что-то пошло не так =(((("+"\n"+"Фотографию не удалось загрузить" , Toast.LENGTH_SHORT).show();
            return;
       }
        if(requestCode==1){

            Picasso.get().load(Uri.parse(workWithFIles.getUriPhotoFromCamera())).into(photo);
            photoPath =workWithFIles.saveBitmap(Uri.parse(workWithFIles.getUriPhotoFromCamera()));
            flagPhoto=true;
            dialog.cancel();
        }
        if(requestCode==2){
            Picasso.get().load(data.getData()).into(photo);
            photoPath =workWithFIles.saveBitmap(data.getData());
            flagPhoto=true;
            dialog.cancel();
        }
    }
}


