package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;

/**
 * Created by Ded on 14.03.2018.
 */

public class TrackingEventsCountFact extends Fact {

    Tracking tracking;
    int eventCount;

    public TrackingEventsCountFact(Tracking tracking)
    {
        trackingId = tracking.GetTrackingID();
        this.tracking = tracking;
        eventCount = 0;
    }

    public int getEventsCount()
    {
        for (Event event: tracking.getEventCollection()) {
            if (!event.isDeleted()) eventCount++;
        }
        calculatePriority();
        return eventCount;
    }

    @Override
    public void calculateData() {
        getEventsCount();
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
        return String.format("Событие %s произошло %s раз", tracking.getTrackingName(), eventCount);
    }
}
