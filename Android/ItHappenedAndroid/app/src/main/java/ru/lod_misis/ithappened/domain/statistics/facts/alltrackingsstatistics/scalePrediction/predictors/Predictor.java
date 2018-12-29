package ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.predictors;

import ru.lod_misis.ithappened.domain.statistics.facts.models.collections.Sequence;

public interface Predictor {

    void fit(Sequence data);

    Prediction predict(int n);
}
