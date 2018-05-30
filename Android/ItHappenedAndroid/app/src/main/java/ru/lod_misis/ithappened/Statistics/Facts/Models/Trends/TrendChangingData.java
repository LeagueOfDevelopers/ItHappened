package ru.lod_misis.ithappened.Statistics.Facts.Models.Trends;

public class TrendChangingData {

    private int EventInCollectionId;
    private double AlphaCoefficient;

    public TrendChangingData(int eventInCollectionId, double alphaCoefficient) {
        EventInCollectionId = eventInCollectionId;
        AlphaCoefficient = alphaCoefficient;
    }

    public double getAlphaCoefficient() {
        return AlphaCoefficient;
    }

    public int getEventInCollectionId() {
        return EventInCollectionId;
    }
}
