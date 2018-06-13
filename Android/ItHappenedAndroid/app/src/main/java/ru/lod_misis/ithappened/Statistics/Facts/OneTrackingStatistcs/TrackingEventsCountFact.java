package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;

/**
 * Created by Ded on 14.03.2018.
 */

public class TrackingEventsCountFact extends Fact {

    NewTracking newTracking;
    int eventCount;

    public TrackingEventsCountFact(NewTracking newTracking)
    {
        trackingId = newTracking.GetTrackingID();
        this.newTracking = newTracking;
        eventCount = 0;
    }


    @Override
    public void calculateData() {

        for (NewEvent newEvent : newTracking.getNewEventCollection()) {
            if (!newEvent.isDeleted()) eventCount++;
        }
        calculatePriority();
    }

    @Override
    public Double getPriority() {
        return priority;
    }

    @Override
    public void calculatePriority() {
        priority = Math.log(eventCount);
    }

    @Override
    public String textDescription() {
        return String.format("Событие <b>%s</b> произошло <b>%s</b> %s", newTracking.getTrackingName(),
                eventCount, StringParse.time(eventCount));
    }
}
