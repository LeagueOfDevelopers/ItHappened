package ru.lod_misis.ithappened.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.synnapps.carouselview.CirclePageIndicator;

import java.util.Objects;

import javax.inject.Inject;

import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.ui.adapters.ViewPagerAdapter;
import ru.lod_misis.ithappened.ui.fragments.IntroFragment;
import ru.lod_misis.ithappened.ui.fragments.IntroLastFragment;
import ru.lod_misis.ithappened.ui.fragments.StartIntroFragment;

public class IntroActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;
    private ViewPager viewPager;
    private CirclePageIndicator circleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ItHappenedApplication.getAppComponent().inject(this);
        checkIntro();
        initViews();
        showIntro();
    }

    private void initViews() {
        circleIndicator = findViewById(R.id.circleIndicator);
        viewPager = findViewById(R.id.view_pager);
    }

    private void showIntro() {
        PagerAdapter adapter = new ViewPagerAdapter(Objects.requireNonNull(getSupportFragmentManager()));
        ((ViewPagerAdapter) adapter).addFragment(new StartIntroFragment());
        ((ViewPagerAdapter) adapter).addFragment(IntroFragment.newInstanse(R.drawable.intro_second_screen));
        ((ViewPagerAdapter) adapter).addFragment(IntroFragment.newInstanse(R.drawable.intro_third_screen));
        ((ViewPagerAdapter) adapter).addFragment(new IntroLastFragment());
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
    }

    void checkIntro() {
        if (sharedPreferences.getBoolean("Tutorial", false)) {
            Intent intent = new Intent(this, UserActionsActivity.class);
            startActivity(intent);
        }
    }
}
