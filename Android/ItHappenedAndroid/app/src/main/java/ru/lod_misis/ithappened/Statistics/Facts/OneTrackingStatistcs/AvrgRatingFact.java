package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;


public class AvrgRatingFact extends Fact {


    NewTracking newTracking;
    List<NewEvent> newEventCollection = new ArrayList<>();
    Double averageValue;
    Double priority;

    public AvrgRatingFact(NewTracking newTracking){
        trackingId = newTracking.GetTrackingID();
        this.newTracking = newTracking;
    }

    public Double getAvrgValue(){

        for(NewEvent newEvent : newTracking.GetEventCollection()){
            if(!newEvent.GetStatus()){
                newEventCollection.add(newEvent);
            }
        }

        Double sumValue = 0.0;
        int count = 0;

        for(NewEvent newEvent : newEventCollection){
            if(newEvent.GetRating() != null){
                sumValue+= newEvent.GetRating().GetRatingValue();
                count++;
            }
        }

        averageValue = sumValue/count/2;
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
        priority = Math.sqrt(averageValue);
    }

    @Override
    public String textDescription() {
        DecimalFormat format = new DecimalFormat("#.##");
        return String.format("Среднее значение оценки для события <b>%s</b> равно <b>%s</b>",
                newTracking.getTrackingName(), format.format(averageValue));
    }

}
