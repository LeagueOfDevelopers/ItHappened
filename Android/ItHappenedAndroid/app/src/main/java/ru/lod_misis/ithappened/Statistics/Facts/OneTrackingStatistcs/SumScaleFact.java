package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;

public class SumScaleFact extends Fact{

    NewTracking newTracking;
    Double scaleSum;
    List<NewEvent> newEventCollection = new ArrayList<>();

    public SumScaleFact(NewTracking newTracking){
        this.newTracking = newTracking;
        trackingId = newTracking.GetTrackingID();
    }

    @Override
    public void calculateData() {
        for(NewEvent newEvent : newTracking.GetEventCollection()){
            if(!newEvent.GetStatus()){
                newEventCollection.add(newEvent);
            }
        }

        scaleSum = 0.0;

        for(NewEvent newEvent : newEventCollection){
            if(newEvent.GetScale() != null){
                scaleSum+= newEvent.GetScale();
            }
        }

        calculatePriority();
    }

    public Double getPriority(){
        return priority;
    }

    @Override
    public void calculatePriority() {
        priority = 2.0;
    }

    @Override
    public String textDescription() {
        DecimalFormat format = new DecimalFormat("#.##");
        return String.format("Сумма значений <b>%s</b> для события <b>%s</b> равна <b>%s</b>",
                newTracking.getScaleName(), newTracking.getTrackingName(), format.format(scaleSum));
    }
}
