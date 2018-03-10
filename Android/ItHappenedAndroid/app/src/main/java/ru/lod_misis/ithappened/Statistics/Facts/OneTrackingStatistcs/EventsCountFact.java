package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.List;

import ru.lod_misis.ithappened.Domain.Tracking;

/**
 * Created by Ded on 09.03.2018.
 */

public class EventsCountFact implements Fact{
    public EventsCountFact (List<Tracking> trackingCollection)
    {
        this.trackingCollection = trackingCollection;
        eventCount = 0;
    }

    public int getEventCount()
    {
        for (Tracking tracking: trackingCollection) {
            eventCount += tracking.getEventCollection().size();
        }
        priority = Math.log(eventCount);
        return eventCount;
    }

    @Override
    public Boolean applicabilityFunction() {
        int count=0;
        for (Tracking tracking: trackingCollection) {
            count += tracking.getEventCollection().size();
            if (count>5)
                return true;
        }
        return null;
    }

    @Override
    public Double getPriority() {
        return priority;
    }

    Double priority;
    List<Tracking> trackingCollection;
    int eventCount;

}
