package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;

/**
 * Created by Пользователь on 09.03.2018.
 */

public class AvrgScaleFact extends Fact {

    Tracking tracking;
    List<Event> eventCollection = new ArrayList<>();
    Double priority;
    Double averageValue;

    public AvrgScaleFact(Tracking tracking){
        this.tracking = tracking;
    }

    public Double getAvrgValue(){

        for(Event event : tracking.GetEventCollection()){
            if(!event.GetStatus()){
                eventCollection.add(event);
            }
        }

        Double sumValue = 0.0;
        int count = 0;

        for(Event event : eventCollection){
            if(event.GetScale() != null){
                sumValue+=event.GetScale();
                count++;
            }
        }

        averageValue = sumValue/count;
        calculatePriority();

        return averageValue;

    }

    @Override
    public Double getPriority(){
        return priority;
    }

}
