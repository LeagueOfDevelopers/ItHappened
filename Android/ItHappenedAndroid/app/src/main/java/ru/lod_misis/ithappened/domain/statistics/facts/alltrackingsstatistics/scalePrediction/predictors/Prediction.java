package ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.predictors;

import java.util.List;

public class Prediction {

    private List<Double> predictions;
    private double standardDeviation; // deviation dor each prediction. sqrt(Variance)

    public Prediction(List<Double> predictions, double standardDeviation) {
        this.predictions = predictions;
        this.standardDeviation = standardDeviation;
    }

    public List<Double> getPredictions() {
        return predictions;
    }

    public double getStandartDeviation() {
        return standardDeviation;
    }
}
