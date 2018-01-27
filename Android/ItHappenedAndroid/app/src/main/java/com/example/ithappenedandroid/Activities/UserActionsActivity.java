package com.example.ithappenedandroid.Activities;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ithappenedandroid.Fragments.EventsFragment;
import com.example.ithappenedandroid.Fragments.StatisticsFragment;
import com.example.ithappenedandroid.Fragments.TrackingsFragment;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Retrofit.RetrofitRequests;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.UUID;

public class UserActionsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TrackingsFragment trackFrg;
    FragmentTransaction fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);


       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#a9a9a9"));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setTitle("Мои отслеживания");
            trackFrg = new TrackingsFragment();
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.trackingsFrg, trackFrg).addToBackStack(null);
            fTrans.commit();
        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            setTitle("Мои отслеживания");
            trackFrg = new TrackingsFragment();

            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.trackingsFrg, trackFrg).addToBackStack(null);
            fTrans.commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.my_events){
            trackFrg = new TrackingsFragment();

            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.trackingsFrg, trackFrg);
            fTrans.commit();

            setTitle("Мои отслеживания");
        }

        if (id == R.id.events_history) {
            setTitle("История событий");
            EventsFragment eventsFrg = new EventsFragment();

            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.trackingsFrg, eventsFrg);
            fTrans.commit();
        }

        if(id == R.id.statistics){
            setTitle("Статистика");
            StatisticsFragment statFrg = new StatisticsFragment();

            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.trackingsFrg, statFrg);
            fTrans.commit();
        }

        if(id == R.id.synchronisation){
            RetrofitRequests requests = new RetrofitRequests(StaticInMemoryRepository.getInstance(), getApplicationContext(), UUID.randomUUID());
            requests.syncData();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
