package ru.lod_misis.ithappened.Statistics.Facts.Models.Trends;

import java.util.Date;
import java.util.UUID;

public class TrendChangingPoint {
    private UUID EventId;
    private Double AverangeValue;
    private Double AlphaCoefficient;
    private Date PointEventDate;

    public TrendChangingPoint(UUID eventId,
                              Double averangeValue,
                              Double alphaCoefficient,
                              Date pointEventDate) {
        EventId = eventId;
        AverangeValue = averangeValue;
        AlphaCoefficient = alphaCoefficient;
        PointEventDate = pointEventDate;
    }

    public UUID getEventId() {
        return EventId;
    }

    public Double getAverangeValue() {
        return AverangeValue;
    }

    public Double getAlphaCoefficient() {
        return AlphaCoefficient;
    }

    public Date getPointEventDate() {
        return PointEventDate;
    }
}
