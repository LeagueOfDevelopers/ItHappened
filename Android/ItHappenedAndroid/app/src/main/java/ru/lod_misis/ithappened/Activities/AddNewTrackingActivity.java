package ru.lod_misis.ithappened.Activities;

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

import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.R;

public class AddNewTrackingActivity extends AppCompatActivity {

    ITrackingRepository trackingRepository;
    InMemoryFactRepository factRepository;

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

    ImageView commentDontImage;
    ImageView commentOptionalImage;
    ImageView commentRequiredImage;

    ImageView scaleDontImage;
    ImageView scaleOptionalImage;
    ImageView scaleRequiredImage;


    CardView visibilityScaleType;
    TextView visbilityScaleTypeHint;
    EditText scaleType;

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

        addTrackingBtn = (Button) findViewById(ru.lod_misis.ithappened.R.id.addTrack);

        stateForScale = 0;
        stateForText = 0;
        stateForRating = 0;

        ratingEnabled = (TextView) findViewById(R.id.ratingTextEnabled);
        commentEnabled = (TextView) findViewById(R.id.commentTextEnabled);
        scaleEnabled = (TextView) findViewById(R.id.scaleTextEnabled);


        ratingDont = (LinearLayout) findViewById(R.id.ratingBackColorDont);
        ratingOptional = (LinearLayout) findViewById(R.id.ratingBackColorCheck);
        ratingRequired = (LinearLayout) findViewById(R.id.ratingBackColorDoubleCheck);

        commentDont = (LinearLayout) findViewById(R.id.commentBackColorDont);
        commentOptional = (LinearLayout) findViewById(R.id.commentBackColorCheck);
        commentRequired = (LinearLayout) findViewById(R.id.commentBackColorDoubleCheck);

        scaleDont = (LinearLayout) findViewById(R.id.scaleBackColorDont);
        scaleOptional = (LinearLayout) findViewById(R.id.scaleBackColorCheck);
        scaleRequired = (LinearLayout) findViewById(R.id.scaleBackColorDoubleCheck);

        ratingDontImage = (ImageView) findViewById(R.id.ratingBackImageDont);
        ratingOptionalImage = (ImageView) findViewById(R.id.ratingBackImageCheck);
        ratingRequiredImage = (ImageView) findViewById(R.id.ratingBackImageDoubleCheck);

        commentDontImage = (ImageView) findViewById(R.id.commentBackImageDont);
        commentOptionalImage = (ImageView) findViewById(R.id.commentBackImageCheck);
        commentRequiredImage = (ImageView) findViewById(R.id.commentBackImageDoubleCheck);

        scaleDontImage = (ImageView) findViewById(R.id.scaleBackImageDont);
        scaleOptionalImage = (ImageView) findViewById(R.id.scaleBackImageCheck);
        scaleRequiredImage = (ImageView) findViewById(R.id.scaleBackImageDoubleCheck);

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


    }


}
