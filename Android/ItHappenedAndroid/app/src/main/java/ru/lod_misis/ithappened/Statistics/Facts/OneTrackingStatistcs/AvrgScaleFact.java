package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;

/**
 * Created by Пользователь on 09.03.2018.
 */

public class AvrgScaleFact extends Fact {

    NewTracking newTracking;
    List<NewEvent> newEventCollection = new ArrayList<>();
    Double priority;
    Double averageValue;

    public AvrgScaleFact(NewTracking newTracking){
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
            if(newEvent.GetScale() != null){
                sumValue+= newEvent.GetScale();
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

    @Override
    public Double getPriority(){
        return priority;
    }

    @Override
    public void calculatePriority() {
        priority = 3.0;
    }

    @Override
    public String textDescription() {
        DecimalFormat format = new DecimalFormat("#.##");
        return String.format("Среднее значение <b>%s</b> для события <b>%s</b> равно <b>%s</b>",
                newTracking.getScaleName(), newTracking.getTrackingName(), format.format(averageValue));
    }

}
