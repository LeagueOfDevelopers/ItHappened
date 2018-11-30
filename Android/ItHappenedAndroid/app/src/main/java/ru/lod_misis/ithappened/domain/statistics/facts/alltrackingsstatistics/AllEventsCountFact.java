package ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics;

import java.util.List;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.StringParse;

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
            for (EventV1 eventV1 : trackingV1.getEventCollection()) {
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
