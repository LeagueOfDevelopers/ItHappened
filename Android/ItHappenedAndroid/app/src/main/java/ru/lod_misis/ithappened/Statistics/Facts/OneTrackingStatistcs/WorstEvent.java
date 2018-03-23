package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;

/**
 * Created by Ded on 23.03.2018.
 */

public class WorstEvent extends Fact {

    Tracking tracking;
    List<Event> eventCollection;
    Event worstEvent;

    public WorstEvent(Tracking tracking)
    {
        this.tracking = tracking;
        priority = 10.0;
        
    }

    @Override
    public void calculateData() {

    }

    @Override
    public Double getPriority() {
        return null;
    }

    @Override
    public void calculatePriority() {

    }

    @Override
    public String textDescription() {
        return null;
    }

    @Override
    public String getFactName() {
        return null;
    }
}
