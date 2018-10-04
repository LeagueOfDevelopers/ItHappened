package ru.lod_misis.ithappened.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

import com.squareup.picasso.Picasso;
import com.yandex.metrica.YandexMetrica;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.Activities.MapActivity.MapActivity;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Fragments.DatePickerFragment;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Retrofit.ItHappenedApplication;
import ru.lod_misis.ithappened.Statistics.FactCalculator;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;
import ru.lod_misis.ithappened.WorkWithFiles.IWorkWithFIles;
import ru.lod_misis.ithappened.WorkWithFiles.WorkWithFiles;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EditEventActivity extends AppCompatActivity {

    @Inject
    TrackingService trackingService;
    @Inject
    InMemoryFactRepository factRepository;
    @Inject
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
    @BindView(R.id.adress)
    TextView address;

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
    AlertDialog.Builder dialog;
    Boolean flagPhoto = false;

    TrackingV1 trackingV1;
    EventV1 eventV1;


    LocationManager locationManager;
    Double latitude = null;
    Double longitude = null;

    String uriPhotoFromCamera;

    Context context;
    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ButterKnife.bind(this);

        context = this;
        activity = this;

        ItHappenedApplication.getAppComponent().inject(this);

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_edit_event));

        trackingId = UUID.fromString(getIntent().getStringExtra("trackingId"));
        eventId = UUID.fromString(getIntent().getStringExtra("eventId"));
        latitude = Double.valueOf(getIntent().getStringExtra("latitude"));
        longitude = Double.valueOf(getIntent().getStringExtra("longitude"));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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
        photoState = calculateState(trackingV1.GetPhotoCustomization());

        calculateUX(commentContainer, commentAccess, commentState);
        calculateUX(ratingContainer, ratingAccess, ratingState);
        calculateUX(scaleContainer, scaleAccess, scaleState);
        calculateUX(geopositionContainer, geopositionAccess, geopositionState);
        calculateUX(photoContainer, photoAccess, photoState);

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
            if (longitude != null && latitude != null) {
                try {
                    address.setText(getAddress(latitude,longitude));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    address.setText(getAddress(eventV1.getLotitude(),eventV1.getLongitude()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if ((trackingV1.GetPhotoCustomization() == TrackingCustomization.Optional
                || trackingV1.GetPhotoCustomization() == TrackingCustomization.Required)) {
            workWithFIles = new WorkWithFiles(getApplication(), this);
            photo.setImageBitmap(workWithFIles.loadImage(eventV1.getPhoto()));
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
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> fields = new ArrayList<>();
                fields.add("3");
                fields.add(trackingV1.GetTrackingID().toString());
                fields.add(eventId.toString());
                fields.add(eventV1.getLotitude().toString());
                fields.add(eventV1.getLongitude().toString());
                MapActivity.toMapActivity(activity, fields);
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
                if (photoState == 2 && !flagPhoto) {
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
                            trackingService.EditEvent(trackingId, eventId, scale, rating, comment, latitude, longitude, photoPath, eventDate);
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
                        trackingService.EditEvent(trackingId, eventId, scale, rating, comment, latitude, longitude, photoPath, eventDate);
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

    private String getAddress(Double latitude, Double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        return geocoder.getFromLocation(latitude, longitude, 1).get(0).getAddressLine(0);
    }
}
