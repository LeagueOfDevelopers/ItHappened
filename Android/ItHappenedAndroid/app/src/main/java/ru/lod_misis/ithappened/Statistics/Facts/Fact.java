package ru.lod_misis.ithappened.Statistics.Facts;

/**
 * Created by Ded on 14.03.2018.
 */

public abstract class Fact {
    protected Double priority;
    public abstract Double getPriority();
    protected abstract void calculatePriority();
    public abstract String TextDescription();
}
