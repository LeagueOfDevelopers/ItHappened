package ru.lod_misis.ithappened.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;

public class EditTrackingActivity extends AppCompatActivity {

    ITrackingRepository trackingRepository;
    TrackingService service;
    InMemoryFactRepository factRepository;

    EditText trackingName;

    TextView ratingEnabled;
    TextView commentEnabled;
    TextView scaleEnabled;

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

    Tracking editableTracking;

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
        setContentView(ru.lod_misis.ithappened.R.layout.activity_addnewtracking);

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Отслеживать");
        factRepository = StaticFactRepository.getInstance();

        trackingId = UUID.fromString(getIntent().getStringExtra("trackingId"));

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        trackingRepository = new StaticInMemoryRepository(getApplicationContext(),
                sharedPreferences.getString("UserId", "")).getInstance();

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

        commentDontImage = (ImageView) findViewById(R.id.commentBackImageDontEdit);
        commentOptionalImage = (ImageView) findViewById(R.id.commentBackImageCheckEdit);
        commentRequiredImage = (ImageView) findViewById(R.id.commentBackImageDoubleCheckEdit);

        scaleDontImage = (ImageView) findViewById(R.id.scaleBackImageDontEdit);
        scaleOptionalImage = (ImageView) findViewById(R.id.scaleBackImageCheckEdit);
        scaleRequiredImage = (ImageView) findViewById(R.id.scaleBackImageDoubleCheckEdit);

        visbilityScaleTypeHint = (TextView) findViewById(R.id.scaleTypeHintEdit);
        visibilityScaleType = (CardView) findViewById(R.id.scaleTypeContainerEdit);
        scaleType = (EditText) findViewById(R.id.editTypeOfScaleEdit);


        editableTracking = trackingRepository.GetTracking(trackingId);

        //trackingName.setText(editableTracking.GetTrackingName());

        if(editableTracking.GetScaleCustomization() == TrackingCustomization.None) {
            visbilityScaleTypeHint.setVisibility(View.GONE);
            visibilityScaleType.setVisibility(View.GONE);
            scaleType.setVisibility(View.GONE);
        }else{
            visbilityScaleTypeHint.setVisibility(View.VISIBLE);
            visibilityScaleType.setVisibility(View.VISIBLE);
            scaleType.setVisibility(View.VISIBLE);
            scaleType.setText(editableTracking.getScaleName());
        }

        stateForRating = calculateState(editableTracking.GetRatingCustomization(),
                ratingDontImage,
                ratingOptionalImage,
                ratingRequiredImage,
                ratingDont,
                ratingOptional,
                ratingRequired,
                ratingEnabled);
        stateForText = calculateState(editableTracking.GetCommentCustomization(),
                commentDontImage,
                commentOptionalImage,
                commentRequiredImage,
                commentDont,
                commentOptional,
                commentRequired,
                commentEnabled);
        stateForScale = calculateState(editableTracking.GetRatingCustomization(),
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
                ratingDontImage.setImageResource(R.mipmap.active_dont);
                ratingOptionalImage.setImageResource(R.mipmap.not_active_check);
                ratingRequiredImage.setImageResource(R.mipmap.not_active_double_chek);
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
                ratingDontImage.setImageResource(R.mipmap.not_active_dont);
                ratingOptionalImage.setImageResource(R.mipmap.active_check);
                ratingRequiredImage.setImageResource(R.mipmap.not_active_double_chek);
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
                ratingDontImage.setImageResource(R.mipmap.not_active_dont);
                ratingOptionalImage.setImageResource(R.mipmap.not_active_check);
                ratingRequiredImage.setImageResource(R.mipmap.active_double_check);
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
                commentDontImage.setImageResource(R.mipmap.not_active_dont);
                commentOptionalImage.setImageResource(R.mipmap.active_check);
                commentRequiredImage.setImageResource(R.mipmap.not_active_double_chek);
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
                commentDontImage.setImageResource(R.mipmap.not_active_dont);
                commentOptionalImage.setImageResource(R.mipmap.not_active_check);
                commentRequiredImage.setImageResource(R.mipmap.active_double_check);
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
                scaleDontImage.setImageResource(R.mipmap.active_dont);
                scaleOptionalImage.setImageResource(R.mipmap.not_active_check);
                scaleRequiredImage.setImageResource(R.mipmap.not_active_double_chek);
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
                scaleDontImage.setImageResource(R.mipmap.not_active_dont);
                scaleOptionalImage.setImageResource(R.mipmap.active_check);
                scaleRequiredImage.setImageResource(R.mipmap.not_active_double_chek);
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
                scaleDontImage.setImageResource(R.mipmap.not_active_dont);
                scaleOptionalImage.setImageResource(R.mipmap.not_active_check);
                scaleRequiredImage.setImageResource(R.mipmap.active_double_check);
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
                if(trackingName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Введите название отслеживания", Toast.LENGTH_SHORT).show();
                }else{

                    String trackingTitle = trackingName.getText().toString();

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

                    if((scale == TrackingCustomization.Optional || scale == TrackingCustomization.Required)&&scaleType.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Введите единицу измерения шкалы", Toast.LENGTH_SHORT).show();
                    }else{
                        if(scale != TrackingCustomization.None){
                            scaleNumb = scaleType.getText().toString();
                        }
                        service.EditTracking(trackingId, rating, scale, comment, trackingTitle, scaleNumb);
                        Toast.makeText(getApplicationContext(), "Отслеживание изменено", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), UserActionsActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


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
                dontImg.setImageResource(R.mipmap.active_dont);
                checkImg.setImageResource(R.mipmap.not_active_check);
                doubleCheckImg.setImageResource(R.mipmap.not_active_double_chek);
                check.setBackgroundColor(Color.parseColor("#ffffff"));
                doubleCheck.setBackgroundColor(Color.parseColor("#ffffff"));
                return 0;
            case Optional:
                hint.setText("не обязательно");
                dont.setBackgroundColor(Color.parseColor("#ffffff"));
                dontImg.setImageResource(R.mipmap.not_active_dont);
                checkImg.setImageResource(R.mipmap.active_check);
                doubleCheckImg.setImageResource(R.mipmap.not_active_double_chek);
                check.setBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                doubleCheck.setBackgroundColor(Color.parseColor("#ffffff"));
                return 1;
            case Required:
                hint.setText("обязательно");
                dont.setBackgroundColor(Color.parseColor("#ffffff"));
                dontImg.setImageResource(R.mipmap.not_active_dont);
                checkImg.setImageResource(R.mipmap.not_active_check);
                doubleCheckImg.setImageResource(R.mipmap.active_double_check);
                check.setBackgroundColor(Color.parseColor("#ffffff"));
                doubleCheck.setBackgroundColor(getResources().getColor(R.color.required));
                return 2;
            default:
                break;

        }

        return 0;

    }




}
