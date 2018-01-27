package com.example.ithappenedandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.example.ithappenedandroid.R;
import com.nvanbenschoten.motion.ParallaxImageView;

public class SignInActivity extends Activity {

    ParallaxImageView mainBackground;
    TextView mainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        findControlsById();
        mainBackground.registerSensorManager();
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        mainTitle.setAnimation(animation);

        mainBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserActionsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findControlsById(){

        mainBackground = (ParallaxImageView) findViewById(R.id.mainBackground);
        mainTitle = (TextView) findViewById(R.id.mainTitle);

    }
}
