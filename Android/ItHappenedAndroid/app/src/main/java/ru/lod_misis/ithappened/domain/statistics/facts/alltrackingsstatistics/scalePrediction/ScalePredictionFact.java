package ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.predictors.LinearRegressionPredictor;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.predictors.Prediction;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.predictors.Predictor;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.predictors.PredictorEnum;
import ru.lod_misis.ithappened.domain.statistics.facts.models.collections.Sequence;

public class ScalePredictionFact extends Fact {

    private int n_predictions;
    private Sequence data;
    private Predictor predictor;
    private Prediction prediction;
    private boolean hasEnoughData;

    private final int DATASLISELENGTH = 10; // How mush data will be used for predictions

    public ScalePredictionFact(TrackingV1 tracking, int n_predictions, PredictorEnum predictorType) {
        this.n_predictions = n_predictions;
        this.data = groupScaleByDays(tracking);

        hasEnoughData = DATASLISELENGTH <= this.data.Length();

        initializePredictor(predictorType);
    }

    @Override
    public void calculateData() {
        predictor.fit(data);
        prediction = predictor.predict(n_predictions);
        calculatePriority();
    }

    @Override
    protected void calculatePriority() {
        if (prediction.getStandartDeviation() > 0) {
            priority = (data.Max() - data.Min()) / (4 * prediction.getStandartDeviation());
        }
        else {
            priority = 40.;
        }
    }

    @Override
    public String textDescription() {
        return null;
    }

    public boolean hasEnoughData() {
        return hasEnoughData;
    }

    private void initializePredictor(PredictorEnum predictorType) {
        switch (predictorType) {
            case LinearRegressionPredictor:
                predictor = new LinearRegressionPredictor(DATASLISELENGTH);
                break;
        }
    }

    private Sequence groupScaleByDays(TrackingV1 tracking) {
        List<EventV1> sorted = sortEventCollectionByDate(tracking.getEventCollection());
        List<Double> values = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < sorted.size(); i++) {
            DateTime date = new DateTime(sorted.get(i).getEventDate());
            double scale = 0;
            for (int j = i; j < sorted.size(); j++) {
                DateTime currentDate = new DateTime(sorted.get(j).getEventDate());
                if (date.dayOfYear() == currentDate.dayOfYear() && date.year() == currentDate.year()) {
                    scale += sorted.get(j).getScale();
                }
                else {
                    index = j;
                    break;
                }
            }
            i = index;
            values.add(scale);
        }
        return new Sequence(values);
    }

    private List<EventV1> sortEventCollectionByDate(List<EventV1> events) {
        List<EventV1> sorted = new ArrayList<>(events);
        Collections.sort(sorted, new Comparator<EventV1>() {
            @Override
            public int compare(EventV1 o1, EventV1 o2) {
                return o1.getEventDate().compareTo(o2.getEventDate());
            }
        });
        return sorted;
    }
}
