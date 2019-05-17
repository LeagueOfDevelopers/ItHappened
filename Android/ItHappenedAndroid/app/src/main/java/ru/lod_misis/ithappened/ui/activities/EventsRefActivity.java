package ru.lod_misis.ithappened.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.yandex.metrica.YandexMetrica;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import ru.lod_misis.ithappened.ui.fragments.EventsFragment;
import ru.lod_misis.ithappened.R;

public class EventsRefActivity extends AppCompatActivity {

    // Время, когда пользователь открыл экран.
    // Нужно для сбора данных о времени, проведенном пользователем на каждом экране
    private DateTime userOpenAnActivityDateTime;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_ref);

        bundle = new Bundle();
        bundle.putLong("dateFrom", getIntent().getLongExtra("dateFrom", 0));
        bundle.putLong("dateTo", getIntent().getLongExtra("dateTo", 0));


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
    protected void onResume() {
        super.onResume();
        EventsFragment eventsFragment = new EventsFragment();
        eventsFragment.setArguments(bundle);
        android.app.FragmentTransaction fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.historyRefContainer, eventsFragment);
        fTrans.commit();
        userOpenAnActivityDateTime = DateTime.now();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Map<String, Object> activityVisitTimeBorders = new HashMap<>();
        activityVisitTimeBorders.put("Start time", userOpenAnActivityDateTime.toDate());
        activityVisitTimeBorders.put("End time", DateTime.now().toDate());
        YandexMetrica.reportEvent(getString(R.string.metrica_user_time_on_activity_events_ref), activityVisitTimeBorders);
    }

    @Override
    protected void onStop() {
        super.onStop();
        YandexMetrica.reportEvent(getString(R.string.metrica_user_last_activity_events_ref));
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
