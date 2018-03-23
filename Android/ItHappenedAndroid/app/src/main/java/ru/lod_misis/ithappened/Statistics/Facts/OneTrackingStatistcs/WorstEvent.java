package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.ArrayList;
import java.util.List;

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
        eventCollection = new ArrayList<>();
    }

    @Override
    public void calculateData() {

        for(Event event : tracking.GetEventCollection()){
            if(!event.GetStatus()){
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
        String toReturn = String.format("Худшее событие %s произошло %s, " +
                "вы поставили ему %s", tracking.getTrackingName(),
                worstEvent.GetEventDate(), worstEvent.GetRating().getRating());

        if (worstEvent.GetComment() == null) return toReturn;

        return String.format(toReturn, " с комментарием %s", worstEvent.GetComment());
    }

    public Event getWorstEvent() { return worstEvent; }

}
