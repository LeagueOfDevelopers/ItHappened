package ru.lod_misis.ithappened.Statistics.Facts;

import java.util.UUID;

import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;

/**
 * Created by Ded on 14.03.2018.
 */

public abstract class Fact {
    protected UUID trackingId;
    protected Double priority;
    protected IllustartionModel illustartion = null;
    public abstract void calculateData();
    public Double getPriority() { return priority; }

    protected abstract void calculatePriority();
    public abstract String textDescription();
    public UUID getTrackingId() {return trackingId;}
    public IllustartionModel getIllustration(){ return illustartion; }
}
