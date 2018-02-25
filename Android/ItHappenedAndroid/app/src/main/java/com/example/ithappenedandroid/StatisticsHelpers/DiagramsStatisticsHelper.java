package com.example.ithappenedandroid.StatisticsHelpers;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;

import java.util.List;

public class DiagramsStatisticsHelper {

    private Tracking tracking;
    List<Event> eventsCollection;

    public DiagramsStatisticsHelper(Tracking tracking)
    {
        this.tracking = tracking;
        eventsCollection = tracking.GetEventCollection();

        for(int i=0;i<eventsCollection.size();i++){
            if(eventsCollection.get(i).GetStatus()){
                eventsCollection.remove(i);
            }
        }
    }

    public int getTrackingEventsCount(){
        return eventsCollection.size();
    }

    public int getAllEventsCount(List<Tracking> trackings){
        int eventsCount = 0;
        trackings.remove(tracking);
        for(Tracking tracking : trackings){
           List<Event> thisEventCollection = tracking.GetEventCollection();
           for(Event event : thisEventCollection){
               if(!event.GetStatus()){
                   eventsCount++;
               }
           }
        }
        return eventsCount;
    }

}
