package com.example.ithappenedandroid.StatisticsHelpers;

import com.example.ithappenedandroid.Domain.Tracking;

import java.util.List;

public class DiagramsStatisticsHelper {

    private Tracking tracking;

    public DiagramsStatisticsHelper(Tracking tracking) {
        this.tracking = tracking;
    }

    public int getTrackingEventsCount(){
        return tracking.GetEventCollection().size();
    }

    public int getAllEventsCount(List<Tracking> trackings){
        int eventsCount = 0;
        trackings.remove(tracking);
        for(Tracking tracking : trackings){
            eventsCount+=tracking.GetEventCollection().size();
        }
        return eventsCount;
    }

}
