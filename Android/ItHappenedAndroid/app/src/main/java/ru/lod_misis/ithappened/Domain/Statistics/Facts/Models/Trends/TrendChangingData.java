package ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Trends;

public class TrendChangingData {

    private int ItemInCollectionId;
    private double AlphaCoefficient;

    public TrendChangingData(int itemInCollectionId, double alphaCoefficient) {
        ItemInCollectionId = itemInCollectionId;
        AlphaCoefficient = alphaCoefficient;
    }

    public double getAlphaCoefficient() {
        return AlphaCoefficient;
    }

    public int getItemInCollectionId() {
        return ItemInCollectionId;
    }

    // Полезность этого класса в том, что он позволяет классу
    // SequenceAnalyzer абстрагироваться от работы с эвентами
    // и работать просто с последовательностями чисел.
}
