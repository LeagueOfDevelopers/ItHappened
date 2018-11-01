package ru.lod_misis.ithappened.Domain.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Fact;

public class SumScaleFact extends Fact{

    TrackingV1 trackingV1;
    Double scaleSum;
    List<EventV1> eventV1Collection = new ArrayList<>();

    public SumScaleFact(TrackingV1 trackingV1){
        this.trackingV1 = trackingV1;
        trackingId = trackingV1.GetTrackingID();
    }

    @Override
    public void calculateData() {
        for(EventV1 eventV1 : trackingV1.GetEventHistory()){
            if(!eventV1.GetStatus()){
                eventV1Collection.add(eventV1);
            }
        }

        scaleSum = 0.0;

        for(EventV1 eventV1 : eventV1Collection){
            if(eventV1.GetScale() != null){
                scaleSum+= eventV1.GetScale();
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
                trackingV1.getScaleName(), trackingV1.getTrackingName(), format.format(scaleSum));
    }
}
