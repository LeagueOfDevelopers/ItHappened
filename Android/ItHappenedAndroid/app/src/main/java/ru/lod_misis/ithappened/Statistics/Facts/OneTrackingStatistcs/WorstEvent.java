package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;

/**
 * Created by Ded on 23.03.2018.
 */

public class WorstEvent extends Fact {

    Tracking tracking;
    List<Event> eventCollection;
    Event worstEvent;

    public WorstEvent(Tracking tracking)
    {
        this.tracking = tracking;
        this.trackingId = tracking.GetTrackingID();
        eventCollection = new ArrayList<>();
    }

    @Override
    public void calculateData() {

        for(Event event : tracking.GetEventCollection()){
            if(!event.GetStatus() && event.GetRating()!=null){
                eventCollection.add(event);
            }
        }

        worstEvent = eventCollection.get(0);

        for(Event event : eventCollection)
        {
            if (worstEvent.GetRating().getRating() > event.GetRating().getRating())
                worstEvent = event;
        }

        illustartion = new IllustartionModel(IllustrationType.EVENTREF);
        illustartion.setEventRef(worstEvent);

        calculatePriority();
    }

    @Override
    public void calculatePriority() {
        priority = 10.0 -worstEvent.GetRating().getRating();
    }

    @Override
    public String textDescription() {
        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);

        String toReturn = String.format("Худшее событие %s произошло %s, " +
                "вы поставили ему %s", tracking.getTrackingName(),
                format.format(worstEvent.GetEventDate()), worstEvent.GetRating().getRating());

        if (worstEvent.GetComment() == null) return toReturn;

        return String.format(toReturn, " с комментарием %s", worstEvent.GetComment());
    }

    public Event getWorstEvent() { return worstEvent; }

}
