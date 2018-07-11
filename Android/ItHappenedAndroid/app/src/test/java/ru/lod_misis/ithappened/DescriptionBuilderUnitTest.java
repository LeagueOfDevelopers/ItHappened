package ru.lod_misis.ithappened;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.BinaryCorrelationFact;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.MultinomialCorrelationFact;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.ScaleCorrelationFact;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.LongestBreakFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.ScaleTrendChangingFact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.DayWithLargestEventCount;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.WeekWithLargestEventCountFact;

public class DescriptionBuilderUnitTest {

    private DataSet<Double> TestDoubleData;

    @Test
    public void CorrDescriptionTest_DescriptionBuilderBuildCorrectCorrelationDescription() {
        InitializeDoubleDataset();
        TrackingV1 tracking1 = InitializeDoubleTracking(TestDoubleData.GetColumn(0),
                "килограммы", "я поел мороженного", 0);
        TrackingV1 tracking2 = InitializeDoubleTracking(TestDoubleData.GetColumn(1),
                "килограммы", "я потолстел", 1);

        ScaleCorrelationFact fact1 = new ScaleCorrelationFact(tracking1, tracking2);
        BinaryCorrelationFact fact2 = new BinaryCorrelationFact(tracking1, tracking2);
        MultinomialCorrelationFact fact3 = new MultinomialCorrelationFact(tracking1, tracking2);

        fact1.calculateData();
        fact2.calculateData();
        fact3.calculateData();

        Assert.assertTrue(fact1.textDescription().equals("C маленькой вероятностью при увеличении количества килограммы в событии я поел мороженного происходит увеличение количества килограммы в событии я потолстел."));
        Assert.assertTrue(fact2.textDescription().equals("C большой вероятностью при увеличении числа событий я поел мороженного происходит увеличение числа событий я потолстел."));
        Assert.assertTrue(fact3.textDescription().equals(""));
    }

    @Test
    public void TrendDescriptionTest_DescriptionBuilderBuildsCorrectTrendDescription() {
        TrackingV1 tracking = GenerateScaleRaisingUpTracking();
        ScaleTrendChangingFact fact = new ScaleTrendChangingFact(tracking);
        fact.calculateData();
        String descr = fact.textDescription();
        Assert.assertTrue(descr.contains("среднее значение шкалы 'scale' cобытия 'tracking' увеличилось"));
    }

    @Test
    public void BuildLongestBreakDescriptionTest_DescriptionBuilderBuildsCorrectReport() {
        TrackingV1 tracking = GenerateTrackingWithDateBreak();
        LongestBreakFact fact = new LongestBreakFact(tracking);
        fact.calculateData();
        String descr = fact.textDescription();
        Assert.assertTrue(descr.contains("Длина перерыва в днях: 9"));
    }

    @Test
    public void BuildDayWithLargestEventCountDescriptionTest_BuilderBuildsCorrectReport() {
        TrackingV1 tracking = GenerateTrackingWithDateBreak();
        List<TrackingV1> trackings = new ArrayList<>();
        trackings.add(tracking);
        DayWithLargestEventCount fact = new DayWithLargestEventCount(trackings);
        fact.calculateData();
        String descr = fact.textDescription();
        Assert.assertEquals(descr, "Самый насыщенный событиями день был 5 января 2018 года. Тогда произошло 2 события.");
    }

    @Test
    public void BuildWeekWithLargestEventCountDescription_BuilderBuildsCorrectDescription() {
        TrackingV1 tracking = GenerateTrackingWithDateBreak();
        List<TrackingV1> trackings = new ArrayList<>();
        trackings.add(tracking);
        WeekWithLargestEventCountFact fact = new WeekWithLargestEventCountFact(trackings);
        fact.calculateData();
        String descr = fact.textDescription();
        Assert.assertEquals(descr, "Самая насыщенная событиями неделя была с 1 января 2018 года до 7 января 2018 года. В течении этой недели произошло 7 событий.");
    }

    private TrackingV1 GenerateTrackingWithDateBreak() {
        TrackingV1 tracking = new TrackingV1("tracking",
                UUID.randomUUID(),
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "scale", "");
        String dates = "1 2 3 4 5 5 7 10 11 12 14 16 25";
        List<Integer> dateArr = new ArrayList<>();
        for (String date: dates.split(" ")) {
            dateArr.add(Integer.parseInt(date));
        }
        for (Integer i: dateArr) {
            EventV1 event = new EventV1();
            DateTime date = new DateTime(2018, 1, i, 10, 0);
            event.SetEventDate(date.toDate());
            event.SetEventId(UUID.randomUUID());
            tracking.AddEvent(event);
        }
        return tracking;
    }

    private TrackingV1 GenerateScaleRaisingUpTracking() {
        TrackingV1 tracking = new TrackingV1("tracking",
                UUID.randomUUID(),
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "scale", "");
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            EventV1 event = new EventV1();
            event.SetScale((double)rand.nextInt(10 + i) + i);
            event.SetEventDate(new DateTime(2000, i / 30 + 3, i % 30 + 1, 0, 0).toDate());
            event.SetEventId(UUID.randomUUID());
            tracking.AddEvent(event);
        }
        return tracking;
    }

    private TrackingV1 InitializeDoubleTracking(List<Double> data, String scaleName,
                                              String trackingName, int dayseed) {
        TrackingV1 tracking = new TrackingV1(trackingName,
                UUID.randomUUID(),
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                scaleName, "");
        for (int i = 0; i < data.size(); i++) {
            EventV1 event = new EventV1();
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
