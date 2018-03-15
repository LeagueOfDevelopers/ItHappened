package ru.lod_misis.ithappened.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Fragments.DatePickerFragment;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;

public class EditEventActivity extends AppCompatActivity {

    Button editEvent;
    Button editDate;
    TextView editedDateText;

    //states
    int commentState = 0;
    int ratingState = 0;
    int scaleState = 0;

    //hints views
    TextView commentHintText;
    TextView ratingHintText;
    TextView scaleHintText;

    //controls views
    EditText commentControlWidget;
    RatingBar ratingControlWidget;
    EditText scaleControlWidget;

    //hints
    LinearLayout commentHint;
    LinearLayout ratingHint;
    LinearLayout scaleHint;

    //controls
    LinearLayout commentControl;
    LinearLayout ratingControl;
    LinearLayout scaleControl;

    ITrackingRepository trackingCollection;
    TrackingService trackingService;

    UUID trackingId;
    UUID eventId;

    TrackingCustomization commentCustm;
    TrackingCustomization ratingCustm;
    TrackingCustomization scaleCustm;

    Date editedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);



        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        trackingCollection = new StaticInMemoryRepository(getApplicationContext(), sharedPreferences.getString("UserId", "")).getInstance();
        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), trackingCollection);


        editDate = (Button) findViewById(R.id.editDateButton);
        editedDateText = (TextView) findViewById(R.id.editedDate);
        editEvent = (Button) findViewById(R.id.editEventBtn);

        //find comment
        commentHint = (LinearLayout) findViewById(R.id.editCommentHint);
        commentControl = (LinearLayout) findViewById(R.id.editCommentLayout);

        //find rating
        ratingHint = (LinearLayout) findViewById(R.id.editScaleHint);
        ratingControl = (LinearLayout) findViewById(R.id.editScaleLayout);

        //find scale
        scaleHint = (LinearLayout) findViewById(R.id.editScaleHint);
        scaleControl = (LinearLayout) findViewById(R.id.editScaleLayout);

        Intent intent = getIntent();
        trackingId = UUID.fromString(intent.getStringExtra("trackingId"));
        eventId = UUID.fromString(intent.getStringExtra("eventId"));

        Tracking tracking = trackingCollection.GetTracking(trackingId);
        Event event = tracking.GetEvent(eventId);

        commentCustm = tracking.GetCommentCustomization();
        ratingCustm = tracking.GetRatingCustomization();
        scaleCustm = tracking.GetScaleCustomization();

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);

        editedDateText.setText(format.format(event.GetEventDate()));


        //add control for comment
        if(commentCustm!=TrackingCustomization.None) {
            commentHintText = new TextView(this);
            commentHintText.setText("Измените комментарий:");
            commentHintText.setTextSize(20);
            commentHintText.setPadding(10, 10, 10, 10);
            commentHintText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            LinearLayout.LayoutParams commentHint = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            commentHint.setMargins(10, 10, 10, 10);

            commentHintText.setLayoutParams(commentHint);
            commentControl.addView(commentHintText);

            commentControlWidget = new EditText(this);
            commentControlWidget.setText(event.GetComment());
            commentControlWidget.setTextColor(getResources().getColor(R.color.cardview_dark_background));

            if (commentCustm == TrackingCustomization.Required) {
                commentState = 2;
                commentControlWidget.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
            if (commentCustm == TrackingCustomization.Optional){
                commentState = 1;
            commentControlWidget.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_for_not_definetly), PorterDuff.Mode.SRC_ATOP);
        }

            LinearLayout.LayoutParams commentControlLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            commentControlLayoutParams.setMargins(10,10,10,10);

            commentControlWidget.setLayoutParams(commentControlLayoutParams);
            commentControl.addView(commentControlWidget);
        }


        //add control for rating
        if(tracking.GetRatingCustomization()!=TrackingCustomization.None){

            ratingHintText = new TextView(this);
            ratingHintText.setText("Измените оценку:");
            ratingHintText.setTextSize(20);
            ratingHintText.setPadding(10, 10, 10, 10);
            ratingHintText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            LinearLayout.LayoutParams ratingHint = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            ratingHint.setMargins(10, 10, 10, 10);

            ratingHintText.setLayoutParams(ratingHint);
            ratingControl.addView(ratingHintText);

            ratingControlWidget = new RatingBar(getApplication());
            ratingControlWidget.setNumStars(5);
            ratingControlWidget.setStepSize(1/2);

            if(tracking.GetRatingCustomization()==TrackingCustomization.Optional){

                ratingState = 1;

                LayerDrawable stars = (LayerDrawable) ratingControlWidget.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.color_for_not_definetly), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
            }
            if(tracking.GetRatingCustomization()==TrackingCustomization.Required){

                ratingState = 2;

                LayerDrawable stars = (LayerDrawable) ratingControlWidget.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
            }
            if(event.GetRating()!=null) {
                ratingControlWidget.setRating(event.GetRating().GetRatingValue() / 2.0F);
            }
            LinearLayout.LayoutParams ratingControlLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ratingControlLayoutParams.setMargins(10,10,10,10);

            ratingControlWidget.setLayoutParams(ratingControlLayoutParams);
            ratingControl.addView(ratingControlWidget);
        }


        //add control for scale

        if(tracking.GetScaleCustomization()!=TrackingCustomization.None){

            scaleHintText = new TextView(this);
            scaleHintText.setText("Измените шкалу:");
            scaleHintText.setTextSize(20);
            scaleHintText.setPadding(10, 10, 10, 10);
            scaleHintText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            LinearLayout.LayoutParams scaleHint = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            scaleHint.setMargins(10, 10, 10, 10);

            scaleHintText.setLayoutParams(scaleHint);
            scaleControl.addView(scaleHintText);

            scaleControlWidget = new EditText(this);
            if(event.GetScale()!=null) {
                scaleControlWidget.setText(event.GetScale().toString());
            }
            scaleControlWidget.setTextColor(getResources().getColor(R.color.cardview_dark_background));
            KeyListener keyListener = DigitsKeyListener.getInstance("1234567890.-");
            scaleControlWidget.setKeyListener(keyListener);

            if(tracking.GetScaleCustomization()==TrackingCustomization.Optional){
                scaleState = 1;

                scaleControlWidget.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_for_not_definetly), PorterDuff.Mode.SRC_ATOP);

            }
            if(tracking.GetScaleCustomization()==TrackingCustomization.Required){
                scaleState = 2;

                scaleControlWidget.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

            }

            LinearLayout.LayoutParams scaleControlLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            scaleControlLayoutParams.setMargins(10,10,10,10);

            scaleControlWidget.setLayoutParams(scaleControlLayoutParams);
            scaleControl.addView(scaleControlWidget);

        }

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment picker = new DatePickerFragment(editedDateText);
                picker.show(getFragmentManager(), "EditedDate");
            }
        });

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean commentFlag = true;
                Boolean ratingFlag = true;
                Boolean scaleFlag = true;

                String comment = null;
                Double scale = null;
                Rating rating = null;


                try {
                    if (commentState == 1 && !commentControlWidget.getText().toString().isEmpty()) {
                        comment = commentControlWidget.getText().toString();
                    }

                    if (ratingState == 1 && ratingControlWidget.getRating() != 0) {
                        rating = new Rating(Math.round((ratingControlWidget.getRating()) * 2));
                    }

                    if (scaleState == 1 && !scaleControlWidget.getText().toString().isEmpty()) {
                        scale = Double.parseDouble(scaleControlWidget.getText().toString());
                    }

                    if (commentState == 2 && commentControlWidget.getText().toString().isEmpty()) {
                        commentFlag = false;
                    }

                    if (commentState == 2 && !commentControlWidget.getText().toString().isEmpty()) {
                        comment = commentControlWidget.getText().toString();
                    }

                    if (ratingState == 2 && ratingControlWidget.getRating() == 0) {
                        ratingFlag = false;
                    }

                    if (ratingState == 2 && ratingControlWidget.getRating() != 0) {
                        rating = new Rating(Math.round((ratingControlWidget.getRating()) * 2));
                    }

                    if (scaleState == 2 && scaleControlWidget.getText().toString().isEmpty()) {
                        scaleFlag = false;
                    }

                    if (scaleState == 2 && !scaleControlWidget.getText().toString().isEmpty()) {
                        scale = Double.parseDouble(scaleControlWidget.getText().toString());
                    }


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

                    try {
                        editedDate = simpleDateFormat.parse(editedDateText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (commentFlag && ratingFlag && scaleFlag) {
                        try {
                            trackingService.EditEvent(trackingId, eventId, scale, rating, comment, editedDate);
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "ex", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String toastMessage = "Введите обязательные данные о событии: ";
                        if (commentFlag == false) {
                            toastMessage += "комментарий, ";
                        }
                        if (ratingFlag == false) {
                            toastMessage += "оценку, ";
                        }
                        if (scaleFlag == false) {
                            toastMessage += "шкалу, ";
                        }

                        Toast.makeText(getApplicationContext(), toastMessage.substring(0, toastMessage.length() - 2) + "!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Введите нормальные данные шкалы!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Изменить событие");
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
}
