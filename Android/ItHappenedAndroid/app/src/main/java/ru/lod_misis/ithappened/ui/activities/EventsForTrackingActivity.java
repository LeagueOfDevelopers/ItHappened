package ru.lod_misis.ithappened.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.ui.dialog.DeleteEventDialog;
import ru.lod_misis.ithappened.ui.presenters.DeleteCallback;
import ru.lod_misis.ithappened.ui.presenters.DeleteContract;
import ru.lod_misis.ithappened.ui.presenters.EventsFragmnetCallBack;
import ru.lod_misis.ithappened.ui.recyclers.EventsAdapter;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;

public class EventsForTrackingActivity extends AppCompatActivity implements DeleteContract.DeleteView, EventsFragmnetCallBack, DeleteCallback {

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

    @Inject
    TrackingDataSource trackingsCollection;
    @Inject
    DeleteContract.DeletePresenter deletePresenter;

    // Время, когда пользователь открыл экран.
    // Нужно для сбора данных о времени, проведенном пользователем на каждом экране
    private DateTime userOpenAnActivityDateTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_history_for_tracking);
        ButterKnife.bind(this);
        userOpenAnActivityDateTime = DateTime.now();
        YandexMetrica.reportEvent(getString(R.string.metrica_enter_events_hitroy_for_tracking));

        ItHappenedApplication.getAppComponent().inject(this);

        Intent intent = getIntent();

        trackingId = UUID.fromString(intent.getStringExtra("id"));
        thisTrackingV1 = trackingsCollection.getTracking(trackingId);

        eventV1s = trackingsCollection.getEventCollection(trackingId);
        deletePresenter.attachView(this);

        setupActionBar();


        setTitle(thisTrackingV1.getTrackingName());
        initEventsRecyclerView();
    }

    private void initEventsRecyclerView() {
        for (int i = 0; i < eventV1s.size(); i++) {
            if (eventV1s.get(i).isDeleted()) {
                eventV1s.remove(i);
            }
        }

        List<EventV1> visibleEventV1s = new ArrayList<>();

        for (int i = 0; i < eventV1s.size(); i++) {
            if (!eventV1s.get(i).isDeleted()) {
                visibleEventV1s.add(eventV1s.get(i));
            }
        }

        if (visibleEventV1s.size() != 0) {
            hintForEvents.setVisibility(View.INVISIBLE);
        }
        eventsRecycler.setLayoutManager(new LinearLayoutManager(this));
        eventsAdpt = new EventsAdapter(visibleEventV1s, this, 0, trackingsCollection, this);
        eventsRecycler.setAdapter(eventsAdpt);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(thisTrackingV1.getTrackingName());
    }

    @Override
    public void onResume() {
        super.onResume();

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_events_hitroy_for_tracking));

        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddNewEventActivity.class);
                intent.putExtra("trackingId", thisTrackingV1.getTrackingId().toString());

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
        activityVisitTimeBorders.put("Start time", userOpenAnActivityDateTime.toDate());
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

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishDetailsEventActivity() {
        initEventsRecyclerView();
    }

    private void deleteEvent(UUID trackingID, UUID eventID) {
        DeleteEventDialog delete = new DeleteEventDialog();
        delete.setDeleteCallback(this);
        Bundle bundle = new Bundle();
        bundle.putString(EventDetailsActivity.TRACKING_ID, trackingID.toString());
        bundle.putString(EventDetailsActivity.EVENT_ID, eventID.toString());
        delete.setArguments(bundle);
        delete.show(getFragmentManager(), "DeleteEvent");
    }

    @Override
    public void showPopupMenu(View v, final UUID trackingID, final UUID eventID, int position) {
        PopupMenu popupMenu = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            popupMenu = new PopupMenu(this, v);

            popupMenu.inflate(R.menu.popup_menu_delete_in_list);

            popupMenu
                    .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete:
                                    deleteEvent(trackingID, eventID);
                                    return true;
                                case R.id.edit:
                                    EditEventActivity.toEditEventActivity(EventsForTrackingActivity.this, trackingID.toString(), eventID.toString());
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
            popupMenu.show();
        }
    }

    @Override
    public void finishDeleting(UUID trackingId, UUID eventId) {
        deletePresenter.okClicked(trackingId, eventId);
    }

    @Override
    public void cansel() {
        deletePresenter.canselClicked();
    }

}
