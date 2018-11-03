package ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Trends;

import java.util.Date;

public class TrendChangingPoint {

    private Double AverageValue;
    private Date PointEventDate;

    public TrendChangingPoint(Double averageValue,
                              Date pointEventDate) {
        AverageValue = averageValue;
        PointEventDate = pointEventDate;
    }

    public Double getAverageValue() {
        return AverageValue;
    }

    public Date getPointEventDate() {
        return PointEventDate;
    }
}
