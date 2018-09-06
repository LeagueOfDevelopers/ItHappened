package ru.lod_misis.ithappened.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Dialog.ChoicePhotoDialog;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Fragments.DatePickerFragment;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.FactCalculator;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;

import ru.lod_misis.ithappened.Utils.UserDataUtils;

import ru.lod_misis.ithappened.WorkWithFiles.IWorkWithFIles;
import ru.lod_misis.ithappened.WorkWithFiles.WorkWithFiles;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EditEventActivity extends AppCompatActivity {

    TrackingService trackingService;
    InMemoryFactRepository factRepository;
    ITrackingRepository trackingCollection;

    int commentState;
    int scaleState;
    int ratingState;
    int geopositionState;
    int photoState;

    UUID trackingId;
    UUID eventId;

    @BindView(R.id.commentEventContainerEdit)
    LinearLayout commentContainer;
    @BindView(R.id.scaleEventContainerEdit)
    LinearLayout scaleContainer;
    @BindView(R.id.ratingEventContainerEdit)
    LinearLayout ratingContainer;
    @BindView(R.id.geopositionEventContainerEdit)
    LinearLayout geopositionContainer;
    @BindView(R.id.photoEventContainerEdit)
    LinearLayout photoContainer;

    @BindView(R.id.commentAccessEdit)
    TextView commentAccess;
    @BindView(R.id.scaleAccessEdit)
    TextView scaleAccess;
    @BindView(R.id.ratingAccessEdit)
    TextView ratingAccess;
    @BindView(R.id.geopositionAccess)
    TextView geopositionAccess;
    @BindView(R.id.photoAccessEdit)
    TextView photoAccess;

    @BindView(R.id.eventCommentControlEdit)
    EditText commentControl;
    @BindView(R.id.eventScaleControlEdit)
    EditText scaleControl;
    @BindView(R.id.ratingEventControlEdit)
    RatingBar ratingControl;
    @BindView(R.id.eventDateControlEdit)
    Button dateControl;

    @BindView(R.id.scaleTypeAccessEdit)
    TextView scaleType;
    @BindView(R.id.editEvent)
    Button addEvent;

    IWorkWithFIles workWithFIles;
    String photoPath;
    @BindView(R.id.photoEdit)
    ImageView photo;
    ChoicePhotoDialog dialog;
    Boolean flagPhoto=false;

    TrackingV1 trackingV1;
    EventV1 eventV1;

    SupportMapFragment supportMapFragment;
    GoogleMap map;
    Marker marker;
    LocationManager locationManager;
    Double latitude = null;
    Double longitude = null;

    Context context;
    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ButterKnife.bind(this);
        context=this;
        activity=this;

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_edit_event));

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        trackingCollection=UserDataUtils.setUserDataSet(sharedPreferences);
        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), trackingCollection);

        factRepository = StaticFactRepository.getInstance();

        trackingId = UUID.fromString(getIntent().getStringExtra("trackingId"));
        eventId = UUID.fromString(getIntent().getStringExtra("eventId"));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleControl.setKeyListener(keyListener);


        trackingV1 = trackingCollection.GetTracking(trackingId);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(trackingV1.GetTrackingName());

        eventV1 = trackingV1.GetEvent(eventId);

        commentState = calculateState(trackingV1.GetCommentCustomization());
        ratingState = calculateState(trackingV1.GetRatingCustomization());
        scaleState = calculateState(trackingV1.GetScaleCustomization());
        geopositionState = calculateState(trackingV1.GetGeopositionCustomization());
        photoState=calculateState(trackingV1.GetPhotoCustomization());
        photoContainer.setVisibility(View.VISIBLE);
        calculateUX(commentContainer, commentAccess, commentState);
        calculateUX(ratingContainer, ratingAccess, ratingState);
        calculateUX(scaleContainer, scaleAccess, scaleState);
        calculateUX(geopositionContainer, geopositionAccess, geopositionState);
        calculateUX(photoContainer,photoAccess,photoState);

        if (trackingV1.GetScaleCustomization() != TrackingCustomization.None && trackingV1.getScaleName() != null) {
            scaleType.setText(trackingV1.getScaleName());
        }

        Date thisDate = eventV1.GetEventDate();

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());

        if ((trackingV1.GetScaleCustomization() == TrackingCustomization.Optional
                || trackingV1.GetScaleCustomization() == TrackingCustomization.Required) && eventV1.GetScale() != null) {
            scaleControl.setText(StringParse.parseDouble(eventV1.GetScale().doubleValue()));
            if (trackingV1.getScaleName() != null) {
                if (trackingV1.getScaleName().length() >= 3) {
                    scaleType.setText(trackingV1.getScaleName().substring(0, 2));
                } else {
                    scaleType.setText(trackingV1.getScaleName());
                }
            }
        }

        if ((trackingV1.GetRatingCustomization() == TrackingCustomization.Optional
                || trackingV1.GetRatingCustomization() == TrackingCustomization.Required) && eventV1.GetRating() != null) {
            ratingControl.setRating(eventV1.GetRating().getRating() / 2.0f);
        }

        if ((trackingV1.GetCommentCustomization() == TrackingCustomization.Optional
                || trackingV1.GetCommentCustomization() == TrackingCustomization.Required) && eventV1.GetComment() != null) {
            commentControl.setText(eventV1.GetComment());
        }
        if ((trackingV1.GetGeopositionCustomization() == TrackingCustomization.Optional
                || trackingV1.GetGeopositionCustomization() == TrackingCustomization.Required)) {
            mapInit();
        }


        dateControl.setText(format.format(thisDate).toString());


        dateControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment picker = new DatePickerFragment(dateControl);
                picker.show(fragmentManager, "from");
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if(photoState == 2 && !flagPhoto){
                    photoFlag = false;
                }

                String comment = null;
                Double scale = null;
                Rating rating = null;


                if (commentFlag && ratingFlag && scaleFlag && geopositionFlag) {
                    if (!commentControl.getText().toString().isEmpty() && !commentControl.getText().toString().trim().isEmpty()) {
                        comment = commentControl.getText().toString().trim();
                    }
                    if (!(ratingControl.getRating() == 0)) {
                        rating = new Rating((int) (ratingControl.getRating() * 2));
                    }
                    if (!scaleControl.getText().toString().isEmpty()) {
                        try {
                            scale = Double.parseDouble(scaleControl.getText().toString());
                            Date eventDate = null;
                            Locale locale = new Locale("ru");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
                            simpleDateFormat.setTimeZone(TimeZone.getDefault());
                            try {
                                eventDate = simpleDateFormat.parse(dateControl.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            trackingService.EditEvent(trackingId, eventId, scale, rating, comment, latitude, longitude,photoPath, eventDate);
                            YandexMetrica.reportEvent(getString(R.string.metrica_edit_event));
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
                            Toast.makeText(getApplicationContext(), "Событие изменено", Toast.LENGTH_SHORT).show();
                            finishActivity();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Введите число", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Date eventDate = null;
                        Locale locale = new Locale("ru");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
                        simpleDateFormat.setTimeZone(TimeZone.getDefault());
                        try {
                            eventDate = simpleDateFormat.parse(dateControl.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        trackingService.EditEvent(trackingId, eventId, scale, rating, comment, latitude, longitude,photoPath,eventDate);
                        YandexMetrica.reportEvent(getString(R.string.metrica_edit_event));
                        new FactCalculator(trackingCollection).calculateFactsForAddNewEventActivity(trackingId);
                        Toast.makeText(getApplicationContext(), "Событие изменено", Toast.LENGTH_SHORT).show();
                        finishActivity();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Заполните поля с *", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_edit_event));
    }

    private void finishActivity() {
        this.finish();
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

    private void mapInit() {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                if (eventV1.getLotitude() != null && eventV1.getLongitude() != null) {
                    marker = map.addMarker(new MarkerOptions().position(new LatLng(eventV1.getLotitude(), eventV1.getLongitude())));
                    moveCamera(eventV1.getLotitude(), eventV1.getLongitude());
                } else {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    marker = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                    moveCamera(location.getLatitude(), location.getLongitude());

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
                        latitude = marker.getPosition().latitude;
                        longitude = marker.getPosition().longitude;
                    }
                });
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        marker.setPosition(latLng);
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;
                    }
                });
            }
        });
    }

    private void moveCamera(Double latitude, Double longitude) {
        CameraUpdate cameraUpdate;
        cameraUpdate = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude))
                        .zoom(5)
                        .build()
        );
        map.moveCamera(cameraUpdate);
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
