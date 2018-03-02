package com.lod.ithappened.Activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lod.ithappened.Fragments.Statistics.DiagrammsFragment;
import com.lod.ithappened.Fragments.Statistics.GraphsFragment;
import com.lod.ithappened.Fragments.Statistics.TextFragment;
import com.lod.ithappened.R;
import com.lod.ithappened.StaticInMemoryRepository;
import com.lod.ithappened.ViewPagerAdapter;

import java.util.UUID;

public class TrackingStatisticsActivity extends AppCompatActivity {
        private Toolbar toolbar;
        private TabLayout tabLayout;
        private ViewPager viewPager;

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

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_statistics);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            final Drawable upArrow = getResources().getDrawable(R.mipmap.leftt);
            upArrow.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setTitle(new StaticInMemoryRepository(getApplicationContext())
                    .getInstance()
                    .GetTracking(UUID.fromString(getIntent()
                    .getStringExtra("id"))).GetTrackingName());

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

