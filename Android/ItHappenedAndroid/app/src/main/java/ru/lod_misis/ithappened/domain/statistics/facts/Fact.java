package ru.lod_misis.ithappened.domain.statistics.facts;

import java.util.UUID;

import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustartionModel;

/**
 * Created by Ded on 14.03.2018.
 */

public abstract class Fact {
    protected UUID trackingId;
    protected UUID eventId;
    protected Double priority;
    protected IllustartionModel illustartion = null;
    public abstract void calculateData();
    public Double getPriority() { return priority; }

    protected abstract void calculatePriority();
    public abstract String textDescription();
    public UUID getTrackingId() {return trackingId;}
    public UUID getEventId() {return eventId;}
    public IllustartionModel getIllustration(){ return illustartion; }
}
