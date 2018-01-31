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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.UUID;

/**
 * Created by Пользователь on 18.01.2018.
 */

public class EditTrackingActivity extends AppCompatActivity {

    ITrackingRepository trackingRepository = new StaticInMemoryRepository(getApplicationContext()).getInstance();
    TrackingService trackingService = new TrackingService("testUser", trackingRepository);

    UUID trackingId;

    RelativeLayout scaleCstm;
    RelativeLayout textCstm;
    RelativeLayout ratingCstm;

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

    Button editTrackingBtn;

    int stateForScale;
    int stateForText;
    int stateForRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tracking);

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
        actionBar.setTitle("Изменить отслеживание");

        Intent intent = getIntent();
        trackingId = UUID.fromString(intent.getStringExtra("trackingId"));
        Tracking editableTracking = trackingRepository.GetTracking(trackingId);

        editTrackingBtn = (Button) findViewById(R.id.editTrack);

        trackingTitleControl = (EditText) findViewById(R.id.editTitleOfTracking);

        scaleCstm = (RelativeLayout) findViewById(R.id.editSclCust);
        textCstm = (RelativeLayout) findViewById(R.id.editTextCstm);
        ratingCstm = (RelativeLayout) findViewById(R.id.editRtCust);

        scaleCard = (CardView) findViewById(R.id.editScaleCard);
        textCard = (CardView) findViewById(R.id.editTextCard);
        ratingCard = (CardView) findViewById(R.id.editRatingCard);

        scaleText = (TextView) findViewById(R.id.editScaleEn);
        textText = (TextView) findViewById(R.id.editTextEn);
        ratingText = (TextView) findViewById(R.id.rtEnEdit);

        stateForScale = setLayoutStyle(editableTracking.GetScaleCustomization(), scaleText, scaleCard);
        stateForText = setLayoutStyle(editableTracking.GetCommentCustomization(), textText, textCard);
        stateForRating = setLayoutStyle(editableTracking.GetRatingCustomization(), ratingText, ratingCard);

        trackingTitleControl.setText(editableTracking.GetTrackingName());

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

        editTrackingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trackingTitle = trackingTitleControl.getText().toString();

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

                Intent intent = getIntent();

                trackingService.EditTracking(trackingId, scaleCustom, ratingCustom, textCustom, trackingTitleControl.getText().toString());
                Toast.makeText(getApplicationContext(), "Отслеживание изменено", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(getApplicationContext(), UserActionsActivity.class);
                startActivity(intent1);
            }
        });

    }

    private int setLayoutStyle(TrackingCustomization customization, TextView text, CardView card){

        int state = 0;

        switch (customization){
            case None:
                text.setText("Не выбрано");
                card.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
                state = 0;
                break;

            case Optional:
                text.setText("Опционально");
                card.setCardBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                state = 1;
                break;

            case Required:
                text.setText("Обязательно");
                ratingCard.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                state = 2;
                break;

        }

    return state;

    }



}
