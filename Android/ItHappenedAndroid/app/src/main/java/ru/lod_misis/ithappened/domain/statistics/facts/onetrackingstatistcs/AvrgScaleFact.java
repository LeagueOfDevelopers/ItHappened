package ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;

/**
 * Created by Пользователь on 09.03.2018.
 */

public class AvrgScaleFact extends Fact {

    private TrackingV1 trackingV1;
    private List<EventV1> eventV1Collection = new ArrayList<>();
    Double priority;
    private Double averageValue;

    public AvrgScaleFact(TrackingV1 trackingV1){
        trackingId = trackingV1.getTrackingId();
        this.trackingV1 = trackingV1;
    }

    private Double getAverageValue(){

        for(EventV1 eventV1 : trackingV1.getEventHistory()){
            if(!eventV1.isDeleted()){
                eventV1Collection.add(eventV1);
            }
        }

        Double sumValue = 0.0;
        int count = 0;

        for(EventV1 eventV1 : eventV1Collection){
            if(eventV1.getScale() != null){
                sumValue+= eventV1.getScale();
                count++;
            }
        }

        averageValue = sumValue/count;
        calculatePriority();

        return averageValue;

    }

    @Override
    public void calculateData() {
        getAverageValue();
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
                trackingV1.getScaleName(), trackingV1.getTrackingName(), format.format(averageValue));
    }

}
