package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Interfaces.IDataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackingLoader implements IDataLoader {

    List<Tracking> trackings = new ArrayList<Tracking>();

    @Override
    public List<Event> loadingEvents() {
        return null;
    }

    @Override
    public List<Tracking> loadingTrackings() {

        for(int i=0;i<100;i++){
            trackings.add(new Tracking("Cобытие", UUID.randomUUID(), TrackingCustomization.Required,TrackingCustomization.None, TrackingCustomization.None));
        }

        return trackings;
        }


    }
