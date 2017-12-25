package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Rating;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Interfaces.IDataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventsLoader implements IDataLoader {

    List<Event> events = new ArrayList<Event>();


    @Override
    public List<Event> loadingEvents() {
        Double count = null;
        Rating rating = null;
        String comment = null;

        for(int i=0;i<15;i++){
            Event event = new Event(UUID.randomUUID(),
                    UUID.randomUUID(),
                    count,
                    rating,
                    comment
            );
            events.add(event);
        }

        return events;
    }

    @Override
    public List<Tracking> loadingTrackings() {
        return null;
    }

}
