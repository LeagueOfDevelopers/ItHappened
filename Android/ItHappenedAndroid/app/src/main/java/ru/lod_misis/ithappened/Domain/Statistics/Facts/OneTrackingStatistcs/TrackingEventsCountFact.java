package ru.lod_misis.ithappened.Domain.Statistics.Facts.OneTrackingStatistcs;

import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.StringParse;

/**
 * Created by Ded on 14.03.2018.
 */

public class TrackingEventsCountFact extends Fact {

    TrackingV1 trackingV1;
    int eventCount;

    public TrackingEventsCountFact(TrackingV1 trackingV1)
    {
        trackingId = trackingV1.GetTrackingID();
        this.trackingV1 = trackingV1;
        eventCount = 0;
    }


    @Override
    public void calculateData() {

        for (EventV1 eventV1 : trackingV1.getEventV1Collection()) {
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
