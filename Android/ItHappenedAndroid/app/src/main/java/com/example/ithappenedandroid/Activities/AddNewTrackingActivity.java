package com.example.ithappenedandroid.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.UUID;

public class AddNewTrackingActivity extends AppCompatActivity {

    ITrackingRepository trackingRepository;

    LinearLayout scaleCstm;
    LinearLayout textCstm;
    LinearLayout ratingCstm;

    EditText trackingTitleControl;

    CardView scaleCard;
    CardView textCard;
    CardView ratingCard;

    TextView scaleText;
    TextView textText;
    TextView ratingText;

    TrackingCustomization textCustom;
    TrackingCustomization scaleCustom;
    TrackingCustomization ratingCustom;
    String trackingTitle;

    Button addTrackingBtn;

    int stateForScale;
    int stateForText;
    int stateForRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewtracking);

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
        actionBar.setTitle("Отслеживать");

        addTrackingBtn = (Button) findViewById(R.id.addTrack);

        trackingTitleControl = (EditText) findViewById(R.id.titleOfTracking);

        scaleCstm = (LinearLayout) findViewById(R.id.scale);
        textCstm = (LinearLayout) findViewById(R.id.text);
        ratingCstm = (LinearLayout) findViewById(R.id.rating);

        scaleCard = (CardView) findViewById(R.id.scaleCard);
        textCard = (CardView) findViewById(R.id.textCard);
        ratingCard = (CardView) findViewById(R.id.ratingCard);

        scaleText = (TextView) findViewById(R.id.scaleEn);
        textText = (TextView) findViewById(R.id.textEn);
        ratingText = (TextView) findViewById(R.id.rtEn);

        stateForScale = 0;
        stateForText = 0;
        stateForRating = 0;

        scaleCstm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateForScale){
                    case 0:
                        stateForScale = 1;
                        scaleCard.setCardBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                        scaleText.setText("Опционально");
                        break;
                    case 1:
                        stateForScale = 2;
                        scaleText.setText("Обязательно");
                        scaleCard.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 2:
                        stateForScale = 0;
                        scaleText.setText("Не выбрано");
                        scaleCard.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
                        break;
                }
            }
        });


        textCstm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateForText){
                    case 0:
                        stateForText = 1;
                        textCard.setCardBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                        textText.setText("Опционально");
                        break;
                    case 1:
                        stateForText = 2;
                        textCard.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                        textText.setText("Обязательно");
                        break;
                    case 2:
                        stateForText = 0;
                        textCard.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
                        textText.setText("Не выбрано");
                        break;
                }
            }
        });

        ratingCstm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateForRating){
                    case 0:
                        stateForRating = 1;
                        ratingCard.setCardBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                        ratingText.setText("Опционально");
                        break;
                    case 1:
                        stateForRating = 2;
                        ratingCard.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                        ratingText.setText("Обязательно");
                        break;
                    case 2:
                        stateForRating = 0;
                        ratingCard.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
                        ratingText.setText("Не выбрано");
                        break;
                }
            }
        });

        addTrackingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trackingTitle = trackingTitleControl.getText().toString();

                    trackingRepository = StaticInMemoryRepository.getInstance();


                    //set properties of scale
                    switch (stateForScale) {
                        case 0:
                            scaleCustom = TrackingCustomization.None;
                            break;
                        case 1:
                            scaleCustom = TrackingCustomization.Optional;
                            break;
                        case 2:
                            scaleCustom = TrackingCustomization.Required;
                            break;
                    }

                    //set properties of text
                    switch (stateForText) {
                        case 0:
                            textCustom = TrackingCustomization.None;
                            break;
                        case 1:
                            textCustom = TrackingCustomization.Optional;
                            break;
                        case 2:
                            textCustom = TrackingCustomization.Required;
                            break;
                    }

                    //set properties of rating
                    switch (stateForRating) {
                        case 0:
                            ratingCustom = TrackingCustomization.None;
                            break;
                        case 1:
                            ratingCustom = TrackingCustomization.Optional;
                            break;
                        case 2:
                            ratingCustom = TrackingCustomization.Required;
                            break;
                    }

                    UUID trackingId = UUID.randomUUID();

                    Tracking newTracking = new Tracking(trackingTitle, trackingId, scaleCustom, ratingCustom, textCustom);
                    trackingRepository.AddNewTracking(newTracking);
                Toast.makeText(getApplicationContext(), "Отслеживание добавлено", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), UserActionsActivity.class);
                    startActivity(intent);
            }
        });

    }
}
