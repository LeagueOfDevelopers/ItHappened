package ru.lod_misis.ithappened.Statistics.Facts.Models;

/**
 * Created by Ded on 09.03.2018.
 */

public class FrequentEventsFactModel {
    Double period;
    String trackingName;
    String trackingId;

    public FrequentEventsFactModel(Double period, String trackingName, String trackingId) {
        this.period = period;
        this.trackingName = trackingName;
        this.trackingId = trackingId;
    }

    public Double getPeriod() {
        return period;
    }

    public String getTrackingName() {
        return trackingName;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
