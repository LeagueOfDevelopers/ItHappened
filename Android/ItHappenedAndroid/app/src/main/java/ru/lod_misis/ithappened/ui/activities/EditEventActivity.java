package ru.lod_misis.ithappened.ui.activities;

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
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.domain.models.Rating;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractor;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractorImpl;
import ru.lod_misis.ithappened.domain.statistics.facts.StringParse;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.ui.activities.mapactivity.MapActivity;
import ru.lod_misis.ithappened.ui.fragments.DatePickerFragment;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.ui.presenters.EditEventContract;

public class EditEventActivity extends AppCompatActivity implements EditEventContract.EditEventView {

    @Inject
    EditEventContract.EditEventPresenter presenter;

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
    TextView adress;

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

    PhotoInteractor workWithFIles;
    String photoPath;
    @BindView(R.id.photoEdit)
    ImageView photo;
    AlertDialog.Builder dialog;
    Boolean flagPhoto = false;
    Boolean flagGeoposition = false;

    LocationManager locationManager;
    Double latitude = null;
    Double longitude = null;

    String uriPhotoFromCamera;

    Context context;
    Activity activity;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ButterKnife.bind(this);

        context = this;
        activity = this;

        ItHappenedApplication.getAppComponent().inject(this);
        presenter.onViewAttached(this);
        presenter.setIdentificators(UUID.fromString(getIntent().getStringExtra("trackingId")) ,
                UUID.fromString(getIntent().getStringExtra("eventId")));

        presenter.onViewCreated();

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_edit_event));

        locationManager = ( LocationManager ) getSystemService(LOCATION_SERVICE);
        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleControl.setKeyListener(keyListener);

        dateControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment picker = new DatePickerFragment(dateControl);
                picker.show(fragmentManager , "from");
            }
        });
        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                MapActivity.toMapActivity(activity , 3,latitude,longitude);
            }
        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                presenter.addEventClick();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                workWithFIles = new PhotoInteractorImpl(context);
                dialog = new AlertDialog.Builder(context);
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
        });

    }

    private void initToolbar (String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
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
    public void showEditResult () {
        finish();
    }

    @Override
    public void onViewCreated (TrackingCustomization comment ,
                               TrackingCustomization rating ,
                               TrackingCustomization scale ,
                               TrackingCustomization photo ,
                               TrackingCustomization geoposition ,
                               Date date ,
                               String commentValue ,
                               Double scaleValue ,
                               Rating ratingValue ,
                               Double longitude ,
                               Double latitude ,
                               String photoPath ,
                               String title ,
                               String scaleName) {
        initToolbar(title);

        commentState = calculateState(comment);
        ratingState = calculateState(rating);
        scaleState = calculateState(scale);
        geopositionState = calculateState(geoposition);
        photoState = calculateState(photo);

        this.latitude=latitude;
        this.longitude=longitude;

        calculateContainerState(commentContainer , commentAccess , commentState);
        calculateContainerState(ratingContainer , ratingAccess , ratingState);
        calculateContainerState(scaleContainer , scaleAccess , scaleState);
        calculateContainerState(geopositionContainer , geopositionAccess , geopositionState);
        calculateContainerState(photoContainer , photoAccess , photoState);

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm" , loc);
        format.setTimeZone(TimeZone.getDefault());

        dateControl.setText(format.format(date));

        if ( scale != TrackingCustomization.None && scaleName != null ) {

            scaleType.setText(scaleName);
        }

        if ( (scale == TrackingCustomization.Optional
                || scale == TrackingCustomization.Required) && scaleValue != null ) {
            scaleControl.setText(StringParse.parseDouble(scaleValue));

            if ( scaleName != null ) {
                if ( scaleName.length() >= 3 ) {
                    scaleType.setText(scaleName.substring(0 , 2));
                } else {
                    scaleType.setText(scaleName);
                }
            }
        }

        if ( (rating == TrackingCustomization.Optional
                || rating == TrackingCustomization.Required) && ratingValue != null ) {
            ratingControl.setRating(ratingValue.getRating() / 2.0f);
        }

        if ( (comment == TrackingCustomization.Optional
                || comment == TrackingCustomization.Required) && commentValue != null ) {
            commentControl.setText(commentValue);
        }

        if ( (geoposition == TrackingCustomization.Optional
                || geoposition == TrackingCustomization.Required) ) {
            if ( longitude != null && latitude != null ) {
                try {
                    adress.setText(getAddress(latitude , longitude));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if ( (photo == TrackingCustomization.Optional
                || photo == TrackingCustomization.Required) ) {
            workWithFIles = new PhotoInteractorImpl(this);
            this.photo.setImageBitmap(workWithFIles.loadImage(photoPath));
        }

    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
    }

    private int calculateState (TrackingCustomization customization) {
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

    @Override
    protected void onPause () {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_edit_event));
    }

    @Override
    public void showMessage (String message) {
        Toast.makeText(this , message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reportEvent (int resourceId) {
        YandexMetrica.reportEvent(getString(resourceId));
    }

    @Override
    public void addEvent () {
        boolean commentFlag = true;
        boolean scaleFlag = true;
        boolean ratingFlag = true;
        boolean geopositionFlag = true;

        if ( commentState == 2 && commentControl.getText().toString().isEmpty() ) {
            commentFlag = false;
        }

        if ( ratingState == 2 && ratingControl.getRating() == 0 ) {
            ratingFlag = false;
        }

        if ( scaleState == 2 && scaleControl.getText().toString() != null ) {
            scaleFlag = false;
        }

        if ( geopositionState == 2 && !flagGeoposition ) {
            geopositionFlag = false;
        }

        String comment = null;
        Double scale = null;
        Rating rating = null;
        Date eventDate = null;


        if ( commentFlag && ratingFlag && scaleFlag && geopositionFlag ) {
            if ( !commentControl.getText().toString().isEmpty() && !commentControl.getText().toString().trim().isEmpty() ) {
                comment = commentControl.getText().toString().trim();
            }
            if ( !(rating == null) ) {
                rating = new Rating(( int ) (rating.getRating()));
            }
            if ( scale != null ) {
                try {
                    scale = scale;
                    Locale locale = new Locale("ru");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm" , locale);
                    simpleDateFormat.setTimeZone(TimeZone.getDefault());
                    try {
                        eventDate = simpleDateFormat.parse(dateControl.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    showMessage("Событие изменено");
                    showEditResult();
                } catch (Exception e) {
                    showMessage("Введите число");
                }
            } else {

                Locale locale = new Locale("ru");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm" , locale);
                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                try {
                    eventDate = simpleDateFormat.parse(dateControl.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                showMessage("Событие изменено");
                showEditResult();

            }
            presenter.finish(scale , rating , comment ,
                    latitude , longitude , photoPath , eventDate);
            reportEvent(R.string.metrica_edit_event);
        } else {
            showMessage("Заполните поля с *");
        }
    }

    private void finishActivity () {
        this.finish();
    }

    private void calculateContainerState (LinearLayout container , TextView access , int state) {
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

    @Override
    protected void onActivityResult (int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        if ( resultCode != RESULT_OK ) {
            Toast.makeText(getApplicationContext() , "Упс,что-то пошло не так =((((" , Toast.LENGTH_SHORT).show();
            return;
        }
        if ( requestCode == 1 ) {
            Picasso.get().load(Uri.parse(workWithFIles.getUriPhotoFromCamera())).into(photo);
            photoPath = workWithFIles.saveBitmap(Uri.parse(workWithFIles.getUriPhotoFromCamera()));
            flagPhoto = true;
        }
        if ( requestCode == 2 ) {
            Picasso.get().load(data.getData()).into(photo);
            photoPath = workWithFIles.saveBitmap(data.getData());
            flagPhoto = true;
        }
        if ( requestCode == MapActivity.MAP_ACTIVITY_REQUEST_CODE ) {
            latitude = data.getDoubleExtra("latitude" , 0);
            longitude = data.getDoubleExtra("longitude" , 0);
            try {
                adress.setText(getAddress(latitude,longitude));
            } catch (IOException e) {
                e.printStackTrace();
            }
            flagGeoposition = true;
        }
    }

    private void pickCamera () {
        if ( workWithFIles != null ) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            uriPhotoFromCamera = workWithFIles.generateFileUri(1).toString();
            intent.putExtra(MediaStore.EXTRA_OUTPUT , workWithFIles.generateFileUri(1));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(intent , 1);
        }
    }

    private void pickGallery () {
        if ( workWithFIles != null ) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            if ( intent.resolveActivity(activity.getPackageManager()) != null ) {
                activity.startActivityForResult(intent , 2);
            }
        }
    }

    private String getAddress (Double latitude , Double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this , Locale.getDefault());
        return geocoder.getFromLocation(latitude , longitude , 1).get(0).getAddressLine(0);
    }
}
