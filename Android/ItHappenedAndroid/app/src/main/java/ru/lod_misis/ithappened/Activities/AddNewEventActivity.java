package ru.lod_misis.ithappened.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Dialog.ChoicePhotoDialog;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Presenters.AddNewEventContract;
import ru.lod_misis.ithappened.Presenters.AddNewEventPresenterImpl;
import ru.lod_misis.ithappened.R;

import ru.lod_misis.ithappened.WorkWithFiles.IWorkWithFIles;
import ru.lod_misis.ithappened.WorkWithFiles.WorkWithFiles;
import rx.android.schedulers.AndroidSchedulers;


public class AddNewEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AddNewEventContract.AddNewEventView {

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
    @BindView(R.id.photoEventContainer)
    LinearLayout photoContainer;

    @BindView(R.id.commentAccess)
    TextView commentAccess;
    @BindView(R.id.scaleAccess)
    TextView scaleAccess;
    @BindView(R.id.ratingAccess)
    TextView ratingAccess;
    @BindView(R.id.geopositionAccess)
    TextView geopositionAccess;
    @BindView(R.id.photoAccess)
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
    Double myLatitude = null;
    Double mylongitude = null;


    IWorkWithFIles workWithFIles;
    String photoPath;
    @BindView(R.id.photo)
    ImageView photo;
    AlertDialog.Builder dialog;

    LocationManager locationManager;
    Marker marker;

    TrackingV1 trackingV1;

    Context context;
    Activity activity;

    AddNewEventContract.AddNewEventPresenter addNewEventPresenter;

    boolean flagPhoto = false;

    Location mYlocation;

    String uriPhotoFromCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        ButterKnife.bind(this);
        YandexMetrica.reportEvent("Пользователь вошел в создание события");

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        addNewEventPresenter = new AddNewEventPresenterImpl(sharedPreferences);
        addNewEventPresenter.attachView(this);
        addNewEventPresenter.init(this);
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
                workWithFIles = new WorkWithFiles(getApplication(), context);
                dialog = new AlertDialog.Builder(context);
                dialog.setTitle(R.string.title_dialog_for_photo);
                dialog.setItems(new String[]{"Галлерея", "Фото"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0: {
                                pickGallery();
                                break;
                            }
                            case 1: {
                                pickCamera();
                                break;
                            }
                        }
                    }
                });

                dialog.show();
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewEventPresenter.addNewEvent();
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
                } else {
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
                    addNewEventPresenter.requestPermission(1);
                } else {
                    Location location = getLastKnownLocation();
                    if (trackingV1.GetGeopositionCustomization() == TrackingCustomization.Required) {
                        marker = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                        latitude = marker.getPosition().latitude;
                        longitude = marker.getPosition().longitude;
                        marker.setDraggable(true);
                    }
                    cameraUpdate = CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(8)
                                    .build()
                    );
                    map.moveCamera(cameraUpdate);

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
                            if (marker == null) {
                                marker = map.addMarker(new MarkerOptions().position(latLng));
                                marker.setDraggable(true);
                            } else {
                                marker.setPosition(latLng);
                            }
                            latitude = latLng.latitude;
                            longitude = latLng.longitude;
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
    public void startedConfiguration(UUID trackingId, TrackingV1 trackingV1) {

        eventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleControl.setKeyListener(keyListener);

        this.trackingV1 = trackingV1;
        this.trackingId = trackingId;

        context = this;
        activity = this;

        calculateState();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishAddEventActivity() {
        finish();
        addNewEventPresenter.detachView();
    }

    private void addEvent() {
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
        if (photoState == 2 && photoPath == null) {
            photoFlag = false;
        }

        String comment = null;
        Double scale = null;
        Rating rating = null;


        if (commentFlag && ratingFlag && scaleFlag && geopositionFlag && photoFlag) {
            if (!commentControl.getText().toString().isEmpty() && !commentControl.getText().toString().trim().isEmpty()) {
                comment = commentControl.getText().toString().trim();
            }
            if (!(ratingControl.getRating() == 0)) {
                rating = new Rating((int) (ratingControl.getRating() * 2));
            }
            if (!scaleControl.getText().toString().isEmpty()) {
                try {
                    scale = Double.parseDouble(scaleControl.getText().toString().trim());
                    addNewEventPresenter.saveEvent(new EventV1(UUID.randomUUID(), trackingId, eventDate, scale, rating, comment, latitude, longitude, photoPath), trackingId);
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
                addNewEventPresenter.saveEvent(new EventV1(UUID.randomUUID(), trackingId, eventDate, scale, rating, comment, latitude, longitude, photoPath), trackingId);
                YandexMetrica.reportEvent(getString(R.string.metrica_add_event));


            }
        } else {
            showMessage("Заполните поля с *");
        }

    }

    private void calculateState() {
        commentState = calculateState(trackingV1.GetCommentCustomization());
        ratingState = calculateState(trackingV1.GetRatingCustomization());
        scaleState = calculateState(trackingV1.GetScaleCustomization());
        geopositionState = calculateState(trackingV1.GetGeopositionCustomization());
        photoState = calculateState(trackingV1.GetPhotoCustomization());

    }

    private void calculateUx() {
        calculateUX(commentContainer, commentAccess, commentState);
        calculateUX(ratingContainer, ratingAccess, ratingState);
        calculateUX(scaleContainer, scaleAccess, scaleState);
        calculateUX(geopositionContainer, geopositionAccess, geopositionState);
        calculateUX(photoContainer, photoAccess, photoState);
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(trackingV1.GetTrackingName());
    }

    private void initDate() {
        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());

        dateControl.setText(format.format(eventDate).toString());
    }

    private void initCalenarDialog() {
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

    private void createAndInitMap() {
        if (geopositionState == 1 || geopositionState == 2) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                addNewEventPresenter.requestPermission(1);
            }
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            initMap();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {

            Toast.makeText(getApplicationContext(), "Упс,что-то пошло не так =((((" + "\n" + "Фотографию не удалось загрузить", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == 1) {

            Picasso.get().load(Uri.parse(workWithFIles.getUriPhotoFromCamera())).into(photo);
            photoPath = workWithFIles.saveBitmap(Uri.parse(workWithFIles.getUriPhotoFromCamera()));
            flagPhoto = true;

        }
        if (requestCode == 2) {
            Picasso.get().load(data.getData()).into(photo);
            photoPath = workWithFIles.saveBitmap(data.getData());
            flagPhoto = true;

        }

    }

    private void pickCamera() {
        if (workWithFIles != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            uriPhotoFromCamera = workWithFIles.generateFileUri(1).toString();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, workWithFIles.generateFileUri(1));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(intent, 1);
        }
    }

    private void pickGallery() {
        if (workWithFIles != null) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(intent, 2);
            }
        }
    }


    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                addNewEventPresenter.requestPermission(1);
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}


