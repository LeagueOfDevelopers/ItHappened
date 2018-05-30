package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.CorrelationFact;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.ScaleTrendChangingFact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;

public class DescriptionBuilderUnitTest {

    private DataSet<Double> TestDoubleData;

    @Test
    public void DescriptionBuilderCorrDescriptionTest_DescriptionBuilderBuildCorrectCorrelationDescription() {
        InitializeDoubleDataset();
        Tracking tracking1 = InitializeDoubleTracking(TestDoubleData.GetColumn(0),
                "килограммы", "я поел мороженного", 0);
        Tracking tracking2 = InitializeDoubleTracking(TestDoubleData.GetColumn(1),
                "килограммы", "я потолстел", 1);
        CorrelationFact fact = new CorrelationFact(tracking1, tracking2);
        fact.calculateData();

        String description = fact.textDescription().trim();
        Assert.assertTrue(description.equals("Между значениями килограммы событий я поел мороженного и значениями килограммы событий я потолстел выявлена слабая положительная взаимосвязь. Между фактами возникновения события я поел мороженного и фактами возникновения события я потолстел выявлена сильная положительная взаимосвязь."));
    }

    @Test
    public void DescriptionBuilderTrendDescriptionTest_DescriptionBuilderBuildsCorrectTrendDescription() {
        Tracking tracking = GenerateScaleRaisingUpTracking();
        ScaleTrendChangingFact fact = new ScaleTrendChangingFact(tracking);
        fact.calculateData();
        String descr = fact.textDescription();
        Assert.assertTrue(descr.contains("В значениях шкалы scale отслеживания tracking выявлен положительный тренд."));
    }

    private Tracking GenerateScaleRaisingUpTracking() {
        Tracking tracking = new Tracking("tracking",
                UUID.randomUUID(),
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "scale");
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            Event event = new Event();
            event.SetScale((double)rand.nextInt(10 + i) + i);
            event.SetEventDate(new Date(2000, i / 30 + 1, i % 30));
            event.SetEventId(UUID.randomUUID());
            tracking.AddEvent(event);
        }
        return tracking;
    }

    private Tracking InitializeDoubleTracking(List<Double> data, String scaleName,
                                              String trackingName, int dayseed) {
        Tracking tracking = new Tracking(trackingName,
                UUID.randomUUID(),
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                scaleName);
        for (int i = 0; i < data.size(); i++) {
            Event event = new Event();
            event.SetScale(data.get(i));
            event.setEventDate(new Date(2000, i / 30 + 1, i % 30 + dayseed));
            tracking.AddEvent(event);
        }
        return tracking;
    }

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
}
