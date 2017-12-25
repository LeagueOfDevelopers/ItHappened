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

    Button addNewEvent;

    EditText commentControl;
    RatingBar ratingControl;
    EditText scaleControl;

    int stateForComment = 0;
    int stateForRating = 0;
    int stateForScale = 0;

    String commentForEvent;
    float ratingForEvent;
    double scaleForEvent;

    int trackingPosition;
    ITrackingRepository trackingCollection;

    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Добавить событие");



        trackingService = new TrackingService("thisUser", trackingCollection);


        trackingCollection = StaticInMemoryRepository.getInstance();
        textCustomControl = (LinearLayout) findViewById(R.id.commentControl);
        scaleCustomControl = (LinearLayout) findViewById(R.id.scaleControl);
        ratingCustomControl = (LinearLayout) findViewById(R.id.ratingControl);

        Intent intent = getIntent();
        trackingPosition = intent.getExtras().getInt("tracking");
        id = intent.getStringExtra("trackingId");
        Tracking thisTracking = trackingCollection.GetTracking(UUID.fromString(id));

        if(thisTracking.GetCommentCustomization() == TrackingCustomization.Required || thisTracking.GetCommentCustomization() == TrackingCustomization.Optional){
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
            commentControl.setLayoutParams(commentLayoutParams);
            textCustomControl.addView(commentControl);
        }

        if(thisTracking.GetRatingCustomization() == TrackingCustomization.Required ){

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
            ratingControl.setLayoutParams(ratingLayoutParams);
            ratingCustomControl.addView(ratingControl);
        }

        if(thisTracking.GetRatingCustomization() == TrackingCustomization.Optional ){

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
            ratingControl.setLayoutParams(ratingLayoutParams);
            ratingCustomControl.addView(ratingControl);
        }

        if(thisTracking.GetScaleCustomization() == TrackingCustomization.Required || thisTracking.GetScaleCustomization() == TrackingCustomization.Optional){
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
            LinearLayout.LayoutParams commentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            scaleControl.setLayoutParams(commentLayoutParams);
            scaleCustomControl.addView(scaleControl);
        }

        addNewEvent = (Button) findViewById(R.id.addEvent);
        id = intent.getStringExtra("trackingId");

        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                UUID trackingId = UUID.fromString(id);

                if(stateForComment == 2){

                    if(commentControl.getText().toString()!=""){

                    }else{

                        commentForEvent = commentControl.getText().toString();

                    }
                }

                if(stateForComment == 1 && commentControl.getText().toString()!=""){
                    commentForEvent = commentControl.getText().toString();
                }

                if(stateForScale == 2){

                    if(scaleControl.getText().toString()!=""){

                    }else{

                        scaleForEvent = Integer.getInteger(scaleControl.getText().toString());

                    }
                }

                if(stateForScale == 1 && scaleControl.getText().toString()!=""){
                    scaleForEvent = Integer.getInteger(scaleControl.getText().toString());
                }


                if(stateForRating == 2){

                    if(ratingControl.getRating()!=0){

                    }else{

                        ratingForEvent = ratingControl.getRating();

                    }
                }

                if(stateForRating == 1 && ratingControl.getRating()!=0){
                    ratingForEvent = ratingControl.getRating();
                }

                String comment = commentForEvent;
                int intRating = (int) ratingForEvent;
                Rating newRating = new Rating(7);
                Rating rating = newRating;
                Double scale = scaleForEvent;

                Event newEvent = new Event(trackingId, UUID.randomUUID(), scale, rating, comment);
                trackingId = UUID.fromString(id);
                //trackingService.AddEvent(trackingId, newEvent);
                Tracking thisTracking = trackingCollection.GetTracking(UUID.fromString(id));
                thisTracking.AddEvent(newEvent);


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




    }
}


