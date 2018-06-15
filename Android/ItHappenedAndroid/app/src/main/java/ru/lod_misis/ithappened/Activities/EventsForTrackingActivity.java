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
import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.EventsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;

public class EventsForTrackingActivity extends AppCompatActivity {

    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;

    TextView hintForEvents;

    FloatingActionButton addNewEvent;
    NewTracking thisNewTracking;

    List<NewEvent> newEvents;
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
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
            trackingsCollection = StaticInMemoryRepository.getInstance();
        }else{
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
            trackingsCollection = StaticInMemoryRepository.getInstance();
        }
        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), trackingsCollection);


        thisNewTracking = trackingsCollection.GetTracking(trackingId);
        actionBar.setTitle(thisNewTracking.GetTrackingName());

        newEvents = trackingsCollection.getEventCollection(trackingId);

        for(int i = 0; i< newEvents.size(); i++){
            if(newEvents.get(i).GetStatus()){
                newEvents.remove(i);
            }
        }

        List<NewEvent> visibleNewEvents = new ArrayList<>();

        for(int i = 0; i< newEvents.size(); i++){
            if(!newEvents.get(i).GetStatus()){
                visibleNewEvents.add(newEvents.get(i));
            }
        }

        if(visibleNewEvents.size()!=0){
            hintForEvents.setVisibility(View.INVISIBLE);
        }

        setTitle(thisNewTracking.GetTrackingName());

        eventsRecycler = (RecyclerView) findViewById(R.id.eventsForTrackingRV);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(this));
        eventsAdpt = new EventsAdapter(visibleNewEvents, this, 0);
        eventsRecycler.setAdapter(eventsAdpt);


        addNewEvent = (FloatingActionButton) findViewById(R.id.addNewEvent);

        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddNewEventActivity.class);
                intent.putExtra("trackingId", thisNewTracking.GetTrackingID().toString());

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
