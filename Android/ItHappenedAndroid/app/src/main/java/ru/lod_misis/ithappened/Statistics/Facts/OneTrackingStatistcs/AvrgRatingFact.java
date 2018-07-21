package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;


public class AvrgRatingFact extends Fact {


    TrackingV1 trackingV1;
    List<EventV1> eventV1Collection = new ArrayList<>();
    Double averageValue;
    Double priority;

    public AvrgRatingFact(TrackingV1 trackingV1){
        trackingId = trackingV1.GetTrackingID();
        this.trackingV1 = trackingV1;
    }

    public Double getAvrgValue(){

        for(EventV1 eventV1 : trackingV1.GetEventHistory()){
            if(!eventV1.GetStatus()){
                eventV1Collection.add(eventV1);
            }
        }

        Double sumValue = 0.0;
        int count = 0;

        for(EventV1 eventV1 : eventV1Collection){
            if(eventV1.GetRating() != null){
                sumValue+= eventV1.GetRating().GetRatingValue();
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
