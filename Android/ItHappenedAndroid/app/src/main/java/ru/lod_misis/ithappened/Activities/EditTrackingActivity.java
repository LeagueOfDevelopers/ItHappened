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

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.NewTracking;
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

public class EditTrackingActivity extends AppCompatActivity {

    ITrackingRepository trackingRepository;
    TrackingService service;
    InMemoryFactRepository factRepository;

    EditText trackingName;

    TextView ratingEnabled;
    TextView commentEnabled;
    TextView scaleEnabled;

    CardView colorTrackingEdit;
    TextView colorTrackingTextEdit;

    String trackingColor;

    LinearLayout ratingDont;
    LinearLayout ratingOptional;
    LinearLayout ratingRequired;

    LinearLayout commentDont;
    LinearLayout commentOptional;
    LinearLayout commentRequired;

    LinearLayout scaleDont;
    LinearLayout scaleOptional;
    LinearLayout scaleRequired;

    ImageView ratingDontImage;
    ImageView ratingOptionalImage;
    ImageView ratingRequiredImage;

    NewTracking editableNewTracking;

    ImageView commentDontImage;
    ImageView commentOptionalImage;
    ImageView commentRequiredImage;

    ImageView scaleDontImage;
    ImageView scaleOptionalImage;
    ImageView scaleRequiredImage;


    CardView visibilityScaleType;
    TextView visbilityScaleTypeHint;
    EditText scaleType;

    UUID trackingId;

    Button addTrackingBtn;

    int stateForScale;
    int stateForText;
    int stateForRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tracking);

        YandexMetrica.reportEvent("Пользователь зашел в изменение отслеживания");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Изменить отслеживание");

        factRepository = StaticFactRepository.getInstance();

        trackingId = UUID.fromString(getIntent().getStringExtra("trackingId"));

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        if(sharedPreferences.getString("LastId","").isEmpty()) {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
            trackingRepository = StaticInMemoryRepository.getInstance();
        }else{
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
            trackingRepository = StaticInMemoryRepository.getInstance();
        }

        service = new TrackingService(sharedPreferences.getString("UserId", ""), trackingRepository);

        addTrackingBtn = (Button) findViewById(ru.lod_misis.ithappened.R.id.editTrack);

        trackingName = (EditText) findViewById(R.id.editTitleOfTrackingEdit);


        ratingEnabled = (TextView) findViewById(R.id.ratingTextEnabledEdit);
        commentEnabled = (TextView) findViewById(R.id.commentTextEnabledEdit);
        scaleEnabled = (TextView) findViewById(R.id.scaleTextEnabledEdit);


        ratingDont = (LinearLayout) findViewById(R.id.ratingBackColorDontEdit);
        ratingOptional = (LinearLayout) findViewById(R.id.ratingBackColorCheckEdit);
        ratingRequired = (LinearLayout) findViewById(R.id.ratingBackColorDoubleCheckEdit);

        commentDont = (LinearLayout) findViewById(R.id.commentBackColorDontEdit);
        commentOptional = (LinearLayout) findViewById(R.id.commentBackColorCheckEdit);
        commentRequired = (LinearLayout) findViewById(R.id.commentBackColorDoubleCheckEdit);

        scaleDont = (LinearLayout) findViewById(R.id.scaleBackColorDontEdit);
        scaleOptional = (LinearLayout) findViewById(R.id.scaleBackColorCheckEdit);
        scaleRequired = (LinearLayout) findViewById(R.id.scaleBackColorDoubleCheckEdit);

        ratingDontImage = (ImageView) findViewById(R.id.ratingBackImageDontEdit);
        ratingOptionalImage = (ImageView) findViewById(R.id.ratingBackImageCheckEdit);
        ratingRequiredImage = (ImageView) findViewById(R.id.ratingBackImageDoubleCheckEdit);

        colorTrackingEdit = findViewById(R.id.colorPickerEdit);
        colorTrackingTextEdit = findViewById(R.id.colorTextEdit);

        commentDontImage = (ImageView) findViewById(R.id.commentBackImageDontEdit);
        commentOptionalImage = (ImageView) findViewById(R.id.commentBackImageCheckEdit);
        commentRequiredImage = (ImageView) findViewById(R.id.commentBackImageDoubleCheckEdit);

        scaleDontImage = (ImageView) findViewById(R.id.scaleBackImageDontEdit);
        scaleOptionalImage = (ImageView) findViewById(R.id.scaleBackImageCheckEdit);
        scaleRequiredImage = (ImageView) findViewById(R.id.scaleBackImageDoubleCheckEdit);

        visbilityScaleTypeHint = (TextView) findViewById(R.id.scaleTypeHintEdit);
        visibilityScaleType = (CardView) findViewById(R.id.scaleTypeContainerEdit);
        scaleType = (EditText) findViewById(R.id.editTypeOfScaleEdit);


        editableNewTracking = trackingRepository.GetTracking(trackingId);

        trackingName.setText(editableNewTracking.GetTrackingName());

        final SpectrumDialog.Builder colorPickerDialogBuilder = new SpectrumDialog.Builder(getApplicationContext());
        colorPickerDialogBuilder.setTitle("Выберите цвет для отслеживания")
                .setColors(getApplicationContext().getResources().getIntArray(R.array.second_palette))
                .setSelectedColor(Integer.parseInt(trackingRepository.GetTracking(trackingId).getColor()))
                .setDismissOnColorSelected(false)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean b, int i) {
                        if(b){
                            Toast.makeText(getApplicationContext(), Integer.toHexString(i)+"", Toast.LENGTH_SHORT).show();
                            colorPickerDialogBuilder.setSelectedColor(i);
                            colorTrackingTextEdit.setTextColor(i  );
                        }
                    }
                });

        colorTrackingTextEdit.setTextColor(Integer.parseInt(trackingRepository.GetTracking(trackingId).getColor()));

        colorTrackingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpectrumDialog dialog = colorPickerDialogBuilder.build();
                dialog.show(getSupportFragmentManager(), "Tag");
            }
        });

        if(editableNewTracking.GetScaleCustomization() == TrackingCustomization.None) {
            visbilityScaleTypeHint.setVisibility(View.GONE);
            visibilityScaleType.setVisibility(View.GONE);
            scaleType.setVisibility(View.GONE);
        }else{
            visbilityScaleTypeHint.setVisibility(View.VISIBLE);
            visibilityScaleType.setVisibility(View.VISIBLE);
            scaleType.setVisibility(View.VISIBLE);
            scaleType.setText(editableNewTracking.getScaleName());
        }

        stateForRating = calculateState(editableNewTracking.GetRatingCustomization(),
                ratingDontImage,
                ratingOptionalImage,
                ratingRequiredImage,
                ratingDont,
                ratingOptional,
                ratingRequired,
                ratingEnabled);
        stateForText = calculateState(editableNewTracking.GetCommentCustomization(),
                commentDontImage,
                commentOptionalImage,
                commentRequiredImage,
                commentDont,
                commentOptional,
                commentRequired,
                commentEnabled);
        stateForScale = calculateState(editableNewTracking.GetScaleCustomization(),
                scaleDontImage,
                scaleOptionalImage,
                scaleRequiredImage,
                scaleDont,
                scaleOptional,
                scaleRequired,
                scaleEnabled);



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
                commentDontImage.setImageResource(R.drawable.active_dont);
                commentOptionalImage.setImageResource(R.drawable.not_active_check);
                commentRequiredImage.setImageResource(R.drawable.not_active_double_chek);
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

        addTrackingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trackingName.getText().toString().isEmpty()||trackingName.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Введите название отслеживания", Toast.LENGTH_SHORT).show();
                }else{

                    trackingColor = ""+colorTrackingTextEdit.getCurrentTextColor();
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
                        service.EditTracking(trackingId, scale, rating, comment, trackingTitle, scaleNumb, trackingColor);
                        factRepository.onChangeCalculateOneTrackingFacts(trackingRepository.GetTrackingCollection(), trackingId)
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
                        YandexMetrica.reportEvent("Пользователь изменил отслеживание");
                        Toast.makeText(getApplicationContext(), "Отслеживание изменено", Toast.LENGTH_SHORT).show();
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
    protected void onPostResume() {
        super.onPostResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent("Пользователь вышел из изменения отслеживания");
    }

    private int calculateState(TrackingCustomization customization,
                               ImageView dontImg,
                               ImageView checkImg,
                               ImageView doubleCheckImg,
                               LinearLayout dont,
                               LinearLayout check,
                               LinearLayout doubleCheck,
                               TextView hint
                               ){
        switch (customization){
            case None:
                hint.setText("не надо");
                dont.setBackgroundColor(getResources().getColor(R.color.dont));
                dontImg.setImageResource(R.drawable.active_dont);
                checkImg.setImageResource(R.drawable.not_active_check);
                doubleCheckImg.setImageResource(R.drawable.not_active_double_chek);
                check.setBackgroundColor(Color.parseColor("#ffffff"));
                doubleCheck.setBackgroundColor(Color.parseColor("#ffffff"));
                return 0;
            case Optional:
                hint.setText("не обязательно");
                dont.setBackgroundColor(Color.parseColor("#ffffff"));
                dontImg.setImageResource(R.drawable.not_active_dont);
                checkImg.setImageResource(R.drawable.active_check);
                doubleCheckImg.setImageResource(R.drawable.not_active_double_chek);
                check.setBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                doubleCheck.setBackgroundColor(Color.parseColor("#ffffff"));
                return 1;
            case Required:
                hint.setText("обязательно");
                dont.setBackgroundColor(Color.parseColor("#ffffff"));
                dontImg.setImageResource(R.drawable.not_active_dont);
                checkImg.setImageResource(R.drawable.not_active_check);
                doubleCheckImg.setImageResource(R.drawable.active_double_check);
                check.setBackgroundColor(Color.parseColor("#ffffff"));
                doubleCheck.setBackgroundColor(getResources().getColor(R.color.required));
                return 2;
            default:
                break;

        }

        return 0;

    }




}
