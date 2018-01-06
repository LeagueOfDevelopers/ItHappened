package com.example.ithappenedandroid.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Fragments.DatePickerFragment;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.Date;
import java.util.UUID;

public class EditEventActivity extends AppCompatActivity {

    Button editDate;
    TextView editedDateText;

    ITrackingRepository trackingCollection = StaticInMemoryRepository.getInstance();
    TrackingService trackingService = new TrackingService("testUser", trackingCollection);

    UUID trackingId;
    UUID eventId;

    Date editedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Изменить событие");

        editDate = (Button) findViewById(R.id.editDateButton);
        editedDateText = (TextView) findViewById(R.id.editedDate);

        Intent intent = getIntent();
        trackingId = UUID.fromString(intent.getStringExtra("trackingId"));
        eventId = UUID.fromString(intent.getStringExtra("eventId"));

        Tracking tracking = trackingCollection.GetTracking(trackingId);
        Event event = tracking.GetEvent(eventId);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment picker = new DatePickerFragment(editedDateText);
                picker.show(getFragmentManager(), "EditedDate");
            }
        });

    }
}
