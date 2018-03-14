package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.FunctionApplicability;


public class AvrgRatingFact extends Fact {


    Tracking tracking;
    List<Event> eventCollection = new ArrayList<>();
    Double averageValue;
    Double priority;

    public AvrgRatingFact(Tracking tracking){
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
            if(event.GetRating() != null){
                sumValue+=event.GetRating().GetRatingValue();
                count++;
            }
        }

        averageValue = sumValue/count;
        calculatePriority();

        return averageValue;
    }

    public Double getPriority(){ return priority; }

    @Override
    protected void calculatePriority() {
        Math.sqrt(averageValue);
    }

    @Override
    public String TextDescription() {
        return null;
    }

}
