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
        trackingId = null;
    }

    @Override
    public void calculateData() {
        for (Tracking tracking: trackingCollection) {
            for (Event event: tracking.getEventCollection()) {
                if (!event.isDeleted()) eventCount++;
            }
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
        return String.format("У вас произошло уже <b>%s</b> событий!", eventCount);
    }
}
