package com.example.ithappenedandroid.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;

public class AddNewEventActivity extends AppCompatActivity {

    LinearLayout textCustomControl;
    LinearLayout scaleCustomControl;
    LinearLayout ratingCustomControl;

    int trackingPosition;
    ITrackingRepository trackingCollection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Добавить событие");


        trackingCollection = StaticInMemoryRepository.getInstance();
        textCustomControl = (LinearLayout) findViewById(R.id.commentControl);
        scaleCustomControl = (LinearLayout) findViewById(R.id.scaleControl);
        ratingCustomControl = (LinearLayout) findViewById(R.id.ratingControl);

        Intent intent = getIntent();
        trackingPosition = intent.getExtras().getInt("tracking");
        Tracking thisTracking = trackingCollection.GetTrackingCollection().get(trackingPosition);

        if(thisTracking.GetCommentCustomization() == TrackingCustomization.Required || thisTracking.GetCommentCustomization() == TrackingCustomization.Optional){
            EditText commentControl = new EditText(getApplication());
            commentControl.setHint("Ваш комментарий");
            commentControl.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if(thisTracking.GetCommentCustomization() == TrackingCustomization.Required){

                commentControl.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

            }

            if(thisTracking.GetCommentCustomization() == TrackingCustomization.Optional){

                commentControl.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_for_not_definetly), PorterDuff.Mode.SRC_ATOP);

            }
            LinearLayout.LayoutParams commentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            commentControl.setLayoutParams(commentLayoutParams);
            textCustomControl.addView(commentControl);
        }

        if(thisTracking.GetCounterCustomization() == TrackingCustomization.Required ){
            RatingBar ratingControl = new RatingBar(getApplication());
            ratingControl.setNumStars(5);
            ratingControl.setStepSize(1);

            LayerDrawable stars = (LayerDrawable) ratingControl.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);

            LinearLayout.LayoutParams ratingLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ratingControl.setLayoutParams(ratingLayoutParams);
            ratingCustomControl.addView(ratingControl);
        }

        if(thisTracking.GetCounterCustomization() == TrackingCustomization.Optional ){
            RatingBar ratingControl = new RatingBar(getApplication());
            ratingControl.setNumStars(5);
            ratingControl.setStepSize(1);

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
            EditText scaleControl = new EditText(getApplication());
            scaleControl.setHint("Ваше число");
            scaleControl.setInputType(InputType.TYPE_CLASS_NUMBER);
            KeyListener keyListener = DigitsKeyListener.getInstance("1234567890");
            scaleControl.setKeyListener(keyListener);
            scaleControl.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if(thisTracking.GetCommentCustomization() == TrackingCustomization.Required){

                scaleControl.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

            }

            if(thisTracking.GetCommentCustomization() == TrackingCustomization.Optional){

                scaleControl.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_for_not_definetly), PorterDuff.Mode.SRC_ATOP);

            }
            LinearLayout.LayoutParams commentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            scaleControl.setLayoutParams(commentLayoutParams);
            scaleCustomControl.addView(scaleControl);
        }

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


