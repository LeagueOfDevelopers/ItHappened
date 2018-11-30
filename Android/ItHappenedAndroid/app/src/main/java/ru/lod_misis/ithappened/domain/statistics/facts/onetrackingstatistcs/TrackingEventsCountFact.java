package ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.StringParse;

/**
 * Created by Ded on 14.03.2018.
 */

public class TrackingEventsCountFact extends Fact {

    private TrackingV1 trackingV1;
    private int eventCount;

    public TrackingEventsCountFact(TrackingV1 trackingV1)
    {
        trackingId = trackingV1.getTrackingId();
        this.trackingV1 = trackingV1;
        eventCount = 0;
    }


    @Override
    public void calculateData() {

        for (EventV1 eventV1 : trackingV1.getEventCollection()) {
            if (!eventV1.isDeleted()) eventCount++;
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
        return String.format("Событие <b>%s</b> произошло <b>%s</b> %s", trackingV1.getTrackingName(),
                eventCount, StringParse.time(eventCount));
    }
}
