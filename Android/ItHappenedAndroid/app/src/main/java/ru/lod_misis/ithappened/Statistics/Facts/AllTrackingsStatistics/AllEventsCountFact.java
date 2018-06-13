package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import java.util.List;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;

/**
 * Created by Ded on 09.03.2018.
 */

public class AllEventsCountFact extends Fact {

    private List<NewTracking> newTrackingCollection;
    private int eventCount;

    public AllEventsCountFact(List<NewTracking> newTrackingCollection)
    {
        this.newTrackingCollection = newTrackingCollection;
        eventCount = 0;
        trackingId = null;
    }

    @Override
    public void calculateData() {
        for (NewTracking newTracking : newTrackingCollection) {
            for (NewEvent newEvent : newTracking.getNewEventCollection()) {
                if (!newEvent.isDeleted()) eventCount++;
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
