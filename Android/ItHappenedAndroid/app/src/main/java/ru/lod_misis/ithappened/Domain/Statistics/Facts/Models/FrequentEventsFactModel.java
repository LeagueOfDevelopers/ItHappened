package ru.lod_misis.ithappened.Domain.Statistics.Facts.Models;

/**
 * Created by Ded on 09.03.2018.
 */

public class FrequentEventsFactModel {
    private Double period;
    private String trackingName;
    private String trackingId;
    private Integer color;

    public FrequentEventsFactModel(Double period, String trackingName, String trackingId, Integer color) {
        this.period = period;
        this.trackingName = trackingName;
        this.trackingId = trackingId;
        this.color = color;
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

    public Integer getColor() {
        return color;
    }
}
