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
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;
import ru.lod_misis.ithappened.Statistics.Facts.Models.SequenceWork.SequenceAnalyzer;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingPoint;

public class ScaleTrendChangingFact extends Fact {

    private String TrackingName;
    private String ScaleName;
    private List<EventV1> Events;
    private TrendChangingPoint PointOfChange;
    private Double NewAverage;

    public ScaleTrendChangingFact(TrackingV1 tracking) {
        ScaleName = tracking.getScaleName();
        trackingId = tracking.GetTrackingID();
        TrackingName = tracking.getTrackingName();
        Events = new ArrayList<>();
        NewAverage = 0.0;
        Events = SelectNotDeletedEventsWithScaleInThePast(tracking.getEventV1Collection());
        for (EventV1 e: Events) {
            NewAverage += e.GetScale();
        }
        Events = SortEventsByDate(Events);
        NewAverage = NewAverage / Events.size(); // Размер коллекции проверяется в функции применимости
    }
    // Если событие не удалено и содержит значение шкалы, добавляем его

    private static List<EventV1> SelectNotDeletedEventsWithScaleInThePast(List<EventV1> events) {
        List<EventV1> validEvents = new ArrayList<>();
        for (EventV1 e: events) {
            if (e != null
                    && !e.isDeleted()
                    && new DateTime(e.getEventDate()).isBefore(DateTime.now())
                    && e.getScale() != null) {
                validEvents.add(e);
            }
        }
        return validEvents;
    }

    @Override
    public void calculateData() {
        CalculateTrendDelta();
        illustartion = new IllustartionModel(IllustrationType.GRAPH);
        illustartion.setGraphData(DataSetBuilder.BuildScaleSequence(Events).ToList());
        calculatePriority();
    }

    @Override
    protected void calculatePriority() {
        Integer days = new Interval(PointOfChange.getPointEventDate().getTime(),
                Events.get(Events.size() - 1).getEventDate().getTime())
                .toDuration() // Преобразование в класс длительности
                .toStandardDays() // Достаем дни
                .getDays();
        if (days != 0)
            priority = Math.abs(NewAverage - PointOfChange.getAverageValue()) * 10 / days;
        // Достает целочисленное значение из обьекта Days
        else
            priority = Double.MAX_VALUE;
    }
    // В данном методе мы сначала вычисляем абсолют разницы средних в данный момент и
    // в точке изменения тренда, а затем делим все это на протяженность периода в днях.
    // Чтобы получить ее используется класс интервал из библиотеки joda time, в конструктор
    // которого передается протяженность интервала в миллисекундах. Затем он преобразуется
    // в класс длительности, из которого потом достаются дни.

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildScaleTrendReport(PointOfChange, NewAverage, TrackingName, ScaleName);
    }

    public boolean IsTrendDeltaSignificant() {
        return PointOfChange.getAverageValue() - NewAverage != 0;
    }

    private void CalculateTrendDelta() {
        // Вытаскиевам из эвентов значения scale и составляем из них последовательность
        Sequence values = DataSetBuilder.BuildScaleSequence(Events);
        // Вычисляем точку изменения тренда в последовательности скаляров
        int trendData = SequenceAnalyzer.DetectTrendChangingPoint(values);
        // Так как каждая точка соответствует одному событию, можно сказать,
        // что getItemInFrequencySequence возвращает нам номер эвента в коллекции эвентов
        PointOfChange = new TrendChangingPoint(
                values.Slice(0, trendData).Mean(),
                Events.get(trendData).GetEventDate());
    }
    // Сначала преобразовываем массив эвентов в массив скаляров. Затем вычисляем
    // точку изменения тренда. И наконец создаем обьект класса TrendChangingPoint,
    // представляющего из себя набор всех необходимых данных для описания точки
    // изменения тренда, который и возвращаем

    private List<EventV1> SortEventsByDate(List<EventV1> events) {
        List<EventV1> copy = new ArrayList<>(events);
        Collections.sort(copy, new Comparator<EventV1>() {
            @Override
            public int compare(EventV1 event, EventV1 t1) {
                return event.GetEventDate().compareTo(t1.GetEventDate());
            }
        });
        return copy;
    }
}
