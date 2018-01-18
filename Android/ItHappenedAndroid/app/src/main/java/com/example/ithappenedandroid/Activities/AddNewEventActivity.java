package com.example.ithappenedandroid.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Rating;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.UUID;

public class AddNewEventActivity extends AppCompatActivity {

    TrackingService trackingService;
    ITrackingRepository TrackingCollection = StaticInMemoryRepository.getInstance();

    LinearLayout textCustomControl;
    LinearLayout scaleCustomControl;
    LinearLayout ratingCustomControl;

    LinearLayout hintForComment;
    LinearLayout hintForRating;
    LinearLayout hintForScale;

    TextView hintForCommentText;
    TextView hintForRatingText;
    TextView hintForScaleText;

    Button addNewEvent;

    EditText commentControl;
    RatingBar ratingControl;
    EditText scaleControl;

    int stateForComment = 0;
    int stateForRating = 0;
    int stateForScale = 0;

    String commentForEvent;
    int ratingForEvent;
    Double scaleForEvent;

    int trackingPosition;
    ITrackingRepository trackingCollection;

    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        trackingService = new TrackingService("thisUser", trackingCollection);


        trackingCollection = StaticInMemoryRepository.getInstance();
        textCustomControl = (LinearLayout) findViewById(R.id.commentControl);
        scaleCustomControl = (LinearLayout) findViewById(R.id.scaleControl);
        ratingCustomControl = (LinearLayout) findViewById(R.id.ratingControl);

        hintForComment = (LinearLayout) findViewById(R.id.hintForNewComment);
        hintForRating = (LinearLayout) findViewById(R.id.hintForNewRating);
        hintForScale = (LinearLayout) findViewById(R.id.hintForNewScale);

        Intent intent = getIntent();
        trackingPosition = intent.getExtras().getInt("tracking");
        id = intent.getStringExtra("trackingId");
        Tracking thisTracking = trackingCollection.GetTracking(UUID.fromString(id));

        //добавляем контрол для комментария

        if(thisTracking.GetCommentCustomization() == TrackingCustomization.Required || thisTracking.GetCommentCustomization() == TrackingCustomization.Optional){

            hintForCommentText = new TextView(this);
            hintForCommentText.setText("Добавьте комментарий:");
            hintForCommentText.setTextSize(20);
            hintForCommentText.setPadding(10, 10, 10, 10);
            hintForCommentText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            LinearLayout.LayoutParams commentHint = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            commentHint.setMargins(10, 40, 10, 10);

            hintForCommentText.setLayoutParams(commentHint);
            hintForComment.addView(hintForCommentText);

            commentControl = new EditText(getApplication());
            commentControl.setHint("Ваш комментарий");
            commentControl.setTextColor(getResources().getColor(R.color.cardview_dark_background));
            commentControl.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if(thisTracking.GetCommentCustomization() == TrackingCustomization.Required){

                commentControl.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                stateForComment = 2;

            }

            if(thisTracking.GetCommentCustomization() == TrackingCustomization.Optional){

                commentControl.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_for_not_definetly), PorterDuff.Mode.SRC_ATOP);
                stateForComment = 1;

            }
            LinearLayout.LayoutParams commentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            commentLayoutParams.setMargins(10,40,10,10);

            commentControl.setLayoutParams(commentLayoutParams);
            textCustomControl.addView(commentControl);
        }

        //добавляем контрол для рейтинга

        if(thisTracking.GetRatingCustomization() == TrackingCustomization.Required ){

            hintForRatingText = new TextView(this);
            hintForRatingText.setText("Добавьте оценку:");
            hintForRatingText.setTextSize(20);
            hintForRatingText.setPadding(10, 10, 10, 10);
            hintForRatingText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            LinearLayout.LayoutParams ratingHint = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            ratingHint.setMargins(10, 40, 10, 10);

            hintForRatingText.setLayoutParams(ratingHint);
            hintForRating.addView(hintForRatingText);

            stateForRating = 2;

            ratingControl = new RatingBar(getApplication());
            ratingControl.setNumStars(5);
            ratingControl.setStepSize(1/2);

            LayerDrawable stars = (LayerDrawable) ratingControl.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);

            LinearLayout.LayoutParams ratingLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            ratingLayoutParams.setMargins(10,40,10,10);

            ratingControl.setLayoutParams(ratingLayoutParams);
            ratingCustomControl.addView(ratingControl);
        }

        if(thisTracking.GetRatingCustomization() == TrackingCustomization.Optional ){

            hintForRatingText = new TextView(this);
            hintForRatingText.setText("Добавьте оценку:");
            hintForRatingText.setTextSize(20);
            hintForRatingText.setPadding(10, 10, 10, 10);
            hintForRatingText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            LinearLayout.LayoutParams ratingHint = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            ratingHint.setMargins(10, 40, 10, 10);

            hintForRatingText.setLayoutParams(ratingHint);
            hintForRating.addView(hintForRatingText);

            stateForRating = 1;

            ratingControl = new RatingBar(getApplication());
            ratingControl.setNumStars(5);
            ratingControl.setStepSize(1/2);

            LayerDrawable stars = (LayerDrawable) ratingControl.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.color_for_not_definetly), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);

            LinearLayout.LayoutParams ratingLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            ratingLayoutParams.setMargins(10,40,10,10);

            ratingControl.setLayoutParams(ratingLayoutParams);
            ratingCustomControl.addView(ratingControl);
        }


        //добавляем контрол для шкалы

        if(thisTracking.GetScaleCustomization() == TrackingCustomization.Required || thisTracking.GetScaleCustomization() == TrackingCustomization.Optional){

            hintForScaleText = new TextView(this);
            hintForScaleText.setText("Добавьте шкалу:");
            hintForScaleText.setTextSize(20);
            hintForScaleText.setPadding(10, 10, 10, 10);
            hintForScaleText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            LinearLayout.LayoutParams scaleHint = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            scaleHint.setMargins(10, 40, 10, 10);

            hintForScaleText.setLayoutParams(scaleHint);
            hintForScale.addView(hintForScaleText);

            scaleControl = new EditText(getApplication());
            scaleControl.setHint("Ваше число");
            scaleControl.setTextColor(getResources().getColor(R.color.cardview_dark_background));
            scaleControl.setInputType(InputType.TYPE_CLASS_NUMBER);
            KeyListener keyListener = DigitsKeyListener.getInstance("1234567890");
            scaleControl.setKeyListener(keyListener);
            scaleControl.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if(thisTracking.GetScaleCustomization() == TrackingCustomization.Required){

                scaleControl.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

                stateForScale = 2;

            }

            if(thisTracking.GetScaleCustomization() == TrackingCustomization.Optional){

                scaleControl.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_for_not_definetly), PorterDuff.Mode.SRC_ATOP);

                stateForScale = 1;

            }
            LinearLayout.LayoutParams scaleLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            scaleLayoutParams.setMargins(10,40,10,10);

            scaleControl.setLayoutParams(scaleLayoutParams);
            scaleCustomControl.addView(scaleControl);
        }

        addNewEvent = (Button) findViewById(R.id.addEvent);
        id = intent.getStringExtra("trackingId");

        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                boolean flag_for_comment = false;
                boolean flag_for_scale = false;
                boolean flag_for_rating = false;

                String textRating;
                int intRating;
                Rating newRating = new Rating(null);

                UUID trackingId = UUID.fromString(id);

                //Обрабатываем состояние кастомизации комментария
                if (stateForComment == 2) {
                    if(!commentControl.getText().toString().isEmpty()) {
                        commentForEvent = commentControl.getText().toString();
                        flag_for_comment = true;
                    }
                }
                if (stateForComment == 1) {
                    flag_for_comment = true;
                    if (commentControl.getText().toString().isEmpty()) {
                        commentForEvent = null;
                    } else {
                        commentForEvent = commentControl.getText().toString();
                    }
                }
                if (stateForComment == 0) {
                    flag_for_comment = true;
                    commentForEvent = null;
                }


                //обрабатываем состояние кастомизации шкалы
                if (stateForScale == 2) {
                    if (!scaleControl.getText().toString().isEmpty()) {
                        scaleForEvent = Double.parseDouble(scaleControl.getText().toString());
                        flag_for_scale = true;
                    }
                }
                        //Toast.makeText(getApplicationContext(), "Введите обязательные данные о событии", Toast.LENGTH_SHORT).show();
                if (stateForScale == 1) {
                    flag_for_scale = true;
                    if(scaleControl.getText().toString().isEmpty()){
                        scaleForEvent = null;
                    }else{
                        scaleForEvent = Double.parseDouble(scaleControl.getText().toString());
                    }
                }
                if (stateForScale == 0) {
                    flag_for_scale = true;
                    scaleForEvent = null;
                }


                //обрабатываем состояние кастомизации рейтинга
                if (stateForRating == 2) {
                    if((int) ratingControl.getRating()!=0) {
                        ratingForEvent = (int) ratingControl.getRating()*2;
                        newRating = new Rating(ratingForEvent);
                        flag_for_rating=true;
                    }
                }
                if(stateForRating == 1) {
                    flag_for_rating = true;
                    if ((int) ratingControl.getRating() != 0) {
                        ratingForEvent = (int) ratingControl.getRating()*2;
                        newRating = new Rating(ratingForEvent);
                    } else {
                        newRating = null;
                    }
                }

                if(stateForRating==0){
                    flag_for_rating = true;
                    newRating = null;
                }


                String comment = commentForEvent;
                Double scale = scaleForEvent;

                Event newEvent = new Event(UUID.randomUUID(), trackingId, scale, newRating, comment);
                trackingId = UUID.fromString(id);
                trackingService = new

                        TrackingService("someName", trackingCollection);

                if(flag_for_comment&&flag_for_rating&&flag_for_scale) {
                    try {
                        trackingService.AddEvent(trackingId, newEvent);
                        Toast.makeText(getApplicationContext(), "Событие добавлено", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getApplicationContext() ,UserActionsActivity.class);
                        intent.putExtra("state", "1");
                        startActivity(intent1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Введите обязательные данные о событии", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Введите обязательные данные о событии", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, UserActionsActivity.class));
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
        actionBar.setTitle("Добавить событие");



    }
}


