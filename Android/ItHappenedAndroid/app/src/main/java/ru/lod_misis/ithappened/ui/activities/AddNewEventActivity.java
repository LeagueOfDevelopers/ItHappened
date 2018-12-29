package ru.lod_misis.ithappened.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
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

import com.squareup.picasso.Picasso;
import com.yandex.metrica.YandexMetrica;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.BuildConfig;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.Rating;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractor;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractorImpl;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.ui.activities.mapactivity.MapActivity;
import ru.lod_misis.ithappened.ui.background.AllId;
import ru.lod_misis.ithappened.ui.background.MyGeopositionService;
import ru.lod_misis.ithappened.ui.background.NotificationJobService;
import ru.lod_misis.ithappened.ui.presenters.AddNewEventContract;


public class AddNewEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AddNewEventContract.AddNewEventView {

    @Inject
    AddNewEventContract.AddNewEventPresenter addNewEventPresenter;
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
    @BindView(R.id.scaleTypeAccess)
    TextView scaleType;
    @BindView(R.id.addEvent)
    Button addEvent;
    @BindView(R.id.adress)
    TextView address;

    //IWorkWithFIles workWithFIles;
    //String photoPath;
    @BindView(R.id.photo)
    ImageView photo;
    private int commentState;
    private int scaleState;
    private int ratingState;
    private int geopositionState;
    private int photoState;
    private UUID trackingId;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    //Date time attrs
    private int eventYear;
    private int eventMonth;
    private int eventDay;
    private boolean timeSetFlag = false;
    private Date eventDate;
    private Double latitude = null;
    private Double longitude = null;
    private PhotoInteractor workWithFIles;
    private String photoPath;
    private TrackingV1 trackingV1;
    private Integer jobId;

    // Время, когда пользователь открыл экран.
    // Нужно для сбора данных о времени, проведенном пользователем на каждом экране
    private DateTime userOpenAnActivityDateTime;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        ButterKnife.bind(this);
        YandexMetrica.reportEvent(getString(R.string.metrica_user_is_adding_new_event));
        trackingId = UUID.fromString(this.getIntent().getStringExtra("trackingId"));
        ItHappenedApplication.getAppComponent().inject(this);
        addNewEventPresenter.attachView(this);
        addNewEventPresenter.init(trackingId);
    }

    @Override
    protected void onStart () {
        super.onStart();
        dateControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                datePickerDialog.create();
                datePickerDialog.show();
            }
        });

        geopositionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if (ActivityCompat.checkSelfPermission(AddNewEventActivity.this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(AddNewEventActivity.this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    addNewEventPresenter.requestPermission(1);
                }
                MapActivity.toMapActivity(AddNewEventActivity.this , 1 , 0 , 0);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                initDialogForPhoto();
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                addEvent();
            }
        });
        scaleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                scaleControl.requestFocus();
                InputMethodManager imm = ( InputMethodManager ) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(scaleControl , InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    private void initDialogForPhoto () {
        workWithFIles = new PhotoInteractorImpl(AddNewEventActivity.this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddNewEventActivity.this);
        dialog.setTitle(R.string.title_dialog_for_photo);
        dialog.setItems(new String[]{"Галлерея" , "Фото"} , new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialogInterface , int i) {
                switch ( i ) {
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

    @Override
    protected void onResume() {
        super.onResume();
        userOpenAnActivityDateTime = DateTime.now();
    }

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_from_add_event));
        Map<String, Object> activityVisitTimeBorders = new HashMap<>();
        activityVisitTimeBorders.put("Start time", userOpenAnActivityDateTime.toDate());
        activityVisitTimeBorders.put("End time", DateTime.now().toDate());
        YandexMetrica.reportEvent(getString(R.string.metrica_user_time_on_activity_add_event), activityVisitTimeBorders);
    }

    @Override
    protected void onStop() {
        super.onStop();
        YandexMetrica.reportEvent(getString(R.string.metrica_user_last_activity_add_event));
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch ( item.getItemId() ) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet (DatePicker datePicker , int year , int month , int day) {
        eventYear = year - 1900;
        eventMonth = month;
        eventDay = day;
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet (TimePicker timePicker , int hour , int minuets) {
        eventDate = new Date(eventYear , eventMonth , eventDay , hour , minuets);
        SimpleDateFormat format = getSimpleDateFormat();
        timeSetFlag = true;
        dateControl.setText(format.format(eventDate));
    }

    @Override
    public void onRequestPermissionsResult (int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {
        Log.d("RequestPermission" , "responseAll");
        super.onRequestPermissionsResult(requestCode , permissions , grantResults);
        switch ( requestCode ) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("RequestPermission" , "RequestYes");
                    stopService(new Intent(this , MyGeopositionService.class));
                    startService(new Intent(this , MyGeopositionService.class));

                } else {
                    onBackPressed();
                }
            }
        }
    }

    @Override
    public void requestPermissionForGeoposition () {
        Log.d("RequestPermission" , "Request");
        ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION} , 1);
    }

    @Override
    public void startConfigurationView () {
        if (trackingV1.getScaleCustomization() != TrackingCustomization.None
                && trackingV1.getScaleName() != null) {
            scaleType.setText(trackingV1.getScaleName());
        }
        calculateUx();
        initToolbar();
        initDate();
        initCalenarDialog();
    }

    @Override
    public void startedConfiguration (TrackingV1 trackingV1) {
        eventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        this.address.setText(R.string.add_geoposition);

        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleControl.setKeyListener(keyListener);

        this.trackingV1 = trackingV1;

        calculateState(trackingV1);
    }

    @Override
    public void showMessage (String message) {
        Toast.makeText(getApplicationContext() , message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishAddEventActivity () {
        addNewEventPresenter.detachView();
        finish();
    }

    @Override
    protected void onActivityResult (int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext() , "Упс,что-то пошло не так =((((" + "\n" + "Фотографию не удалось загрузить" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == 1) {
            Picasso.get().load(Uri.parse(workWithFIles.getUriPhotoFromCamera())).into(photo);
            photoPath = workWithFIles.saveBitmap(Uri.parse(workWithFIles.getUriPhotoFromCamera()));
        }
        if (requestCode == 2) {
            Picasso.get().load(data.getData()).into(photo);
            photoPath = workWithFIles.saveBitmap(data.getData());
        }
        if (requestCode == MapActivity.MAP_ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                latitude = data.getDoubleExtra("latitude" , 0);
                longitude = data.getDoubleExtra("longitude" , 0);
                try {
                    address.setText(getAddress(latitude , longitude));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void addEvent () {
        boolean commentFlag = true;
        boolean scaleFlag = true;
        boolean ratingFlag = true;
        boolean geolocationFlag = true;
        boolean photoFlag = true;
        Double scale;
        String comment = null;
        Rating rating = null;

        if (commentState == 2 && commentControl.getText().toString().isEmpty()) {
            commentFlag = false;
        }

        if (ratingState == 2 && ratingControl.getRating() == 0) {
            ratingFlag = false;
        }

        if (scaleState == 2 && scaleControl.getText().toString().isEmpty()) {
            scaleFlag = false;
        }
        if (geopositionState == 2 && address.getText().toString().equals("Добавить геометку")) {
            geolocationFlag = false;
        }
        if (photoState == 2 && photoPath == null) {
            photoFlag = false;
        }

        if (commentFlag && ratingFlag && scaleFlag && geolocationFlag && photoFlag) {
            if (!commentControl.getText().toString().isEmpty() && !commentControl.getText().toString().trim().isEmpty()) {
                comment = commentControl.getText().toString().trim();
            }
            if (!(ratingControl.getRating() == 0)) {
                rating = new Rating(( int ) (ratingControl.getRating() * 2));
            }
            if (!scaleControl.getText().toString().isEmpty()) {
                try {
                    scale = Double.parseDouble(scaleControl.getText().toString().trim());
                    addNewEventPresenter.saveEvent(new EventV1(UUID.randomUUID() , trackingId , eventDate , scale , rating , comment , latitude , longitude , photoPath) , trackingId);
                    YandexMetrica.reportEvent("Пользователь добавил событие");
                    if (longitude != null && latitude != null) {
                        YandexMetrica.reportEvent(getString(R.string.metrica_user_add_address_to_event));
                    }
                } catch (Exception e) {
                    showMessage("Введите число");
                }
            }
            if (timeSetFlag) {
                SimpleDateFormat simpleDateFormat = getSimpleDateFormat();
                try {
                    eventDate = simpleDateFormat.parse(dateControl.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            addNewEventPresenter.saveEvent(new EventV1(UUID.randomUUID() , trackingId , eventDate , null , rating , comment , latitude , longitude , photoPath) , trackingId);
            YandexMetrica.reportEvent(getString(R.string.metrica_add_event));
            initJobSchedulerForNotifications(trackingV1);
        } else {
            showMessage("Заполните поля с *");
        }

    }

    private void initJobSchedulerForNotifications (TrackingV1 trackingV1) {
        jobId = AllId.addNewValue(trackingV1.getTrackingId());
        JobScheduler jobScheduler = ( JobScheduler ) AddNewEventActivity.this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.cancel(trackingV1.getEventHistory().size() - 1);
        }
        planningNotification(trackingV1);
    }

    private void calculateState (TrackingV1 trackingV1) {
        commentState = convertState(trackingV1.getCommentCustomization());
        ratingState = convertState(trackingV1.getRatingCustomization());
        scaleState = convertState(trackingV1.getScaleCustomization());
        geopositionState = convertState(trackingV1.getGeopositionCustomization());
        photoState = convertState(trackingV1.getPhotoCustomization());
    }

    private void calculateUx () {
        calculateUX(commentContainer , commentAccess , commentState);
        calculateUX(ratingContainer , ratingAccess , ratingState);
        calculateUX(scaleContainer , scaleAccess , scaleState);
        calculateUX(geopositionContainer , geopositionAccess , geopositionState);
        calculateUX(photoContainer , photoAccess , photoState);
    }

    private void initToolbar () {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(trackingV1.getTrackingName());
        }
    }

    private void initDate () {
        SimpleDateFormat format = getSimpleDateFormat();
        dateControl.setText(format.format(eventDate));
    }

    private void initCalenarDialog () {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        datePickerDialog = new DatePickerDialog(
                this ,
                this ,
                calendar.get(Calendar.YEAR) ,
                calendar.get(Calendar.MONTH) ,
                calendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(
                this ,
                this ,
                calendar.get(Calendar.HOUR_OF_DAY) ,
                calendar.get(Calendar.MINUTE) ,
                true);
    }

    private void pickCamera () {
        if (workWithFIles != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT , workWithFIles.generateFileUri(1));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            AddNewEventActivity.this.startActivityForResult(intent , 1);
        }
    }

    private void pickGallery () {
        if (workWithFIles != null) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            if (intent.resolveActivity(AddNewEventActivity.this.getPackageManager()) != null) {
                AddNewEventActivity.this.startActivityForResult(intent , 2);
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

    private Long calculateAverangeTime(TrackingV1 trackingV1){
        int eventCount = 0;
        Date dateOfFirstEvent = Calendar.getInstance(TimeZone.getDefault()).getTime();
        for (EventV1 eventV1 : trackingV1.getEventHistory()) {
            if (!eventV1.isDeleted()) {
                eventCount++;
                if (eventV1.getEventDate().before(dateOfFirstEvent))
                    dateOfFirstEvent = eventV1.getEventDate();
            }
        }
        return (new Date().getTime() - dateOfFirstEvent.getTime()) / eventCount;
    }

    private void planningNotification (TrackingV1 trackingV1) {
        Long averangeTime;
        Long oneDay = ( long ) (1000 * 60 * 60 * 24);
        if (trackingV1.getEventHistory().size() < 10) {
            return;
        }
        averangeTime = calculateAverangeTime(trackingV1);

        if (BuildConfig.DEBUG) {
            createJobScheduler(Long.valueOf(BuildConfig.TEST_PUSH));
        } else {
            if (averangeTime * 2 < oneDay) {
                createJobScheduler(oneDay);
            } else {
                createJobScheduler(averangeTime * 2);
            }
        }
    }

    private void createJobScheduler (Long time) {
        ComponentName notificationJobServiece = new ComponentName(this , NotificationJobService.class);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(jobId , notificationJobServiece);
        jobBuilder.setMinimumLatency(time);
        JobScheduler jobScheduler =
                ( JobScheduler ) AddNewEventActivity.this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(jobBuilder.build());
        }
    }

    private String getAddress (Double latitude , Double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this , Locale.getDefault());
        return geocoder.getFromLocation(latitude , longitude , 1).get(0).getAddressLine(0);
    }

    @NonNull
    private SimpleDateFormat getSimpleDateFormat () {
        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm" , loc);
        format.setTimeZone(TimeZone.getDefault());
        return format;
    }

    private int convertState (TrackingCustomization customization) {
        switch ( customization ) {
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

    @SuppressLint("SetTextI18n")
    private void calculateUX (LinearLayout container , TextView access , int state) {
        switch ( state ) {
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
}


