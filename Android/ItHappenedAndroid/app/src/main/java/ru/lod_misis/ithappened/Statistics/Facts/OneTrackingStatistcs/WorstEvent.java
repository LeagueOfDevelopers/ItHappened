package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;

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
        this.trackingId = trackingV1.GetTrackingID();
        eventV1Collection = new ArrayList<>();
    }

    @Override
    public void calculateData() {

        for(EventV1 eventV1 : trackingV1.GetEventHistory()){
            if(!eventV1.GetStatus() && eventV1.GetRating()!=null){
                eventV1Collection.add(eventV1);
            }
        }

        worstEventV1 = eventV1Collection.get(0);

        for(EventV1 eventV1 : eventV1Collection)
        {
            if (worstEventV1.GetRating().getRating() > eventV1.GetRating().getRating()
                    && worstEventV1.GetEventDate().after(eventV1.GetEventDate()))
                worstEventV1 = eventV1;
        }

        illustartion = new IllustartionModel(IllustrationType.EVENTREF);
        illustartion.setEventV1Ref(worstEventV1);

        calculatePriority();
    }

    @Override
    public void calculatePriority() {
        priority = 10.0 - worstEventV1.GetRating().getRating();
    }

    @Override
    public String textDescription() {
        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        String toReturn = String.format("Событие <b>%s</b> с самым низким рейтингом <b>%s</b> произошло <b>%s</b>, ",
                trackingV1.getTrackingName(),
                decimalFormat.format(worstEventV1.GetRating().getRating()/2.0),
                format.format(worstEventV1.GetEventDate()));

        if (worstEventV1.GetComment() == null) return toReturn;

        return String.format(toReturn, " с комментарием <b>%s</b>", worstEventV1.GetComment());
    }

    public EventV1 getWorstEventV1() { return worstEventV1; }

}
