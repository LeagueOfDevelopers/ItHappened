package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;


public class AvrgRatingFact extends Fact {


    Tracking tracking;
    List<Event> eventCollection = new ArrayList<>();
    Double averageValue;
    Double priority;

    public AvrgRatingFact(Tracking tracking){
        trackingId = tracking.GetTrackingID();
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

    @Override
    public void calculateData() {
        getAvrgValue();
    }

    public Double getPriority(){ return priority; }

    @Override
    public void calculatePriority() {
        Math.sqrt(averageValue);
    }

    @Override
    public String textDescription() {
        DecimalFormat format = new DecimalFormat("#.##");
        return String.format("Среднее значение оценки для события <b>%s</b> равно <b>%.2f</b>",
                tracking.getTrackingName(), averageValue);
    }

}
