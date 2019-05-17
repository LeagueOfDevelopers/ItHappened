package ru.lod_misis.ithappened.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yandex.metrica.YandexMetrica;

import org.joda.time.DateTime;

import java.io.IOException;
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
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractor;
import ru.lod_misis.ithappened.domain.photointeractor.PhotoInteractorImpl;
import ru.lod_misis.ithappened.domain.statistics.facts.StringParse;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.ui.activities.mapactivity.MapActivity;
import ru.lod_misis.ithappened.ui.dialog.DeleteEventDialog;
import ru.lod_misis.ithappened.ui.presenters.DeleteCallback;
import ru.lod_misis.ithappened.ui.presenters.DeleteContract;
import ru.lod_misis.ithappened.ui.presenters.EventDetailsContract;
import ru.lod_misis.ithappened.ui.utils.WorkWithDecimal;


public class EventDetailsActivity extends AppCompatActivity implements DeleteContract.DeleteView, EventDetailsContract.EventDetailsView, DeleteCallback {

    public static String TRACKING_ID = "trackingId";
    public static String EVENT_ID = "eventId";

    @BindView(R.id.backButton)
    ImageView backButton;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.editEventButton)
    Button editEvent;
    @BindView(R.id.deleteEventButton)
    Button deleteEvent;

    private UUID trackingId;
    private UUID eventId;

    @BindView(R.id.valuesCard)
    RelativeLayout valuesCard;
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
    @BindView(R.id.geoposition_logo)
    ImageView geopositionLogo;
    @BindView(R.id.adress)
    TextView adress;
    @BindView(R.id.ratingValue)
    RatingBar ratingValue;

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.container_with_content)
    RelativeLayout conteinerWithContent;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    private Double latitude;
    private Double longitude;
    private PhotoInteractor workWithFIles;
    private TrackingV1 thisTrackingV1;
    private EventV1 thisEventV1;
    private Date thisDate;
    private SimpleDateFormat format;


    private Intent intent;
    private Bitmap bitmap;
    // Время, когда пользователь открыл экран.
    // Нужно для сбора данных о времени, проведенном пользователем на каждом экране
    private DateTime userOpenAnActivityDateTime;

    @Inject
    DeleteContract.DeletePresenter deletePresenter;
    @Inject
    EventDetailsContract.EventDetailsPresenter eventDetailsPresenter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        intent = getIntent();
        ButterKnife.bind(this);
        workWithFIles = new PhotoInteractorImpl(this);

        ItHappenedApplication.getAppComponent().inject(this);

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_event_details));
        eventDetailsPresenter.attachView(this);
        deletePresenter.attachView(this);
        this.trackingId = UUID.fromString(getIntent().getStringExtra("trackingId"));
        this.eventId = UUID.fromString(getIntent().getStringExtra("eventId"));

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEvent(trackingId, eventId);
            }
        });
        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YandexMetrica.reportEvent(getString(R.string.metrica_user_use_address_button));
                MapActivity.toMapActivity(EventDetailsActivity.this, 2, latitude, longitude);
            }
        });
        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEvent(trackingId, eventId);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventDetailsActivity.this.finish();
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(photo, thisEventV1.getPhoto());

            }
        });

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventDetailsPresenter.init(trackingId);
        userOpenAnActivityDateTime = DateTime.now();
    }

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_event_details));
        Map<String, Object> activityVisitTimeBorders = new HashMap<>();
        activityVisitTimeBorders.put("Start time", userOpenAnActivityDateTime.toDate());
        activityVisitTimeBorders.put("End time", DateTime.now().toDate());
        YandexMetrica.reportEvent(getString(R.string.metrica_user_time_on_activity_event_details), activityVisitTimeBorders);
    }

    @Override
    protected void onStop() {
        super.onStop();
        YandexMetrica.reportEvent(getString(R.string.metrica_user_last_activity_event_details));
    }

    /* @Override
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
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void startConfigurationView() {
        title.setText(thisTrackingV1.getTrackingName());
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
            scaleValue.setText(WorkWithDecimal.makeDecimalWithMantisa(thisEventV1.getScale()) + " " + thisTrackingV1.getScaleName());
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
                adress.setText(getAddress(this.latitude, this.longitude));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            adress.setVisibility(View.GONE);
            geopositionLogo.setVisibility(View.GONE);
        }
        if (thisEventV1.getPhoto() != null) {
            Glide.with(this).load(thisEventV1.getPhoto()).into(photo);
            nullsCard.setVisibility(View.GONE);
        } else {
            float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64f, getResources().getDisplayMetrics());
            photo.setBackgroundColor(getResources().getColor(R.color.white));
            photo.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(height)));
            photo.setVisibility(View.INVISIBLE);
            title.setTextColor(getResources().getColor(R.color.black));
            backButton.setImageResource(R.drawable.ic_arraow_back_black_24dp);
        }
    }


    @Override
    public void startedConfiguration(TrackingV1 tracking) {
        setTitle(tracking.getTrackingName());
        thisEventV1 = tracking.getEvent(eventId);
        thisTrackingV1 = tracking;
        thisEventV1 = thisTrackingV1.getEvent(eventId);
        thisDate = thisEventV1.getEventDate();

        Locale loc = new Locale("ru");
        format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishDetailsEventActivity() {
        deletePresenter.detachView();
        eventDetailsPresenter.detachView();
        onBackPressed();
    }

    public void deleteEvent(UUID trackingId, UUID eventId) {
        DeleteEventDialog delete = new DeleteEventDialog();
        delete.setDeleteCallback(this);
        Bundle bundle = new Bundle();
        bundle.putString(TRACKING_ID, trackingId.toString());
        bundle.putString(EVENT_ID, eventId.toString());
        delete.setArguments(bundle);
        delete.show(getFragmentManager(), "DeleteEvent");
    }

    private void editEvent(UUID trackingId, UUID eventId) {
        EditEventActivity.toEditEventActivity(this, trackingId.toString(), eventId.toString());
        deletePresenter.detachView();
    }

    private String getAddress(Double latitude, Double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        return geocoder.getFromLocation(latitude, longitude, 1).get(0).getAddressLine(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MapActivity.MAP_ACTIVITY_REQUEST_CODE)
            if (resultCode != RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Фотографию не удалось загрузить", Toast.LENGTH_SHORT).show();
                return;
            }
        if (resultCode == RESULT_OK)
            super.onActivityResult(requestCode, resultCode, data);
    }

    //Это анимация скопировал с документации,из-за нехватки времени
    private void zoomImageFromThumb(final View thumbView, String imageResId) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        Glide.with(this).load(imageResId).into(expandedImageView);
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);
        conteinerWithContent.setVisibility(View.GONE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        conteinerWithContent.setVisibility(View.VISIBLE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        conteinerWithContent.setVisibility(View.VISIBLE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    @Override
    public void finishDeleting(UUID trackingId, UUID eventId) {
        deletePresenter.okClicked(trackingId, eventId);
    }

    @Override
    public void cansel() {
        deletePresenter.canselClicked();
    }



}
