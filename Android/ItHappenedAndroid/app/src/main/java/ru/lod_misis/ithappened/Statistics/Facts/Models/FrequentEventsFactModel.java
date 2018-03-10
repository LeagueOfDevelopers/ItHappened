package ru.lod_misis.ithappened.Statistics.Facts.Models;

/**
 * Created by Ded on 09.03.2018.
 */

public class FrequentEventsFactModel {
    int period;
    String trackingName;
    String trackingId;

    public FrequentEventsFactModel(int period, String trackingName, String trackingId) {
        this.period = period;
        this.trackingName = trackingName;
        this.trackingId = trackingId;
    }

    public int getPeriod() {
        return period;
    }

    public String getTrackingName() {
        return trackingName;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
