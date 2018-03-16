package ru.lod_misis.ithappened.Statistics.Facts;

import java.util.UUID;

/**
 * Created by Ded on 14.03.2018.
 */

public abstract class Fact {
    protected UUID trackingId;
    protected Double priority;
    public abstract void calculateData();
    public abstract Double getPriority();
    protected abstract void calculatePriority();
    public abstract String textDescription();
    public UUID getTrackingId() {return trackingId;}
}
