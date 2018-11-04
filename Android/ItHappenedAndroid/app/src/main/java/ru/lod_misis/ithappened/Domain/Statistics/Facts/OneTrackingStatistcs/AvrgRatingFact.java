package ru.lod_misis.ithappened.Domain.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Fact;


public class AvrgRatingFact extends Fact {


    TrackingV1 trackingV1;
    List<EventV1> eventV1Collection = new ArrayList<>();
    Double averageValue;
    Double priority;

    public AvrgRatingFact(TrackingV1 trackingV1){
        trackingId = trackingV1.getTrackingId();
        this.trackingV1 = trackingV1;
    }

    public Double getAvrgValue(){

        for(EventV1 eventV1 : trackingV1.getEventHistory()){
            if(!eventV1.isDeleted()){
                eventV1Collection.add(eventV1);
            }
        }

        Double sumValue = 0.0;
        int count = 0;

        for(EventV1 eventV1 : eventV1Collection){
            if(eventV1.getRating() != null){
                sumValue+= eventV1.getRating().getRating();
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
                trackingV1.getTrackingName(), format.format(averageValue));
    }

}
