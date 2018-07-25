package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;

/**
 * Created by Ded on 23.03.2018.
 */

public class BestEvent extends Fact {

    TrackingV1 trackingV1;
    List<EventV1> eventV1Collection;
    EventV1 bestEventV1;

    public BestEvent(TrackingV1 trackingV1)
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

        bestEventV1 = eventV1Collection.get(0);

        Date curDateTime = Calendar.getInstance(TimeZone.getDefault()).getTime();

        for(EventV1 eventV1 : eventV1Collection)
        {
            Date bestTime = eventV1.GetEventDate();
            if (bestEventV1.GetRating().getRating() <= eventV1.GetRating().getRating()
                    && 7.0 < ((double)(curDateTime.getTime() - bestTime.getTime())/1000/60/60/24)
                    && bestEventV1.GetEventDate().after(eventV1.GetEventDate()))
                bestEventV1 = eventV1;
        }

        illustartion = new IllustartionModel(IllustrationType.EVENTREF);
        illustartion.setEventV1Ref(bestEventV1);

        calculatePriority();
    }

    @Override
    public void calculatePriority() {
        priority = (double) bestEventV1.GetRating().getRating();
    }

    @Override
    public String textDescription() {
        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        String toReturn = String.format("Событие <b>%s</b> с самым высоким рейтингом <b>%s</b> произошло <b>%s</b>,",
                        trackingV1.getTrackingName(),
                decimalFormat.format(bestEventV1.GetRating().getRating()/2.0),
                format.format(bestEventV1.GetEventDate()));

        if (bestEventV1.GetComment() == null) return toReturn;

        return String.format(toReturn, " с комментарием <b>%s</b>", bestEventV1.GetComment());
    }

    public EventV1 getBestEventV1() { return bestEventV1; }
}
