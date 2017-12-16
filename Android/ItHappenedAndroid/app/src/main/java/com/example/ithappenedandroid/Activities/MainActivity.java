package com.example.ithappenedandroid.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ithappenedandroid.R;

public class MainActivity extends AppCompatActivity {

    Button startTrackingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTrackingButton = (Button) findViewById(R.id.startTrackingButton);
        startTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserActionsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClick(){
        Intent intent = new Intent(getApplicationContext(), UserActionsActivity.class);
        startActivity(intent);
    }
}
