package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.Sequence;
import ru.lod_misis.ithappened.Statistics.Facts.Models.SequenceWork.SequenceAnalyzer;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingData;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingPoint;

public class FrequencyTrendChangingFact extends Fact {

    private String TrackingName;
    private List<EventV1> Events;
    private TrendChangingPoint PointOfChange;
    private Double NewAverage;
    private int LastPeriodEventCount;
    private Interval LastInterval;

    public FrequencyTrendChangingFact(TrackingV1 tracking){
        trackingId = tracking.GetTrackingID();
        TrackingName = tracking.getTrackingName();
        Events = new ArrayList<>();
        NewAverage = 0.0;
        Events = SelectNotDeletedEventsInThePast(tracking.getEventV1Collection());
    }

    private static List<EventV1> SelectNotDeletedEventsInThePast(List<EventV1> events) {
        List<EventV1> validEvents = new ArrayList<>();
        for (EventV1 e: events) {
            if (e != null
                    && !e.isDeleted()
                    && new DateTime(e.getEventDate()).isBefore(DateTime.now())) {
                validEvents.add(e);
            }
        }
        return validEvents;
    }

    @Override
    public void calculateData() {
        CalculateTrendData();
        calculatePriority();
    }

    @Override
    protected void calculatePriority() {
        Integer days = LastInterval.toDuration().toStandardDays().getDays();
        if (NewAverage - PointOfChange.getAverangeValue() > 0)
            if (days != 0)
                priority = (double)LastPeriodEventCount / days;
            else
                priority = Double.MAX_VALUE;
        else
            if (LastPeriodEventCount != 0)
                priority = (double)days / LastPeriodEventCount;
            else
                priority = Double.MAX_VALUE;
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildFrequencyTrendReport(PointOfChange, NewAverage, TrackingName,
                new Interval(PointOfChange.getPointEventDate().getTime(),
                        DateTime.now().toDate().getTime()), LastPeriodEventCount);
    }

    public boolean IsTrendDeltaSignificant() {
        return NewAverage - PointOfChange.getAverangeValue() != 0 && LastPeriodEventCount != 0;
    }

    private void CalculateTrendData() {
        // Преобразовываем набор событий в набор значений, необходимых нам для анализа, то есть частоты
        Sequence data = DataSetBuilder.BuildFrequencySequence(Events);
        // На основе полученных данных считаем среднее
        NewAverage = data.Mean();
        // Вычисляем "точку перегиба" графика (точку, в которой тренд меняется на противоположный)
        TrendChangingData trendData = SequenceAnalyzer.DetectTrendChangingPoint(data);
        // Считаем количество событий за последний период путем суммирования
        // количества событий произошедших с того времени
        LastPeriodEventCount = (int)data.Slice(trendData.getItemInCollectionId(),
                data.Length()).Sum();
        // Вычисляем интервал последнего события, как разность между датами последнего
        // события из выборки и события, которое является точкой перегиба
        LastInterval = new Interval(Events.get(Events.size() - 1).GetEventDate().getTime(),
                Events.get(trendData.getItemInCollectionId()).GetEventDate().getTime());
        // Собираем все полученные данные в обьект, хранящий данные
        // о изменении тренда и сохраняем как переменную класса
        PointOfChange = new TrendChangingPoint(
                Events.get(trendData.getItemInCollectionId()).GetEventId(),
                data.Slice(0, trendData.getItemInCollectionId()).Mean(),
                trendData.getAlphaCoefficient(),
                Events.get(trendData.getItemInCollectionId()).GetEventDate());
    }
}
