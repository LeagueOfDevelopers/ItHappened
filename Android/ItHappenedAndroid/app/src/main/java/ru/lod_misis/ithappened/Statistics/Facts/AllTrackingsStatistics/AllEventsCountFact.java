package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;

/**
 * Created by Ded on 09.03.2018.
 */

public class AllEventsCountFact extends Fact {

    private List<Tracking> trackingCollection;
    private int eventCount;

    public AllEventsCountFact(List<Tracking> trackingCollection)
    {
        this.trackingCollection = trackingCollection;
        eventCount = 0;
    }

    public int getEventCount()
    {
        for (Tracking tracking: trackingCollection) {
            for (Event event: tracking.getEventCollection()) {
                if (!event.isDeleted()) eventCount++;
            }
        }
        calculatePriority();
        return eventCount;
    }

    @Override
    public Double getPriority() {
        return priority;
    }

    @Override
    protected void calculatePriority() {
        priority = Math.log(eventCount);
    }

    @Override
    public String TextDescription() {
        return String.format("У вас произошло уже %s событий!", eventCount);
    }
}
