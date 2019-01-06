package ru.lod_misis.ithappened;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.realm.RealmList;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.Tracking;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.FunctionApplicability;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.ScalePredictionFact;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.predictors.LinearRegressionPredictor;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.predictors.Prediction;
import ru.lod_misis.ithappened.domain.statistics.facts.models.collections.Sequence;

public class ScalePredictionFactUnitTest {

//    @Test
//    public void ScalePredictionFactStressTest_FactDontRuineApplication() {
//        Random r = new Random(18);
//        int c = 1;
//        while (true) {
//            TrackingV1 trackingV1 = generateRandomTracking(r);
//            System.out.print(c + "\n");
//            System.out.print(trackingV1.getEventCollection().size() + "\n");
//            DateTime time = DateTime.now();
//            Fact fact = FunctionApplicability.ScalePredictionFactApplicability(trackingV1);
//            Duration duration = new Duration(DateTime.now().minus(time.toDate().getTime()).toDate().getTime());
//            if (fact == null) {
//                System.out.print("null\n");
//            }
//            else {
//                System.out.print(fact.textDescription() + "\n");
//                System.out.print(fact.getPriority() + "\n");
//            }
//            System.out.print(duration.toPeriod().getMillis() + "\n");
//            System.out.print("------------------------------\n");
//            c++;
//        }
//    }

    @Test
    public void ScalePredictionAccuracyTest_PredictionsAreAccurate() {
        List<Double> arr = new ArrayList<>();
        String[] dataStr = data.split(", ");
        for (int i = 0; i < 100; i++) {
            arr.add(Double.parseDouble(dataStr[i]));
        }
        LinearRegressionPredictor predictor = new LinearRegressionPredictor(10);
        predictor.fit(new Sequence(arr).Slice(0, arr.size() - 1));
        Prediction pred = predictor.predict(1);

        double epsilon = pred.getStandartDeviation();
        double prediction = pred.getPredictions().get(0);
        Assert.assertTrue(prediction - 2 * epsilon < arr.get(arr.size() - 1)
                && arr.get(arr.size() - 1) < prediction + 2 * epsilon);
    }

    private TrackingV1 generateRandomTracking(Random r) {
        int eventCount = r.nextInt(10000);
        TrackingV1 tracking = new TrackingV1();
        tracking.setEventCollection(new RealmList<EventV1>());
        tracking.setTrackingId(UUID.randomUUID());
        tracking.setScaleCustomization(TrackingCustomization.Required);
        tracking.setCommentCustomization(TrackingCustomization.None);
        tracking.setRatingCustomization(TrackingCustomization.None);
        tracking.setGeopositionCustomization(TrackingCustomization.None);
        for (int i = 0; i < eventCount; i++) {
            EventV1 e = new EventV1();
            e.setEventId(UUID.randomUUID());
            e.setEventDate(
                    new DateTime(
                            2016 + r.nextInt(1000) / 365,
                            r.nextInt(12) + 1,
                            r.nextInt(27) + 1,
                            0,
                            0).toDate());
            e.setScale(r.nextDouble());
            tracking.addEvent(e);
        }
        return tracking;
    }

    private String data = "0.49256466, 0.99163366, 1.48013768, 1.97070154, " +
            "2.51770509, 3.03434447, 3.53568262, 4.02047089, " +
            "4.51117111, 4.98503408, 5.51866539, 6.20164377, " +
            "6.83871273, 7.33789197, 7.57848685, 7.90042666, " +
            "8.38547668, 9.15034176, 9.42309554, 10.36706581, " +
            "10.42351154, 10.80211628, 11.65307144, 11.73869909, " +
            "12.35058718, 13.36466902, 13.48451327, 13.50657827, " +
            "14.58067925, 15.03642678, 15.97946505, 15.23367533, " +
            "16.33907836, 17.06226843, 17.87064312, 18.58556715, " +
            "18.25229252, 18.45041291, 19.40855308, 19.9528872, " +
            "20.42267646, 20.3053054, 21.53533181, 22.37462728, " +
            "21.2178548, 22.9642124, 23.39038844, 24.44629095, " +
            "24.42213593, 25.41559696, 25.4901409, 26.34676264, " +
            "25.68513436, 27.39552616, 27.16101197, 28.03066173, " +
            "28.75819506, 28.55796622, 29.4653565, 29.44399382, " +
            "31.24984108, 31.29055635, 30.13844981, 31.39104202, " +
            "32.25265886, 32.71635111, 33.84147017, 34.03329709, " +
            "34.14055515, 35.21650592, 34.61159745, 35.82971829, " +
            "35.59688985, 37.4898766, 37.12179981, 37.28825717, " +
            "37.15748696, 39.39515031, 38.90050755, 41.05031985, " +
            "40.93751211, 41.73419084, 42.29763227, 43.29658969, " +
            "43.13773626, 43.13146249, 42.57557652, 44.03209839, " +
            "43.35939215, 43.28779955, 46.60327778, 45.41906321, " +
            "45.3092497, 46.35232927, 49.39731692, 47.34096115, " +
            "49.4059816, 48.4575152, 50.32224391, 50.35740132";
}
