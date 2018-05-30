package ru.lod_misis.ithappened.Statistics.Facts.Models.Trends;

import java.util.UUID;

public class TrendDelta {

    private TrendChangingPoint Point;
    private Double NewAverange;

    public TrendDelta(TrendChangingPoint point, Double newAverange) {
        Point = point;
        NewAverange = newAverange;
    }

    public TrendChangingPoint getPoint() {
        return Point;
    }

    public Double getNewAverange() {
        return NewAverange;
    }

    public Double getAverangeDelta() {
        return NewAverange - Point.getAverangeValue();
    }
}
