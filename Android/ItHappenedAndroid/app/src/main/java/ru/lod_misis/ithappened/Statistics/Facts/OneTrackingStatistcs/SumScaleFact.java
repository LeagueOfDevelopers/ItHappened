package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;

public class SumScaleFact extends Fact{

    Tracking tracking;
    Double scaleSum;
    List<Event> eventCollection = new ArrayList<>();

    public SumScaleFact(Tracking tracking){
        this.tracking = tracking;
        trackingId = tracking.GetTrackingID();
    }

    @Override
    public void calculateData() {
        for(Event event : tracking.GetEventCollection()){
            if(!event.GetStatus()){
                eventCollection.add(event);
            }
        }

        scaleSum = 0.0;

        for(Event event : eventCollection){
            if(event.GetScale() != null){
                scaleSum+=event.GetScale();
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
        return String.format("Сумма значений <b>%s</b> для события <b>%s</b> равна <b>%s</b>",
                tracking.getScale(), tracking.getTrackingName(), scaleSum);
    }
}
