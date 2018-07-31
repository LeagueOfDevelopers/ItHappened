package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.Sequence;
import ru.lod_misis.ithappened.Statistics.Facts.Models.SequenceWork.SequenceAnalyzer;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.EventsTimeDistribution;
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
        SortEventCollectionByTime(Events);
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
        if (PointOfChange == null || LastInterval == null) return;
        calculatePriority();
    }

    @Override
    protected void calculatePriority() {
        // Вычисляем количество дней в периоде
        Integer days = LastInterval.toDuration().toStandardDays().getDays();
        if (NewAverage - PointOfChange.getAverageValue() > 0)
            // Если среднее возросло
            if (days != 0)
                // Если количество дней больше 0
                priority = (double)LastPeriodEventCount / days;
            else
                // Если нет, то подставляем некоторую симуляцию
                // бесконечности (в противном случае получаем деление на 0)
                priority = Double.MAX_VALUE;
        else
            // Если среднее уменьшилось
            if (LastPeriodEventCount != 0)
                // Если в течении периода уменьшения происходили события
                priority = (double)days / LastPeriodEventCount;
            else
                // Если нет, то снова подставляем бесконечность
                priority = Double.MAX_VALUE;
    }

    private static void SortEventCollectionByTime(List<EventV1> events) {
        Collections.sort(events, new Comparator<EventV1>() {
            @Override
            public int compare(EventV1 event, EventV1 t1) {
                return event.getEventDate().compareTo(t1.getEventDate());
            }
        });
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildFrequencyTrendReport(PointOfChange, NewAverage, TrackingName,
                LastInterval, LastPeriodEventCount);
    }

    public boolean IsTrendDeltaSignificant() {
        return PointOfChange != null && LastInterval != null
                && NewAverage - PointOfChange.getAverageValue() != 0
                && LastPeriodEventCount != 0;
    }

    private void CalculateTrendData() {
        // Преобразовываем набор событий в набор значений, необходимых нам для анализа, то есть частоты
        EventsTimeDistribution distr =  DataSetBuilder.BuildFrequencySequence(Events);
        Sequence data = new Sequence(distr.toCountsArray());

        // На основе полученных данных считаем среднее
        NewAverage = data.Mean();

        // Вычисляем "точку перегиба" графика (точку, в которой тренд меняется на противоположный)
        // В данном случае точкой перегиба является целый период, но
        // так как нужно откуда то взять точное значение даты, то это будет
        // либо дата первого эвента в периоде, либо дата начала периода
        int periodIndex = SequenceAnalyzer.DetectTrendChangingPoint(data);
        if (periodIndex == -1) return;

        // Считаем количество событий за последний период путем суммирования
        // количества событий произошедших с того времени
        LastPeriodEventCount = (int)data.Slice(periodIndex, data.Length()).Sum();

        // Вычисляем интервал первого события
        // Если в сегменте, отмеченном как переломный, событий нет,
        // то дата начала последнего периода - это дата начала сегмента
        DateTime leftIntervalBorder;
        if (distr.isThisIntervalEmpty(periodIndex)) {
            leftIntervalBorder = distr.getIntervalById(periodIndex).getStart();
        }
        // В противном случае - это дата первого события в сегменте
        else {
            leftIntervalBorder = new DateTime(distr.getFirstEventFromInterval(
                    periodIndex).getEventDate());
        }

        // Дата конца последнего периода - это дата последнего события
        DateTime rightIntervalBorder = new DateTime(Events.get(Events.size() - 1).GetEventDate().getTime());

        // Если новый эвент в коллекции и есть точка перелома,
        // значит надо вернуть заглушки и обработать их позднее
        if (leftIntervalBorder.isEqual(rightIntervalBorder.toDate().getTime())) {
            PointOfChange = null;
            LastInterval = null;
            return;
        }

        // Если индекс начала изменения тренда равен 0, то среднее в начале отслеживания = 0
        Double mean;
        if (periodIndex == 0) {
            mean = 0.;
        }
        else {
            // Среднее значение за период от начала и до ключевого эвента (даты точки перелома)
            mean = data.Slice(0, periodIndex).Mean();
        }

        // Собираем все полученные данные в обьект, хранящий данные
        // о изменении тренда и сохраняем как переменную класса
        PointOfChange = new TrendChangingPoint(mean, leftIntervalBorder.toDate());
        // Собираем даты в интервал
        LastInterval = new Interval(leftIntervalBorder, rightIntervalBorder);
    }
}
