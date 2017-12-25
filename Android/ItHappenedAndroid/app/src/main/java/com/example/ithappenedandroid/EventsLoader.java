package com.example.ithappenedandroid;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Scale;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Interfaces.IDataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventsLoader implements IDataLoader {

    List<Event> events = new ArrayList<Event>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Event> loadingEvents() {

<<<<<<< HEAD
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.empty();
=======
        Double count = null;
        Scale scale = null;
        String comment = null;
>>>>>>> 525bbbfc0493cae6e60b93b9ed2b4df281eee43a

        for(int i=0;i<15;i++){
            Event event = new Event(UUID.randomUUID(),
                    UUID.randomUUID(),
                    count,
                    scale,
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
