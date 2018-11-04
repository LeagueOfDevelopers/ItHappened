package ru.lod_misis.ithappened;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.realm.RealmList;
import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Models.Rating;
import ru.lod_misis.ithappened.Domain.Models.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.AllTrackingsStatistics.Correlation.BinaryCorrelationFact;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.AllTrackingsStatistics.Correlation.MultinomialCorrelationFact;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.AllTrackingsStatistics.Correlation.ScaleCorrelationFact;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Collections.DataSet;

public class CorrelationFactUnitTest {

    private DataSet<Double> TestDoubleData;
    private DataSet<Integer> TestBooleanData;
    private DataSet<Integer> TestMultinomialData;
    private TrackingV1 trackingV1_1;
    private TrackingV1 trackingV1_2;

    @Test
    public void CalculateCorrelationTest_GotCorrectCorrelationValue() {
        InitializeDoubleDataset();
        InitializeBooleanDataset();
        InitializeMultinomialDataset();

        BinaryCorrelationFact binaryCorrelationFact = new BinaryCorrelationFact(TestBooleanData);
        ScaleCorrelationFact scaleCorrelationFact = new ScaleCorrelationFact(TestDoubleData);
        MultinomialCorrelationFact multinomialCorrelationFact = new MultinomialCorrelationFact(TestMultinomialData);

        double perfectDoubleCorr = 0.752962;
        double perfectBinaryCorr = -0.3333333;
        double perfectMultCorr = 0.006732;
        double acceptedDeltaValue = 0.003; //Допустимое значение отклонения расчитанной корреляции от эталонной

        binaryCorrelationFact.calculateData();
        scaleCorrelationFact.calculateData();
        multinomialCorrelationFact.calculateData();

        Assert.assertTrue(scaleCorrelationFact.IsDoubleCorrSignificant());
        Assert.assertTrue(binaryCorrelationFact.IsBoolCorrSignificant());
        Assert.assertFalse(multinomialCorrelationFact.IsMultinomialCorrSignificant());

        Assert.assertTrue(perfectBinaryCorr - acceptedDeltaValue <
                binaryCorrelationFact.getCorrelation() &&
                binaryCorrelationFact.getCorrelation() < perfectBinaryCorr + acceptedDeltaValue);

        Assert.assertTrue(perfectDoubleCorr - acceptedDeltaValue <
                scaleCorrelationFact.getCorrelation() &&
                scaleCorrelationFact.getCorrelation() < perfectDoubleCorr + acceptedDeltaValue);

        Assert.assertTrue(perfectMultCorr - acceptedDeltaValue <
                multinomialCorrelationFact.getCorrelation() &&
                multinomialCorrelationFact.getCorrelation() < perfectMultCorr + acceptedDeltaValue);
    }

    //Stress test
    /*@Test
    public void BinaryCorrelationFactStressTest_BinaryCorrelationFactDoesNotThrowsExceptions() {
        int bound = 10000;
        Random r = new Random();
        int i = 1;
        while (true) {
            trackingV1_1 = InitializeRandomFrequencyTracking(r, bound);
            trackingV1_2 = InitializeRandomFrequencyTracking(r, bound);
            List<TrackingV1> trackings = new ArrayList<>();
            trackings.add(trackingV1_1);
            trackings.add(trackingV1_2);
            DateTime time = DateTime.now();
            List<Fact> fact = FunctionApplicability.BinaryCorrelationFactApplicability(trackings);
            System.out.print("Test " + i + " passed\n");
            System.out.print("Binary correlation\n");
            System.out.print("Data set 1 size: " + trackingV1_1.getEventCollection().size() + "\n");
            System.out.print("Data set 2 size: " + trackingV1_2.getEventCollection().size() + "\n");
            System.out.print("Computed in " + new Interval(time, DateTime.now()).toDuration().getMillis() + "\n");
            if (fact.size() > 0) {
                System.out.print(fact.get(0).textDescription() + "\n");
            }
            else {
                System.out.print("Fact is null\n");
            }
            i += 1;
        }
    }

    //Stress test
    @Test
    public void ScaleCorrelationFactStressTest_ScaleCorrelationFactDoesNotThrowsExceptions() {
        int bound = 10000;
        Random r = new Random();
        int i = 1;
        while (true) {
            trackingV1_1 = InitializeRandomScaleTracking(r, bound);
            trackingV1_2 = InitializeRandomScaleTracking(r, bound);
            List<TrackingV1> trackings = new ArrayList<>();
            trackings.add(trackingV1_1);
            trackings.add(trackingV1_2);
            DateTime time = DateTime.now();
            List<Fact> fact = FunctionApplicability.ScaleCorrelationFactApplicability(trackings);
            System.out.print("Test " + i + " passed\n");
            System.out.print("Scale correlation\n");
            System.out.print("Data set 1 size: " + trackingV1_1.getEventCollection().size() + "\n");
            System.out.print("Data set 2 size: " + trackingV1_2.getEventCollection().size() + "\n");
            System.out.print("Computed in " + new Interval(time, DateTime.now()).toDuration().getMillis() + "\n");
            if (fact.size() > 0) {
                System.out.print(fact.get(0).textDescription() + "\n");
            }
            else {
                System.out.print("Fact is null\n");
            }
            i += 1;
        }
    }

    //Stress test
    @Test
    public void RatingCorrelationFactStressTest_RatingCorrelationFactDoesNotThrowsExceptions() {
        int bound = 10000;
        Random r = new Random();
        int i = 1;
        while (true) {
            trackingV1_1 = InitializeRandomRatingTracking(r, bound);
            trackingV1_2 = InitializeRandomRatingTracking(r, bound);
            List<TrackingV1> trackings = new ArrayList<>();
            trackings.add(trackingV1_1);
            trackings.add(trackingV1_2);
            DateTime time = DateTime.now();
            List<Fact> fact = FunctionApplicability.MultinomialCorrelationApplicability(trackings);
            System.out.print("Test " + i + " passed\n");
            System.out.print("Rating correlation\n");
            System.out.print("Data set 1 size: " + trackingV1_1.getEventCollection().size() + "\n");
            System.out.print("Data set 2 size: " + trackingV1_2.getEventCollection().size() + "\n");
            System.out.print("Computed in " + new Interval(time, DateTime.now()).toDuration().getMillis() + "\n");
            if (fact.size() > 0) {
                System.out.print(fact.get(0).textDescription() + "\n");
            }
            else {
                System.out.print("Fact is null\n");
            }
            i += 1;
        }
    }*/

    private void InitializeDoubleDataset() {
        TestDoubleData = new DataSet<>(false);
        String firstDoubleCol = "20.5, 39.1, 15.0, 5.9, 73.5, 18.5, 17.6, 11.9, 11.5, 83.4, " +
                "18.1, 54.8, 39.8, 30.9, 60.1, 60.7, 19.6, 7.5, 50.7, 44.9, " +
                "21.6, 59.1, 41.6, 15.2, 9.1, 53.1, 22.3, 21.6, 52.4, 64.8, " +
                "8.1, 66.9, 32.7, 36.8, 72.4, 48.8, 23.0, 9.5, 51.7, 15.9, " +
                "27.4, 6.2, 8.3, 13.4, 20.3, 18.3, 43.4, 17.8, 5.5, 59.5, " +
                "28.1, 8.0, 37.5, 50.3, 14.7, 82.0, 6.4, 68.1, 17.4, 9.8, " +
                "67.3, 16.3, 32.2, 90.2, 39.0, 32.8, 69.4, 8.1, 40.6, 7.0, " +
                "13.3, 8.8, 5.5, 15.0, 35.2, 29.3, 69.2, 10.4, 14.3, 10.1, " +
                "49.5, 19.2, 36.1, 29.4, 5.4, 53.1, 35.6, 21.4, 18.5, 6.9, 8.5, " +
                "69.8, 25.2, 12.9";
        List<String> firstCol = Arrays.asList(firstDoubleCol.split(", "));
        String secondDoubleCol = "1.78, 2.44, 2.34, 2.97, 5.6, 3.65, 3.03, 2.29, 2.38, 5.9, " +
                "2.23, 6.8, 3.89, 3.53, 4.73, 6.3, 1.81, 2.4, 3.76, 6.7, " +
                "5.6, 4.7, 4.74, 2.73, 2.67, 3.1, 2.76, 5.89, 5.24, 5.32, " +
                "2.79, 4.4, 4.06, 4.33, 7.08, 3.75, 3.47, 0.97, 2.84, 2.27, " +
                "2.07, 2.82, 2.38, 3.29, 4.98, 2.39, 4.5, 2.25, 3.4, 6.78, " +
                "2.85, 0.88, 5.04, 5.84, 2.74, 6.72, 1.37, 5.59, 1.98, 2.11, " +
                "5.3, 3.66, 3.08, 7.67, 5.5, 3.44, 4.12, 2.62, 3.8, 3.67, 2.74, " +
                "1.4, 1.8, 2.89, 5.8, 3.83, 4.9, 1.24, 2.78, 1.91, 4.15, 3.91, " +
                "3.24, 5.2, 1.89, 5.03, 2.04, 2.19, 2.43, 2.65, 1.78, 5.87, 5.4, 3.34";
        List<String> secondCol = Arrays.asList(secondDoubleCol.split(", "));
        for (int i = 0; i < 94; i++) {
            TestDoubleData.AddRow(Double.parseDouble(firstCol.get(i)),
                    Double.parseDouble(secondCol.get(i)));
        }
    }
    // Для тестов я вытащил один из своих датасетов. Для эталонного значения корреляции
    // я взял таковую, посчитанную в питоне.

    private void InitializeBooleanDataset() {
        TestBooleanData = new DataSet<>(true);
        for (int i = 0; i < 25; i++) {
            TestBooleanData.AddRow(1, 1);
            TestBooleanData.AddRow(1, 1);
            TestBooleanData.AddRow(1, 0);
            TestBooleanData.AddRow(0, 1);
        }
    }
    // Просто датасет из 100 элементов, для которого можно вычислить корреляцию

    private void InitializeMultinomialDataset() {
        TestMultinomialData = new DataSet<>(true);
        String firstMultinomCol = "10, 5, 8, 8, 4, 8, " +
                "7, 10, 8, 4, 2, 1, 1, 1, 4, 1, 4, 8, " +
                "4, 4, 8, 1, 9, 3, 2, 9, 9, 5, 10, 9, " +
                "10, 8, 7, 3, 9, 2, 9, 3, 7, 8, 8, 5, " +
                "10, 7, 3, 4, 4, 9, 7, 6, 6, 5, 10, 7, " +
                "3, 9, 7, 8, 10, 4, 7, 6, 6, 9, 10, 6, " +
                "10, 4, 9, 3, 9, 1, 2, 8, 5, 5, 10, 6, " +
                "2, 3, 9, 5, 8, 8, 2, 5, 6, 4, 2, 9, 10, 1, 1, 3, 3, 9, 9, 9, 4, 1";
        List<String> FirstCol = Arrays.asList(firstMultinomCol.split(", "));
        String secondMultinomCol = "3, 2, 2, 8, 7, 2, " +
                "1, 7, 1, 5, 10, 6, 1, 9, 7, 7, 9, 8, " +
                "9, 6, 4, 5, 7, 2, 5, 6, 7, 4, 5, 8, 7, " +
                "1, 4, 7, 6, 6, 8, 2, 9, 6, 1, 1, 10, " +
                "10, 3, 9, 1, 9, 4, 9, 10, 8, 9, 1, 9, " +
                "9, 4, 1, 6, 9, 9, 8, 7, 1, 9, 10, 8, " +
                "1, 3, 10, 2, 5, 2, 1, 1, 4, 2, 3, 5, " +
                "3, 5, 3, 5, 6, 8, 1, 7, 7, 5, 5, 4, 7, 4, 7, 1, 8, 7, 4, 8, 9";
        List<String> SecondCol = Arrays.asList(secondMultinomCol.split(", "));
        for (int i = 0; i < FirstCol.size(); i++) {
            TestMultinomialData.AddRow(Integer.parseInt(FirstCol.get(i)),
                    Integer.parseInt(SecondCol.get(i)));
        }
    }

    private TrackingV1 InitializeRandomScaleTracking(Random r, int bound) {
        int n = r.nextInt(bound);
        TrackingV1 tracking = new TrackingV1();
        tracking.setEventCollection(new RealmList<EventV1>());
        tracking.setTrackingId(UUID.randomUUID());
        tracking.setScaleCustomization(TrackingCustomization.Optional);
        tracking.setCommentCustomization(TrackingCustomization.None);
        tracking.setRatingCustomization(TrackingCustomization.None);
        for (int i = 0; i < n; i++) {
            EventV1 event = new EventV1();
            event.setEventId(UUID.randomUUID());
            if (r.nextInt() % 10 == 2) {
                event.setScale(null);
            } else {
                event.setScale(r.nextDouble());
            }
            event.setEventDate(DateTime.now().minus(Math.abs(r.nextInt())).toDate());
            tracking.addEvent(event);
        }
        return tracking;
    }

    private TrackingV1 InitializeRandomRatingTracking(Random r, int bound) {
        int n = r.nextInt(bound);
        TrackingV1 tracking = new TrackingV1();
        tracking.setEventCollection(new RealmList<EventV1>());
        tracking.setTrackingId(UUID.randomUUID());
        tracking.setScaleCustomization(TrackingCustomization.None);
        tracking.setCommentCustomization(TrackingCustomization.None);
        tracking.setRatingCustomization(TrackingCustomization.Optional);
        for (int i = 0; i < n; i++) {
            EventV1 event = new EventV1();
            event.setEventId(UUID.randomUUID());
            if (r.nextInt() % 10 == 2) {
                event.setRating(null);
            }
            else {
                event.setRating(new Rating(Math.abs(r.nextInt()) % 10 + 1));
            }
            event.setEventDate(DateTime.now().minus(Math.abs(r.nextInt())).toDate());
            tracking.addEvent(event);
        }
        return tracking;
    }

    private TrackingV1 InitializeRandomFrequencyTracking(Random r, int bound) {
        int n = r.nextInt(bound);
        TrackingV1 tracking = new TrackingV1();
        tracking.setEventCollection(new RealmList<EventV1>());
        tracking.setTrackingId(UUID.randomUUID());
        tracking.setScaleCustomization(TrackingCustomization.None);
        tracking.setCommentCustomization(TrackingCustomization.None);
        tracking.setRatingCustomization(TrackingCustomization.None);
        for (int i = 0; i < n; i++) {
            EventV1 event = new EventV1();
            event.setEventId(UUID.randomUUID());
            event.setEventDate(DateTime.now().minus(Math.abs(r.nextInt())).toDate());
            tracking.addEvent(event);
        }
        return tracking;
    }
}
