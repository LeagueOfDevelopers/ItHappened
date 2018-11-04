package ru.lod_misis.ithappened.Domain.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.IllustrationType;

/**
 * Created by Ded on 23.03.2018.
 */

public class WorstEvent extends Fact {

    TrackingV1 trackingV1;
    List<EventV1> eventV1Collection;
    EventV1 worstEventV1;

    public WorstEvent(TrackingV1 trackingV1)
    {
        this.trackingV1 = trackingV1;
        this.trackingId = trackingV1.getTrackingId();
        eventV1Collection = new ArrayList<>();
    }

    @Override
    public void calculateData() {

        for(EventV1 eventV1 : trackingV1.getEventHistory()){
            if(!eventV1.isDeleted() && eventV1.getRating()!=null){
                eventV1Collection.add(eventV1);
            }
        }

        worstEventV1 = eventV1Collection.get(0);

        for(EventV1 eventV1 : eventV1Collection)
        {
            if (worstEventV1.getRating().getRating() > eventV1.getRating().getRating()
                    && worstEventV1.getEventDate().after(eventV1.getEventDate()))
                worstEventV1 = eventV1;
        }

        illustartion = new IllustartionModel(IllustrationType.EVENTREF);
        illustartion.setEventV1Ref(worstEventV1);

        calculatePriority();
    }

    @Override
    public void calculatePriority() {
        priority = 10.0 - worstEventV1.getRating().getRating();
    }

    @Override
    public String textDescription() {
        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        String toReturn = String.format("Событие <b>%s</b> с самым низким рейтингом <b>%s</b> произошло <b>%s</b>, ",
                trackingV1.getTrackingName(),
                decimalFormat.format(worstEventV1.getRating().getRating()/2.0),
                format.format(worstEventV1.getEventDate()));

        if (worstEventV1.getComment() == null) return toReturn;

        return String.format(toReturn, " с комментарием <b>%s</b>", worstEventV1.getComment());
    }

    public EventV1 getWorstEventV1() { return worstEventV1; }

}
