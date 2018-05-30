package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;

public class FrequencyTrendChangingFact extends Fact {

    Tracking Tracking;

    public FrequencyTrendChangingFact(Tracking tracking){
        Tracking = tracking;
    }

    @Override
    public void calculateData() {

    }

    @Override
    protected void calculatePriority() {

    }

    @Override
    public String textDescription() {
        return null;
    }
}
