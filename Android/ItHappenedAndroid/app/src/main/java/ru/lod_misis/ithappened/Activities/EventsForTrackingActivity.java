package ru.lod_misis.ithappened.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.EventsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;

public class EventsForTrackingActivity extends AppCompatActivity {

    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;

    TextView hintForEvents;

    FloatingActionButton addNewEvent;
    Tracking thisTracking;

    List<Event> events;
    UUID trackingId;

    ITrackingRepository trackingsCollection;
    TrackingService trackingService;
    int trackingPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_history_for_tracking);
    }

    @Override
    public void onResume() {
        super.onResume();

        YandexMetrica.reportEvent("Пользователь зашел в историю событий по отслеживанию");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        hintForEvents = (TextView) findViewById(R.id.hintForEventsFragment);

            trackingId = UUID.fromString(intent.getStringExtra("id"));


        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        if(sharedPreferences.getString("LastId","").isEmpty()) {
            trackingsCollection = new StaticInMemoryRepository(getApplicationContext(),
                    sharedPreferences.getString("UserId", "")).getInstance();
        }else{
            trackingsCollection = new StaticInMemoryRepository(getApplicationContext(),
                    sharedPreferences.getString("LastId", "")).getInstance();
        }
        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), trackingsCollection);


        thisTracking = trackingsCollection.GetTracking(trackingId);
        actionBar.setTitle(thisTracking.GetTrackingName());

        events = thisTracking.GetEventCollection();

        for(int i = 0;i<events.size();i++){
            if(events.get(i).GetStatus()){
                events.remove(i);
            }
        }

        List<Event> visibleEvents = new ArrayList<>();

        for(int i=0;i<events.size();i++){
            if(!events.get(i).GetStatus()){
                visibleEvents.add(events.get(i));
            }
        }

        if(visibleEvents.size()!=0){
            hintForEvents.setVisibility(View.INVISIBLE);
        }

        setTitle(thisTracking.GetTrackingName());

        eventsRecycler = (RecyclerView) findViewById(R.id.eventsForTrackingRV);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(this));
        eventsAdpt = new EventsAdapter(visibleEvents , this, 0);
        eventsRecycler.setAdapter(eventsAdpt);


        addNewEvent = (FloatingActionButton) findViewById(R.id.addNewEvent);

        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddNewEventActivity.class);
                intent.putExtra("trackingId", thisTracking.GetTrackingID().toString());

                YandexMetrica.reportEvent("Пользователь добавляет событие");

                startActivity(intent);

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent("Пользователь вышел из истории событий по отслеживанию");
    }

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
}
