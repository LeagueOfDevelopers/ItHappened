package ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.predictors;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.domain.statistics.facts.models.collections.Sequence;
import ru.lod_misis.ithappened.domain.statistics.facts.models.sequencework.LinearRegression;
import ru.lod_misis.ithappened.domain.statistics.facts.models.sequencework.SequenceAnalyzer;

public class LinearRegressionPredictor implements Predictor {

    private LinearRegression regression;
    private Sequence deviations;
    private int dataSliceLength;
    private int dataLength;
    private boolean isFitted;

    public LinearRegressionPredictor(int dataSliceLength) {
        if (dataSliceLength < 0) {
            throw new IllegalArgumentException("dataSliceLength argument cannot be lower than zero");
        }
        this.dataSliceLength = dataSliceLength;
        this.isFitted = false;
    }

    @Override
    public void fit(Sequence data) {
        dataLength = data.Length();
        Sequence trainData = data.Slice(data.Length() - dataSliceLength, data.Length());
        regression = SequenceAnalyzer.BuildLinearRegression(trainData);
        List<Double> dev = new ArrayList<>();
        for (int i = 0; i < trainData.Length(); i++) {
            dev.add(trainData.get(i) - regression.predictionInPoint(i));
        }
        deviations = new Sequence(dev);
        isFitted = true;
    }

    @Override
    public Prediction predict(int n) {
        if (isFitted) {
            List<Double> predictions = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                predictions.add(regression.predictionInPoint(dataLength + i));
            }
            return new Prediction(predictions, Math.sqrt(deviations.Var()));
        }
        return null;
    }
}
