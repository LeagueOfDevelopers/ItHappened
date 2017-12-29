package com.example.ithappenedandroid.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FiltersActivity extends AppCompatActivity {

    Spinner trackingsSpinner;
    TrackingService trackingService;
    ITrackingRepository collection = StaticInMemoryRepository.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        trackingService = new TrackingService("testUser", collection);

        ArrayList<UUID> idCollection = new ArrayList<UUID>();
        ArrayList<String> strings = new ArrayList<String>();

        List<Tracking> trackings = new ArrayList<>();
        trackings = trackingService.GetTrackingCollection();

        for(int i=0;i<trackings.size();i++){
            strings.add(trackings.get(i).GetTrackingName());
            idCollection.add(trackings.get(i).GetTrackingID());
        }

        trackingsSpinner = (Spinner) findViewById(R.id.spinnerForTrackings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trackingsSpinner.setAdapter(adapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
