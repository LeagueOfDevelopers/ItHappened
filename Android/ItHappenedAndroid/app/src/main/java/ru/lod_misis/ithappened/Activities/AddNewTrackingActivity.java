package ru.lod_misis.ithappened.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Presenters.CreateTrackingContract;
import ru.lod_misis.ithappened.Presenters.CreateTrackingPresenter;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Statistics.FactCalculator;

public class AddNewTrackingActivity extends AppCompatActivity  implements CreateTrackingContract.CreateTrackingView{

    ITrackingRepository trackingRepository;
    InMemoryFactRepository factRepository;
    CreateTrackingContract.CreateTrackingPresenter createTrackingPresenter;
    FactCalculator factCalculator;

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

    String trackingColor;

    @BindView(R.id.scaleTypeContainer)
    CardView visibilityScaleType;
    @BindView(R.id.scaleTypeHint)
    TextView visbilityScaleTypeHint;
    @BindView(R.id.editTypeOfScale)
    EditText scaleType;

    @BindView(R.id.addTrack)
    Button addTrackingBtn;

    int stateForScale;
    int stateForText;
    int stateForRating;
    int stateForGeoposition;

    TrackingCustomization geoposition = TrackingCustomization.None;

    Context context;
    Activity activity;
    Boolean permissionForGPS = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ru.lod_misis.ithappened.R.layout.activity_addnewtracking);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        createTrackingPresenter=new CreateTrackingPresenter(sharedPreferences);
        createTrackingPresenter.attachView(this);
        createTrackingPresenter.init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        YandexMetrica.reportEvent(getString(R.string.metrica_enter_add_tracking));

        ratingDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingDont.setBackgroundColor(getResources().getColor(R.color.dont));
                ratingEnabled.setText("не надо");
                ratingDontImage.setImageResource(R.drawable.active_dont);
                ratingOptionalImage.setImageResource(R.drawable.not_active_check);
                ratingRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                ratingOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                ratingRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                stateForRating = 0;
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
                ratingOptional.setBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                ratingRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                stateForRating = 1;
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
                ratingRequired.setBackgroundColor(getResources().getColor(R.color.required));
                stateForRating = 2;
            }
        });


        commentDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentEnabled.setText("не надо");
                commentDont.setBackgroundColor(getResources().getColor(R.color.dont));
                commentDontImage.setImageResource(R.mipmap.active_dont);
                commentOptionalImage.setImageResource(R.mipmap.not_active_check);
                commentRequiredImage.setImageResource(R.mipmap.not_active_double_chek);
                commentOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                commentRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                stateForText = 0;
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
                commentOptional.setBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                commentRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                stateForText = 1;
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
                commentRequired.setBackgroundColor(getResources().getColor(R.color.required));
                stateForText = 2;
            }
        });


        scaleDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleEnabled.setText("не надо");
                scaleDont.setBackgroundColor(getResources().getColor(R.color.dont));
                scaleDontImage.setImageResource(R.drawable.active_dont);
                scaleOptionalImage.setImageResource(R.drawable.not_active_check);
                scaleRequiredImage.setImageResource(R.drawable.not_active_double_chek);
                scaleOptional.setBackgroundColor(Color.parseColor("#ffffff"));
                scaleRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                stateForScale = 0;

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
                scaleOptional.setBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                scaleRequired.setBackgroundColor(Color.parseColor("#ffffff"));
                stateForScale = 1;

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
                scaleRequired.setBackgroundColor(getResources().getColor(R.color.required));
                stateForScale = 2;

                visbilityScaleTypeHint.setVisibility(View.VISIBLE);
                visibilityScaleType.setVisibility(View.VISIBLE);
                scaleType.setVisibility(View.VISIBLE);
            }
        });
        geopositionDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geopositionDont.setBackgroundColor(getResources().getColor(R.color.dont));
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
                geopositionOptional.setBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
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
                geopositionRequired.setBackgroundColor(getResources().getColor(R.color.required));
                geoposition = TrackingCustomization.Required;
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
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        createTrackingPresenter.requestPermission(1);
                    } else {
                        createTrackingPresenter.createNewTracking();
                    }
                }else{
                    createTrackingPresenter.createNewTracking();
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
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_from_add_tracking));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    createTrackingPresenter.createNewTracking();
                }
            }
        }
    }

    public void addNewTracking() {
        if (trackingName.getText().toString().isEmpty() || trackingName.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Введите название отслеживания", Toast.LENGTH_SHORT).show();
        } else {

            trackingColor = String.valueOf(colorPickerText.getCurrentTextColor());
            String trackingTitle = trackingName.getText().toString().trim();

            TrackingCustomization rating = TrackingCustomization.None;
            TrackingCustomization comment = TrackingCustomization.None;
            TrackingCustomization scale = TrackingCustomization.None;

            String scaleNumb = null;

            switch (stateForRating) {
                case 0:
                    rating = TrackingCustomization.None;
                    break;
                case 1:
                    rating = TrackingCustomization.Optional;
                    break;
                case 2:
                    rating = TrackingCustomization.Required;
                    break;
                default:
                    break;
            }


            switch (stateForText) {
                case 0:
                    comment = TrackingCustomization.None;
                    break;
                case 1:
                    comment = TrackingCustomization.Optional;
                    break;
                case 2:
                    comment = TrackingCustomization.Required;
                    break;
                default:
                    break;
            }

            switch (stateForScale) {
                case 0:
                    scale = TrackingCustomization.None;
                    break;
                case 1:
                    scale = TrackingCustomization.Optional;
                    break;
                case 2:
                    scale = TrackingCustomization.Required;
                    break;
                default:
                    break;
            }

            if ((scale == TrackingCustomization.Optional || scale == TrackingCustomization.Required) &&
                    (scaleType.getText().toString().isEmpty()
                            || scaleType.getText().toString().trim().isEmpty())) {
               showMessage("Введите единицу измерения шкалы");
            } else {
                if (scale != TrackingCustomization.None) {
                    scaleNumb = scaleType.getText().toString().trim();
                }
                TrackingV1 newTrackingV1 = new TrackingV1(trackingTitle, UUID.randomUUID(), scale, rating, comment, geoposition, scaleNumb, trackingColor);
                createTrackingPresenter.saveNewTracking(newTrackingV1);
                YandexMetrica.reportEvent(getString(R.string.metrica_add_tracking));



            }
        }
    }

    @Override
    public void createTracking() {
        addNewTracking();
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
        visbilityScaleTypeHint.setVisibility(View.GONE);
        visibilityScaleType.setVisibility(View.GONE);
        scaleType.setVisibility(View.GONE);
        setupToolbar();
    }

    @Override
    public void satredConfiguration() {
        stateForScale = 0;
        stateForText = 0;
        stateForRating = 0;
        stateForGeoposition = 0;

        context = this;
        activity = this;
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

    private void setupToolbar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Отслеживать");
    }
}
