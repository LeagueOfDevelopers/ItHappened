package ru.lod_misis.ithappened.Statistics.Facts.Models.CorrelationModels;

public class CorrelationData {

    private Double BinaryCorrelation;
    private Double ScaleCorrelation;
    private Double MultinomialCorrelation;

    public CorrelationData(Double binaryCorrelation,
                           Double scaleCorrelation,
                           Double multinomialCorrelation) {
        BinaryCorrelation = binaryCorrelation;
        ScaleCorrelation = scaleCorrelation;
        MultinomialCorrelation = multinomialCorrelation;
    }

    public Double getScaleCorrelation() {
        return ScaleCorrelation;
    }

    public Double getBinaryCorrelation() {
        return BinaryCorrelation;
    }

    public Double getMultinomialCorrelation() {
        return MultinomialCorrelation;
    }
}
