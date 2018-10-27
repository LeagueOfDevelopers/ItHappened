package ru.lod_misis.ithappened.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.lod_misis.ithappened.Fragments.EventsFragment;
import ru.lod_misis.ithappened.R;

public class EventsRefActivity extends AppCompatActivity {

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_ref);

        Bundle bundle = new Bundle();
        bundle.putLong("dateFrom" , getIntent().getLongExtra("dateFrom" , 0));
        bundle.putLong("dateTo" , getIntent().getLongExtra("dateTo" , 0));

        EventsFragment eventsFragment = new EventsFragment();
        eventsFragment.setArguments(bundle);
        android.app.FragmentTransaction fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.historyRefContainer , eventsFragment);
        fTrans.commit();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch ( item.getItemId() ) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed () {
        this.finish();

    }
}
