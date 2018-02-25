package com.example.ithappenedandroid.Activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.ithappenedandroid.Fragments.Statistics.DiagrammsFragment;
import com.example.ithappenedandroid.Fragments.Statistics.GraphsFragment;
import com.example.ithappenedandroid.Fragments.Statistics.TextFragment;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.ViewPagerAdapter;

public class TrackingStatisticsActivity extends AppCompatActivity {
        private Toolbar toolbar;
        private TabLayout tabLayout;
        private ViewPager viewPager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_statistics);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            final Drawable upArrow = getResources().getDrawable(R.mipmap.leftt);
            upArrow.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setTitle("Статистика");

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);


            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tablayout);
            tabLayout.setupWithViewPager(viewPager);


        }

        private void setupViewPager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new TextFragment(), "Текст");
            adapter.addFragment(new DiagrammsFragment(), "Диаграммы");
            adapter.addFragment(new GraphsFragment(), "Графики");
            viewPager.setAdapter(adapter);
        }

    }

