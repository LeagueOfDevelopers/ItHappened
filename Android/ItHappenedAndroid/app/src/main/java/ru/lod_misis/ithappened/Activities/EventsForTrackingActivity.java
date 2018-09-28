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

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.EventsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Utils.UserDataUtils;

public class EventsForTrackingActivity extends AppCompatActivity {

    @BindView(R.id.eventsForTrackingRV)
    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;

    @BindView(R.id.hintForEventsFragment)
    TextView hintForEvents;

    @BindView(R.id.addNewEvent)
    FloatingActionButton addNewEvent;
    TrackingV1 thisTrackingV1;

    List<EventV1> eventV1s;
    UUID trackingId;

    ITrackingRepository trackingsCollection;
    TrackingService trackingService;
    int trackingPosition;

    // Время, когда пользователь открыл экран.
    // Нужно для сбора данных о времени, проведенном пользователем на каждом экране
    private DateTime UserOpenAnActivityDateTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_history_for_tracking);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        UserOpenAnActivityDateTime = DateTime.now();
        YandexMetrica.reportEvent(getString(R.string.metrica_enter_events_hitroy_for_tracking));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();


        trackingId = UUID.fromString(intent.getStringExtra("id"));


        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        trackingsCollection=UserDataUtils.setUserDataSet(sharedPreferences);
        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), trackingsCollection);


        thisTrackingV1 = trackingsCollection.GetTracking(trackingId);
        actionBar.setTitle(thisTrackingV1.GetTrackingName());

        eventV1s = trackingsCollection.getEventCollection(trackingId);

        for (int i = 0; i < eventV1s.size(); i++) {
            if (eventV1s.get(i).GetStatus()) {
                eventV1s.remove(i);
            }
        }

        List<EventV1> visibleEventV1s = new ArrayList<>();

        for (int i = 0; i < eventV1s.size(); i++) {
            if (!eventV1s.get(i).GetStatus()) {
                visibleEventV1s.add(eventV1s.get(i));
            }
        }

        if (visibleEventV1s.size() != 0) {
            hintForEvents.setVisibility(View.INVISIBLE);
        }

        setTitle(thisTrackingV1.GetTrackingName());

        eventsRecycler.setLayoutManager(new LinearLayoutManager(this));
        eventsAdpt = new EventsAdapter(visibleEventV1s, this, 0);
        eventsRecycler.setAdapter(eventsAdpt);

        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddNewEventActivity.class);
                intent.putExtra("trackingId", thisTrackingV1.GetTrackingID().toString());

                YandexMetrica.reportEvent(getString(R.string.metrica_user_press_button_add_event));

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
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_event_history_for_tracking));
        Map<String, Object> activityVisitTimeBorders = new HashMap<>();
        activityVisitTimeBorders.put("Start time", UserOpenAnActivityDateTime.toDate());
        activityVisitTimeBorders.put("End time", DateTime.now().toDate());
        YandexMetrica.reportEvent(getString(R.string.metrica_user_time_on_activity_events_for_tracking), activityVisitTimeBorders);
    }

    @Override
    protected void onStop() {
        super.onStop();
        YandexMetrica.reportEvent(getString(R.string.metrica_user_last_activity_events_for_tracking));
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
