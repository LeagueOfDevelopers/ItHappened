package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import java.util.List;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;

/**
 * Created by Ded on 09.03.2018.
 */

public class AllEventsCountFact extends Fact {

    private List<TrackingV1> trackingV1Collection;
    private int eventCount;

    public AllEventsCountFact(List<TrackingV1> trackingV1Collection)
    {
        this.trackingV1Collection = trackingV1Collection;
        eventCount = 0;
        trackingId = null;
    }

    @Override
    public void calculateData() {
        for (TrackingV1 trackingV1 : trackingV1Collection) {
            for (EventV1 eventV1 : trackingV1.getEventV1Collection()) {
                if (!eventV1.isDeleted()) eventCount++;
            }
        }
        calculatePriority();
    }

    public int getEventCount() {
        return eventCount;
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
        return String.format("У вас произошло уже <b>%s</b> %s!",
                eventCount, StringParse.event(eventCount));
    }
}
