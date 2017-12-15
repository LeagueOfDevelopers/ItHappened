package com.example.ithappenedandroid.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ithappenedandroid.R;

public class AddNewTrackingActivity extends AppCompatActivity {

    LinearLayout scaleCstm;
    LinearLayout textCstm;
    LinearLayout ratingCstm;

    CardView scaleCard;
    CardView textCard;
    CardView ratingCard;

    TextView scaleText;
    TextView textText;
    TextView ratingText;

    int stateForScale;
    int stateForText;
    int stateForRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewtracking);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Отслеживать");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, TrackingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

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

    }
}
