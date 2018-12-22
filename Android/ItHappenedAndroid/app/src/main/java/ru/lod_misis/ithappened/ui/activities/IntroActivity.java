package ru.lod_misis.ithappened.ui.activities;

import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.ui.adapters.ViewPagerAdapter;
import ru.lod_misis.ithappened.ui.fragments.IntroFragment;
import ru.lod_misis.ithappened.ui.fragments.StartIntroFragment;

public class IntroActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initViews();
        showIntro();
    }

    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
    }

    private void showIntro() {
        PagerAdapter adapter = new ViewPagerAdapter(Objects.requireNonNull(getSupportFragmentManager()));
        ((ViewPagerAdapter) adapter).addFragment(new StartIntroFragment());
        ((ViewPagerAdapter) adapter).addFragment(IntroFragment.newInstanse(R.drawable.intro_second_screen));
        ((ViewPagerAdapter) adapter).addFragment(IntroFragment.newInstanse(R.drawable.intro_third_screen));
        ((ViewPagerAdapter) adapter).addFragment(IntroFragment.newInstanse(R.drawable.intro_fourth_screen));
        viewPager.setAdapter(adapter);
    }
}
