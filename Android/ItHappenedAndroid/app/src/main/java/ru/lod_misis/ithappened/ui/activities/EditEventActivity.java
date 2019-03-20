package ru.lod_misis.ithappened.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
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

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.domain.models.Rating;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractor;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractorImpl;
import ru.lod_misis.ithappened.domain.statistics.facts.StringParse;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.ui.activities.mapactivity.MapActivity;
import ru.lod_misis.ithappened.ui.fragments.DatePickerFragment;
import ru.lod_misis.ithappened.ui.presenters.EditEventContract;

public class EditEventActivity extends AppCompatActivity implements EditEventContract.EditEventView {

    public static String TRACKING_ID = "trackingId";
    public static String EVENT_ID = "eventId";

    public static void toEditEventActivity(Context context,String trackingId,String eventId){
        Intent intent = new Intent(context, EditEventActivity.class);
        intent.putExtra(TRACKING_ID, trackingId);
        intent.putExtra(EVENT_ID, eventId);
        context.startActivity(intent);
    }
    @Inject
    EditEventContract.EditEventPresenter presenter;
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
    @BindView(R.id.adress)
    TextView adress;
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
    PhotoInteractor photoInteractor;
    String photoPath;
    @BindView(R.id.photo)
    ImageView photo;
    AlertDialog.Builder dialog;
    Boolean flagPhoto = false;
    Double latitude = null;
    Double longitude = null;
    String uriPhotoFromCamera;
    Context context;
    Activity activity;
    private int commentState;
    private int scaleState;
    private int ratingState;
    private int geopositionState;
    private int photoState;

    // Время, когда пользователь открыл экран.
    // Нужно для сбора данных о времени, проведенном пользователем на каждом экране
    private DateTime userOpenAnActivityDateTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        ButterKnife.bind(this);

        context = this;
        activity = this;

        ItHappenedApplication.getAppComponent().inject(this);
        presenter.onViewAttached(this);
        presenter.setIdentificators(UUID.fromString(getIntent().getStringExtra("trackingId")),
                UUID.fromString(getIntent().getStringExtra("eventId")));

        presenter.onViewCreated();

        addEvent.setText("Сохранить");

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_edit_event));

        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleControl.setKeyListener(keyListener);

        dateControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment picker = new DatePickerFragment(dateControl);
                picker.show(fragmentManager, "from");
            }
        });
        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapActivity.toMapActivity(activity, 3, latitude, longitude);
            }
        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addEventClick();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoInteractor = new PhotoInteractorImpl(context);
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

    private void initToolbar(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
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
    public void showEditResult() {
        finish();
    }

    @Override
    public void onViewCreated(TrackingCustomization comment,
                              TrackingCustomization rating,
                              TrackingCustomization scale,
                              TrackingCustomization photo,
                              TrackingCustomization geoposition,
                              Date date,
                              String commentValue,
                              Double scaleValue,
                              Rating ratingValue,
                              Double longitude,
                              Double latitude,
                              String photoPath,
                              String title,
                              String scaleName) {
        initToolbar(title);

        commentState = calculateState(comment);
        ratingState = calculateState(rating);
        scaleState = calculateState(scale);
        geopositionState = calculateState(geoposition);
        photoState = calculateState(photo);

        this.latitude = latitude;
        this.longitude = longitude;

        calculateContainerState(commentContainer, commentAccess, commentState);
        calculateContainerState(ratingContainer, ratingAccess, ratingState);
        calculateContainerState(scaleContainer, scaleAccess, scaleState);
        calculateContainerState(geopositionContainer, geopositionAccess, geopositionState);
        calculateContainerState(photoContainer, photoAccess, photoState);

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());

        dateControl.setText(format.format(date));

        if (scale != TrackingCustomization.None && scaleName != null) {

            scaleType.setText(scaleName);
        }

        if ((scale == TrackingCustomization.Optional
                || scale == TrackingCustomization.Required) && scaleValue != null) {
            scaleControl.setText(StringParse.parseDouble(scaleValue));

            if (scaleName != null) {
                if (scaleName.length() >= 3) {
                    scaleType.setText(scaleName.substring(0, 2));
                } else {
                    scaleType.setText(scaleName);
                }
            }
        }

        if ((rating == TrackingCustomization.Optional
                || rating == TrackingCustomization.Required) && ratingValue != null) {
            ratingControl.setRating(ratingValue.getRating() / 2.0f);
        }

        if ((comment == TrackingCustomization.Optional
                || comment == TrackingCustomization.Required) && commentValue != null) {
            commentControl.setText(commentValue);
        }

        if ((geoposition == TrackingCustomization.Optional
                || geoposition == TrackingCustomization.Required)) {
            if (longitude != null && latitude != null) {
                try {
                    adress.setText(getAddress(latitude, longitude));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if ((photo == TrackingCustomization.Optional
                || photo == TrackingCustomization.Required)) {
            photoInteractor = new PhotoInteractorImpl(this);
            if (photoPath != null) {
                this.photoPath = photoPath;
                this.photo.setImageBitmap(photoInteractor.loadImage(photoPath));
            }
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
    protected void onResume() {
        super.onResume();
        userOpenAnActivityDateTime = DateTime.now();
    }

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_edit_event));
        Map<String, Object> activityVisitTimeBorders = new HashMap<>();
        activityVisitTimeBorders.put("Start time", userOpenAnActivityDateTime.toDate());
        activityVisitTimeBorders.put("End time", DateTime.now().toDate());
        YandexMetrica.reportEvent(getString(R.string.metrica_user_time_on_activity_edit_event), activityVisitTimeBorders);
    }

    @Override
    protected void onStop() {
        super.onStop();
        YandexMetrica.reportEvent(getString(R.string.metrica_user_last_activity_edit_event));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reportEvent(int resourceId) {
        YandexMetrica.reportEvent(getString(resourceId));
    }

    @Override
    public void addEvent() {
        String comment = null;
        Double scale = null;
        Rating rating = null;
        Date eventDate;

        boolean commentFlag = true;
        boolean scaleFlag = true;
        boolean ratingFlag = true;
        boolean geopositionFlag = true;
        boolean photoFlag = true;

        if (commentState == 2 && commentControl.getText().toString().isEmpty() && commentControl.getText().toString().trim().isEmpty()) {
            commentFlag = false;
        }

        if (ratingState == 2 && ratingControl.getRating() == 0) {
            ratingFlag = false;
        }

        if (scaleState == 2 && scaleControl.getText().toString().isEmpty() && scaleControl.getText().toString().trim().isEmpty()) {
            scaleFlag = false;
        }

        if (geopositionState == 2 && adress.getText().toString().trim().isEmpty()) {
            geopositionFlag = false;
        }
        if (photoState == 2 && photoPath.isEmpty())
            photoFlag = false;

        if (commentFlag && ratingFlag && scaleFlag && geopositionFlag && photoFlag) {
            if (!commentControl.getText().toString().trim().isEmpty())
                comment = commentControl.getText().toString().trim();
            if (!scaleControl.getText().toString().trim().isEmpty())
                scale = Double.valueOf(scaleControl.getText().toString());
            if (ratingControl.getRating() != 0f)
                rating = new Rating((int)(ratingControl.getRating()*2));
            try {
                eventDate = getDate();
            } catch (ParseException e) {
                e.printStackTrace();
                showMessage(getString(R.string.error_message));
                return;
            }
            showMessage(getString(R.string.edit_event_success));
            presenter.finish(scale, rating, comment,
                    latitude, longitude, photoPath, eventDate);
            reportEvent(R.string.metrica_edit_event);
            showEditResult();
        } else {
            showMessage("Заполните поля с *");
        }
    }

    private Date getDate() throws ParseException {

        Date eventDate = null;
        Locale locale = new Locale("ru");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        eventDate = simpleDateFormat.parse(dateControl.getText().toString());
        return eventDate;

    }

    private void finishActivity() {
        this.finish();
    }

    private void calculateContainerState(LinearLayout container, TextView access, int state) {
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
        if (requestCode == 1) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Фотографию не удалось загрузить", Toast.LENGTH_SHORT).show();
                return;
            }
            Picasso.get().load(Uri.parse(photoInteractor.getUriPhotoFromCamera())).into(photo);
            photoPath = photoInteractor.saveBitmap(Uri.parse(photoInteractor.getUriPhotoFromCamera()));
            flagPhoto = true;
        }
        if (requestCode == 2) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Фотографию не удалось загрузить", Toast.LENGTH_SHORT).show();
                return;
            }
            Picasso.get().load(data.getData()).into(photo);
            photoPath = photoInteractor.saveBitmap(data.getData());
            flagPhoto = true;
        }
        if (requestCode == MapActivity.MAP_ACTIVITY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Не удалось определить ваше местоположение", Toast.LENGTH_SHORT).show();
                return;
            }
            if (data != null) {
                latitude = data.getDoubleExtra("latitude", 0);
                longitude = data.getDoubleExtra("longitude", 0);
                try {
                    adress.setText(getAddress(latitude, longitude));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void pickCamera() {
        if (photoInteractor != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            uriPhotoFromCamera = photoInteractor.generateFileUri(1).toString();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoInteractor.generateFileUri(1));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(intent, 1);
        }
    }

    private void pickGallery() {
        if (photoInteractor != null) {
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
