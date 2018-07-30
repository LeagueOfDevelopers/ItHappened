package ru.lod_misis.ithappened.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.UUID;

import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddNewTrackingActivity extends AppCompatActivity {

    ITrackingRepository trackingRepository;
    InMemoryFactRepository factRepository;

    EditText trackingName;

    TextView ratingEnabled;
    TextView commentEnabled;
    TextView scaleEnabled;
    TextView geopositionEnabled;

    CardView colorPickerButton;
    TextView colorPickerText;

    LinearLayout ratingDont;
    LinearLayout ratingOptional;
    LinearLayout ratingRequired;

    LinearLayout commentDont;
    LinearLayout commentOptional;
    LinearLayout commentRequired;

    LinearLayout scaleDont;
    LinearLayout scaleOptional;
    LinearLayout scaleRequired;

    LinearLayout geopositionDont;
    LinearLayout geopositionOptional;
    LinearLayout geopositionRequired;

    ImageView ratingDontImage;
    ImageView ratingOptionalImage;
    ImageView ratingRequiredImage;

    ImageView commentDontImage;
    ImageView commentOptionalImage;
    ImageView commentRequiredImage;

    ImageView scaleDontImage;
    ImageView scaleOptionalImage;
    ImageView scaleRequiredImage;

    ImageView geopositionDontImage;
    ImageView geopositionOptionalImage;
    ImageView geopositionRequiredImage;

    String trackingColor;


    CardView visibilityScaleType;
    TextView visbilityScaleTypeHint;
    EditText scaleType;

    Button addTrackingBtn;

    int stateForScale;
    int stateForText;
    int stateForRating;
    int stateForGeoposition;

    TrackingCustomization geoposition ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ru.lod_misis.ithappened.R.layout.activity_addnewtracking);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Отслеживать");
        factRepository = StaticFactRepository.getInstance();
        YandexMetrica.reportEvent(getString(R.string.metrica_enter_add_tracking));

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        if(sharedPreferences.getString("LastId","").isEmpty()) {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
            trackingRepository = StaticInMemoryRepository.getInstance();
        }else{
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
            trackingRepository = StaticInMemoryRepository.getInstance();
        }

        addTrackingBtn = (Button) findViewById(ru.lod_misis.ithappened.R.id.addTrack);

        trackingName = (EditText) findViewById(R.id.editTitleOfTracking);

        stateForScale = 0;
        stateForText = 0;
        stateForRating = 0;
        stateForGeoposition=0;

        ratingEnabled = (TextView) findViewById(R.id.ratingTextEnabled);
        commentEnabled = (TextView) findViewById(R.id.commentTextEnabled);
        scaleEnabled = (TextView) findViewById(R.id.scaleTextEnabled);
        geopositionEnabled = (TextView) findViewById(R.id.geopositionTextEnabled);

        ratingDont = (LinearLayout) findViewById(R.id.ratingBackColorDont);
        ratingOptional = (LinearLayout) findViewById(R.id.ratingBackColorCheck);
        ratingRequired = (LinearLayout) findViewById(R.id.ratingBackColorDoubleCheck);

        colorPickerButton = findViewById(R.id.colorPicker);

        commentDont = (LinearLayout) findViewById(R.id.commentBackColorDont);
        commentOptional = (LinearLayout) findViewById(R.id.commentBackColorCheck);
        commentRequired = (LinearLayout) findViewById(R.id.commentBackColorDoubleCheck);

        scaleDont = (LinearLayout) findViewById(R.id.scaleBackColorDont);
        scaleOptional = (LinearLayout) findViewById(R.id.scaleBackColorCheck);
        scaleRequired = (LinearLayout) findViewById(R.id.scaleBackColorDoubleCheck);

        geopositionDont = (LinearLayout) findViewById(R.id.geopositionBackColorDont);
        geopositionOptional = (LinearLayout) findViewById(R.id.geopositionBackColorCheck);
        geopositionRequired= (LinearLayout) findViewById(R.id.geopositionBackColorDoubleCheck);

        ratingDontImage = (ImageView) findViewById(R.id.ratingBackImageDont);
        ratingOptionalImage = (ImageView) findViewById(R.id.ratingBackImageCheck);
        ratingRequiredImage = (ImageView) findViewById(R.id.ratingBackImageDoubleCheck);

        commentDontImage = (ImageView) findViewById(R.id.commentBackImageDont);
        commentOptionalImage = (ImageView) findViewById(R.id.commentBackImageCheck);
        commentRequiredImage = (ImageView) findViewById(R.id.commentBackImageDoubleCheck);

        scaleDontImage = (ImageView) findViewById(R.id.scaleBackImageDont);
        scaleOptionalImage = (ImageView) findViewById(R.id.scaleBackImageCheck);
        scaleRequiredImage = (ImageView) findViewById(R.id.scaleBackImageDoubleCheck);

        geopositionDontImage = (ImageView) findViewById(R.id.geopositionBackImageDont);
        geopositionOptionalImage = (ImageView) findViewById(R.id.geopositionBackImageCheck);
        geopositionRequiredImage = (ImageView) findViewById(R.id.geopositionBackImageDoubleCheck);

        colorPickerText = findViewById(R.id.colorText);

        visbilityScaleTypeHint = (TextView) findViewById(R.id.scaleTypeHint);
        visibilityScaleType = (CardView) findViewById(R.id.scaleTypeContainer);
        scaleType = (EditText) findViewById(R.id.editTypeOfScale);

        visbilityScaleTypeHint.setVisibility(View.GONE);
        visibilityScaleType.setVisibility(View.GONE);
        scaleType.setVisibility(View.GONE);


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
                geoposition=TrackingCustomization.None;
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
                geoposition=TrackingCustomization.Optional;
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
                geoposition=TrackingCustomization.Required;
            }
        });

        final SpectrumDialog.Builder colorPickerDialogBuilder = new SpectrumDialog.Builder(getApplicationContext());
        colorPickerDialogBuilder.setTitle("Выберите цвет для отслеживания")
                .setColors(getApplicationContext().getResources().getIntArray(R.array.second_palette))
                .setDismissOnColorSelected(false)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean b, int i) {
                        if(b){
                            Toast.makeText(getApplicationContext(), Integer.toHexString(i)+"", Toast.LENGTH_SHORT).show();
                            colorPickerDialogBuilder.setSelectedColor(i);
                            colorPickerText.setTextColor(i  );
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
                if(trackingName.getText().toString().isEmpty()||trackingName.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Введите название отслеживания", Toast.LENGTH_SHORT).show();
                }else{

                    trackingColor = String.valueOf(colorPickerText.getCurrentTextColor());
                    String trackingTitle = trackingName.getText().toString().trim();

                    TrackingCustomization rating = TrackingCustomization.None;
                    TrackingCustomization comment = TrackingCustomization.None;
                    TrackingCustomization scale = TrackingCustomization.None;

                    String scaleNumb = null;

                    switch (stateForRating){
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


                    switch (stateForText){
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

                    switch (stateForScale){
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

                    if((scale == TrackingCustomization.Optional || scale == TrackingCustomization.Required)&&
                            (scaleType.getText().toString().isEmpty()
                            ||scaleType.getText().toString().trim().isEmpty())){
                        Toast.makeText(getApplicationContext(), "Введите единицу измерения шкалы", Toast.LENGTH_SHORT).show();
                    }else{
                        if(scale != TrackingCustomization.None){
                            scaleNumb = scaleType.getText().toString().trim();
                        }
                        TrackingV1 newTrackingV1 = new TrackingV1(trackingTitle, UUID.randomUUID(), scale, rating, comment,geoposition, scaleNumb, trackingColor);
                        trackingRepository.AddNewTracking(newTrackingV1);
                        YandexMetrica.reportEvent(getString(R.string.metrica_add_tracking));
                        Toast.makeText(getApplicationContext(), "Отслеживание добавлено", Toast.LENGTH_SHORT).show();
                        factRepository.onChangeCalculateOneTrackingFacts(trackingRepository.GetTrackingCollection(), newTrackingV1.GetTrackingID())
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call(Fact fact) {
                                        Log.d("Statistics", "calculate");
                                    }
                                });
                        factRepository.calculateAllTrackingsFacts(trackingRepository.GetTrackingCollection())
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call(Fact fact) {
                                        Log.d("Statistics", "calculate");
                                    }
                                });
                        Intent intent = new Intent(getApplicationContext(), UserActionsActivity.class);
                        startActivity(intent);
                    }
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


}
