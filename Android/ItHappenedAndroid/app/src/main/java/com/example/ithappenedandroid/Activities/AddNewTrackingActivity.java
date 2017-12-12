package com.example.ithappenedandroid.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.ithappenedandroid.R;

public class AddNewTrackingActivity extends AppCompatActivity {

    Button scaleCstmBtn;
    Button textCstmBtn;
    Button ratingCstmBtn;

    int stateForScale;
    int stateForText;
    int stateForRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewtracking);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        scaleCstmBtn = (Button) findViewById(R.id.sclCust);
        textCstmBtn = (Button) findViewById(R.id.txtCust);
        ratingCstmBtn = (Button) findViewById(R.id.rtCust);

        stateForScale = 0;
        stateForText = 0;
        stateForRating = 0;

        scaleCstmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateForScale){
                    case 0:
                        stateForScale = 1;
                        scaleCstmBtn.setBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                        break;
                    case 1:
                        stateForScale = 2;
                        scaleCstmBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 2:
                        stateForScale = 0;
                        scaleCstmBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                }
            }
        });


        textCstmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateForText){
                    case 0:
                        stateForText = 1;
                        textCstmBtn.setBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                        break;
                    case 1:
                        stateForText = 2;
                        textCstmBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 2:
                        stateForText = 0;
                        textCstmBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                }
            }
        });

        ratingCstmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateForRating){
                    case 0:
                        stateForRating = 1;
                        ratingCstmBtn.setBackgroundColor(getResources().getColor(R.color.color_for_not_definetly));
                        break;
                    case 1:
                        stateForRating = 2;
                        ratingCstmBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 2:
                        stateForRating = 0;
                        ratingCstmBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                }
            }
        });

    }
}
