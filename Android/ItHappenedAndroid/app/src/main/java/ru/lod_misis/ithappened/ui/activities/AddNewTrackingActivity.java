package ru.lod_misis.ithappened.ui.activities;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thebluealliance.spectrum.SpectrumDialog;
import com.yandex.metrica.YandexMetrica;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.ui.background.GeopositionService;
import ru.lod_misis.ithappened.ui.background.NotificationJobService;
import ru.lod_misis.ithappened.ui.presenters.BillingPresenter;
import ru.lod_misis.ithappened.ui.presenters.BillingView;
import ru.lod_misis.ithappened.ui.presenters.CreateTrackingContract;

public class AddNewTrackingActivity extends AppCompatActivity implements CreateTrackingContract.CreateTrackingView, BillingView {

    public static final int GEO_REQUEST_CODE = 1;
    @Inject
    CreateTrackingContract.CreateTrackingPresenter createTrackingPresenter;

    @BindView(R.id.editTitleOfTracking)
    EditText trackingName;

    @BindView(R.id.ratingTextEnabled)
    TextView ratingEnabled;
    @BindView(R.id.commentTextEnabled)
    TextView commentEnabled;
    @BindView(R.id.scaleTextEnabled)
    TextView scaleEnabled;
    @BindView(R.id.geopositionTextEnabled)
    TextView geopositionEnabled;
    @BindView(R.id.photoTextEnabled)
    TextView photoEnabled;

    @BindView(R.id.colorPicker)
    CardView colorPickerButton;
    @BindView(R.id.colorText)
    TextView colorPickerText;

    @BindView(R.id.ratingBackColorDont)
    LinearLayout ratingDont;
    @BindView(R.id.ratingBackColorCheck)
    LinearLayout ratingOptional;
    @BindView(R.id.ratingBackColorDoubleCheck)
    LinearLayout ratingRequired;

    @BindView(R.id.commentBackColorDont)
    LinearLayout commentDont;
    @BindView(R.id.commentBackColorCheck)
    LinearLayout commentOptional;
    @BindView(R.id.commentBackColorDoubleCheck)
    LinearLayout commentRequired;


    @BindView(R.id.scaleBackColorDont)
    LinearLayout scaleDont;
    @BindView(R.id.scaleBackColorCheck)
    LinearLayout scaleOptional;
    @BindView(R.id.scaleBackColorDoubleCheck)
    LinearLayout scaleRequired;

    @BindView(R.id.geopositionBackColorDont)
    LinearLayout geopositionDont;
    @BindView(R.id.geopositionBackColorCheck)
    LinearLayout geopositionOptional;
    @BindView(R.id.geopositionBackColorDoubleCheck)
    LinearLayout geopositionRequired;

    @BindView(R.id.photoBackColorDont)
    LinearLayout photoDont;
    @BindView(R.id.photoBackColorCheck)
    LinearLayout photoOptional;
    @BindView(R.id.photoBackColorDoubleCheck)
    LinearLayout photoRequired;

    @BindView(R.id.ratingBackImageDont)
    ImageView ratingDontImage;
    @BindView(R.id.ratingBackImageCheck)
    ImageView ratingOptionalImage;
    @BindView(R.id.ratingBackImageDoubleCheck)
    ImageView ratingRequiredImage;

    @BindView(R.id.commentBackImageDont)
    ImageView commentDontImage;
    @BindView(R.id.commentBackImageCheck)
    ImageView commentOptionalImage;
    @BindView(R.id.commentBackImageDoubleCheck)
    ImageView commentRequiredImage;

    @BindView(R.id.scaleBackImageDont)
    ImageView scaleDontImage;
    @BindView(R.id.scaleBackImageCheck)
    ImageView scaleOptionalImage;
    @BindView(R.id.scaleBackImageDoubleCheck)
    ImageView scaleRequiredImage;

    @BindView(R.id.geopositionBackImageDont)
    ImageView geopositionDontImage;
    @BindView(R.id.geopositionBackImageCheck)
    ImageView geopositionOptionalImage;
    @BindView(R.id.geopositionBackImageDoubleCheck)
    ImageView geopositionRequiredImage;

    @BindView(R.id.photoBackImageDont)
    ImageView photoDontImage;
    @BindView(R.id.photoBackImageCheck)
    ImageView photoOptionalImage;
    @BindView(R.id.photoBackImageDoubleCheck)
    ImageView photoRequiredImage;

    String trackingColor;

    @BindView(R.id.scaleTypeContainer)
    CardView visibilityScaleType;
    @BindView(R.id.scaleTypeHint)
    TextView visbilityScaleTypeHint;
    @BindView(R.id.editTypeOfScale)
    EditText scaleType;

    @BindView(R.id.addTrack)
    Button addTrackingBtn;
    @BindView(R.id.addNewPhoto)
    CardView photoContainer;
    @BindView(R.id.addNewPhotoHint)
    TextView photoHint;
    @BindView(R.id.addNewGeoposition)
    CardView geopositionContainer;
    @BindView(R.id.addNewGeopositionHint)
    TextView geopositionHint;

    private TrackingCustomization rating;
    private TrackingCustomization comment;
    private TrackingCustomization scale;
    private TrackingCustomization photo;
    private TrackingCustomization geoposition;
    private BillingPresenter billingPresenter;

    // Время, когда пользователь открыл экран.
    // Нужно для сбора данных о времени, проведенном пользователем на каждом экране
    private DateTime userOpenAnActivityDateTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ru.lod_misis.ithappened.R.layout.activity_addnewtracking);
        ButterKnife.bind(this);
        ItHappenedApplication.getAppComponent().inject(this);

        createTrackingPresenter.attachView(this);
        billingPresenter = new BillingPresenter(this);
        billingPresenter.attachView(this);
        startConfiguration();
    }

    @Override
    protected void onStart() {
        super.onStart();
        YandexMetrica.reportEvent(getString(R.string.metrica_enter_add_tracking));

        ratingDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingDont.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                ratingEnabled.setText("не надо");
                ratingDontImage.setImageResource(R.drawable.active_dont);
                ratingOptionalImage.setImageResource(R.drawable.not_active_check);
                ratingRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                ratingOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                ratingRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                rating = TrackingCustomization.None;
            }
        });

        ratingOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingDont.setBackgroundColor(Color.parseColor("#ffffff"));
                ratingEnabled.setText("не обязательно");
                ratingDontImage.setImageResource(R.drawable.not_active_dont);
                ratingOptionalImage.setImageResource(R.drawable.active_check);
                ratingRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                ratingOptional.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                ratingRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                rating = TrackingCustomization.Optional;
            }
        });


        ratingRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingDont.setBackgroundColor(Color.parseColor("#ffffff"));
                ratingEnabled.setText("обязательно");
                ratingDontImage.setImageResource(R.drawable.not_active_dont);
                ratingOptionalImage.setImageResource(R.drawable.not_active_check);
                ratingRequiredImage.setImageResource(R.drawable.active_double_check);
                ratingOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                ratingRequired.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                rating = TrackingCustomization.Required;
            }
        });


        commentDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentEnabled.setText("не надо");
                commentDont.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                commentDontImage.setImageResource(R.mipmap.active_dont);
                commentOptionalImage.setImageResource(R.mipmap.not_active_check);
                commentRequiredImage.setImageResource(R.mipmap.not_active_double_chek);
                commentOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                commentRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                comment = TrackingCustomization.None;
            }
        });

        commentOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentEnabled.setText("не обязательно");
                commentDont.setBackgroundColor(Color.parseColor("#ffffff"));
                commentDontImage.setImageResource(R.drawable.not_active_dont);
                commentOptionalImage.setImageResource(R.drawable.active_check);
                commentRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                commentOptional.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                commentRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                comment = TrackingCustomization.Optional;
            }
        });


        commentRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentEnabled.setText("обязательно");
                commentDont.setBackgroundColor(Color.parseColor("#ffffff"));
                commentDontImage.setImageResource(R.drawable.not_active_dont);
                commentOptionalImage.setImageResource(R.drawable.not_active_check);
                commentRequiredImage.setImageResource(R.drawable.active_double_check);
                commentOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                commentRequired.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                comment = TrackingCustomization.Required;
            }
        });


        scaleDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleEnabled.setText("не надо");
                scaleDont.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                scaleDontImage.setImageResource(R.drawable.active_dont);
                scaleOptionalImage.setImageResource(R.drawable.not_active_check);
                scaleRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                scaleOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                scaleRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                scale = TrackingCustomization.None;

                visbilityScaleTypeHint.setVisibility(View.GONE);
                visibilityScaleType.setVisibility(View.GONE);
                scaleType.setVisibility(View.GONE);
            }
        });

        scaleOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleEnabled.setText("не обязательно");
                scaleDont.setBackgroundColor(Color.parseColor("#ffffff"));
                scaleDontImage.setImageResource(R.drawable.not_active_dont);
                scaleOptionalImage.setImageResource(R.drawable.active_check);
                scaleRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                scaleOptional.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                scaleRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                scale = TrackingCustomization.Optional;

                visbilityScaleTypeHint.setVisibility(View.VISIBLE);
                visibilityScaleType.setVisibility(View.VISIBLE);
                scaleType.setVisibility(View.VISIBLE);
            }
        });


        scaleRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleEnabled.setText("обязательно");
                scaleDont.setBackgroundColor(Color.parseColor("#ffffff"));
                scaleDontImage.setImageResource(R.drawable.not_active_dont);
                scaleOptionalImage.setImageResource(R.drawable.not_active_check);
                scaleRequiredImage.setImageResource(R.drawable.active_double_check);
                scaleOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                scaleRequired.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                scale = TrackingCustomization.Required;
                visbilityScaleTypeHint.setVisibility(View.VISIBLE);
                visibilityScaleType.setVisibility(View.VISIBLE);
                scaleType.setVisibility(View.VISIBLE);
            }
        });
        geopositionDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geopositionDont.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                geopositionEnabled.setText("не надо");
                geopositionDontImage.setImageResource(R.drawable.active_dont);
                geopositionOptionalImage.setImageResource(R.drawable.not_active_check);
                geopositionRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                geopositionOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                geopositionRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                geoposition = TrackingCustomization.None;
            }
        });

        geopositionOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geopositionDont.setBackgroundColor(Color.parseColor("#ffffff"));
                geopositionEnabled.setText("не обязательно");
                geopositionDontImage.setImageResource(R.drawable.not_active_dont);
                geopositionOptionalImage.setImageResource(R.drawable.active_check);
                geopositionRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                geopositionOptional.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                geopositionRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                geoposition = TrackingCustomization.Optional;
            }
        });


        geopositionRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geopositionDont.setBackgroundColor(Color.parseColor("#ffffff"));
                geopositionEnabled.setText("обязательно");
                geopositionDontImage.setImageResource(R.drawable.not_active_dont);
                geopositionOptionalImage.setImageResource(R.drawable.not_active_check);
                geopositionRequiredImage.setImageResource(R.drawable.active_double_check);
                geopositionOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                geopositionRequired.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                geoposition = TrackingCustomization.Required;
            }
        });

        photoDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDont.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                photoEnabled.setText("не надо");
                photoDontImage.setImageResource(R.drawable.active_dont);
                photoOptionalImage.setImageResource(R.drawable.not_active_check);
                photoRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                photoOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                photoRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                photo = TrackingCustomization.None;
            }
        });

        photoOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDont.setBackgroundColor(Color.parseColor("#ffffff"));
                photoEnabled.setText("не обязательно");
                photoDontImage.setImageResource(R.drawable.not_active_dont);
                photoOptionalImage.setImageResource(R.drawable.active_check);
                photoRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                photoOptional.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                photoRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                photo = TrackingCustomization.Optional;
            }
        });


        photoRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDont.setBackgroundColor(Color.parseColor("#ffffff"));
                photoEnabled.setText("обязательно");
                photoDontImage.setImageResource(R.drawable.not_active_dont);
                photoOptionalImage.setImageResource(R.drawable.not_active_check);
                photoRequiredImage.setImageResource(R.drawable.active_double_check);
                photoOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                photoRequired.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                photo = TrackingCustomization.Required;
            }
        });

        final SpectrumDialog.Builder colorPickerDialogBuilder = new SpectrumDialog.Builder(getApplicationContext());
        colorPickerDialogBuilder.setTitle("Выберите цвет для отслеживания")
                .setColors(getApplicationContext().getResources().getIntArray(R.array.second_palette))
                .setDismissOnColorSelected(false)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean b, int i) {
                        if (b) {
                            Toast.makeText(getApplicationContext(), Integer.toHexString(i) + "", Toast.LENGTH_SHORT).show();
                            colorPickerDialogBuilder.setSelectedColor(i);
                            colorPickerText.setTextColor(i);
                        }
                    }
                });

        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpectrumDialog dialog = colorPickerDialogBuilder.build();
                dialog.show(getSupportFragmentManager(), "Tag");
            }
        });

        addTrackingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (geoposition == TrackingCustomization.Optional || geoposition == TrackingCustomization.Required) {
                    if (ActivityCompat.checkSelfPermission(AddNewTrackingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(AddNewTrackingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        createTrackingPresenter.requestPermission(GEO_REQUEST_CODE);
                    } else {
                        addNewTracking();
                    }
                } else {
                    addNewTracking();
                }
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
    protected void onResume() {
        super.onResume();
        userOpenAnActivityDateTime = DateTime.now();
    }

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_from_add_tracking));
        Map<String, Object> activityVisitTimeBorders = new HashMap<>();
        activityVisitTimeBorders.put("Start time", userOpenAnActivityDateTime.toDate());
        activityVisitTimeBorders.put("End time", DateTime.now().toDate());
        YandexMetrica.reportEvent(getString(R.string.metrica_user_time_on_activity_add_tracking), activityVisitTimeBorders);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("RequestPermission", "ReSPONSEaLL");
        switch (requestCode) {
            case GEO_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    createGeopositionJobService();
                    addNewTracking();
                }
            }
        }
    }

    private void createGeopositionJobService() {
        ComponentName notificationJobServiece = new ComponentName(this, GeopositionService.class);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(534324, notificationJobServiece);
        jobBuilder.setMinimumLatency(1000 * 60L);
        JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(jobBuilder.build());
        }
    }

    public void addNewTracking() {
        if (trackingName.getText().toString().isEmpty() || trackingName.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Введите название отслеживания", Toast.LENGTH_SHORT).show();
        } else {

            trackingColor = String.valueOf(colorPickerText.getCurrentTextColor());
            String trackingTitle = trackingName.getText().toString().trim();
            String scaleNumb = null;

            if ((scale == TrackingCustomization.Optional || scale == TrackingCustomization.Required) &&
                    (scaleType.getText().toString().isEmpty()
                            || scaleType.getText().toString().trim().isEmpty())) {
                showMessage("Введите единицу измерения шкалы");
            } else {
                if (scale != TrackingCustomization.None) {
                    scaleNumb = scaleType.getText().toString().trim();
                }
                TrackingV1 newTrackingV1 = new TrackingV1(trackingTitle, UUID.randomUUID(), scale, rating, comment, geoposition, photo, scaleNumb, trackingColor);
                createTrackingPresenter.saveNewTracking(newTrackingV1);

                Map<String, Object> customizationMask = new HashMap<>();
                customizationMask.put("Scale customization", scale);
                customizationMask.put("Rating customization", rating);
                customizationMask.put("Comment customization", comment);
                customizationMask.put("Photo customization", photo);
                customizationMask.put("Geoposition customization", geoposition);
                YandexMetrica.reportEvent(getString(R.string.metrica_user_customization_mask), customizationMask);
                YandexMetrica.reportEvent(getString(R.string.metrica_add_tracking));
            }
        }
    }

    @Override
    public void requestPermissionForGeoposition() {
        Log.d("RequestPermission", "Request");
        ActivityCompat.requestPermissions(AddNewTrackingActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, GEO_REQUEST_CODE);
    }

    private void startConfiguration() {
        rating = TrackingCustomization.None;
        comment = TrackingCustomization.None;
        scale = TrackingCustomization.None;
        photo = TrackingCustomization.None;
        geoposition = TrackingCustomization.None;

        visbilityScaleTypeHint.setVisibility(View.GONE);
        visibilityScaleType.setVisibility(View.GONE);
        scaleType.setVisibility(View.GONE);
        billingPresenter.checkPurchase();
        setupToolbar();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishCreatingTracking() {
        Intent intent = new Intent(getApplicationContext(), UserActionsActivity.class);
        startActivity(intent);
        createTrackingPresenter.detachView();
    }

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Отслеживать");
    }

    @Override
    public void getPurchase(Boolean purchase) {
        if (!purchase) {
            photoContainer.setVisibility(View.GONE);
            photoEnabled.setVisibility(View.GONE);
            photoHint.setVisibility(View.GONE);
            geopositionContainer.setVisibility(View.GONE);
            geopositionEnabled.setVisibility(View.GONE);
            geopositionHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        billingPresenter.detachView();
    }
}
