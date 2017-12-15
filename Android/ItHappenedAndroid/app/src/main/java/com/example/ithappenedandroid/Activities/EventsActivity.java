package com.example.ithappenedandroid.Activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ithappenedandroid.Fragments.EventsFragment;
import com.example.ithappenedandroid.R;


public class EventsActivity extends AppCompatActivity {

    EventsFragment eventsFrg;
    FragmentTransaction fTrans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_history);

        eventsFrg = new EventsFragment();

        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.eventsFrg, eventsFrg);
        fTrans.commit();
    }
}
