package ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.models.builders.DataSetBuilder;
import ru.lod_misis.ithappened.domain.statistics.facts.models.builders.DescriptionBuilder;
import ru.lod_misis.ithappened.domain.statistics.facts.models.collections.Sequence;
import ru.lod_misis.ithappened.domain.statistics.facts.models.sequencework.SequenceAnalyzer;
import ru.lod_misis.ithappened.domain.statistics.facts.models.trends.TrendChangingPoint;

public class ScaleTrendChangingFact extends Fact {

    private String TrackingName;
    private String ScaleName;
    private List<EventV1> Events;
    private TrendChangingPoint PointOfChange;
    private Double NewAverage;

    public ScaleTrendChangingFact(TrackingV1 tracking) {
        ScaleName = tracking.getScaleName();
        trackingId = tracking.getTrackingId();
        TrackingName = tracking.getTrackingName();
        Events = new ArrayList<>();
        NewAverage = 0.0;
        Events = SelectNotDeletedEventsWithScaleInThePast(tracking.getEventCollection());
        for (EventV1 e: Events) {
            NewAverage += e.getScale();
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
        if (PointOfChange == null) return;
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
        return PointOfChange != null && PointOfChange.getAverageValue() - NewAverage != 0;
    }

    private void CalculateTrendDelta() {
        // Вытаскиевам из эвентов значения scale и составляем из них последовательность
        Sequence values = DataSetBuilder.BuildScaleSequence(Events);
        // Вычисляем точку изменения тренда в последовательности скаляров
        int trendData = SequenceAnalyzer.DetectTrendChangingPoint(values);
        // Так как каждая точка соответствует одному событию, можно сказать,
        // что getItemInFrequencySequence возвращает нам номер эвента в коллекции эвентов
        if (trendData == -1) return;
        Double mean;
        if (trendData == 0) {
            mean = 0.;
        }
        else {
            mean = values.Slice(0, trendData).Mean();
        }
        PointOfChange = new TrendChangingPoint(mean, Events.get(trendData).getEventDate());
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
                return event.getEventDate().compareTo(t1.getEventDate());
            }
        });
        return copy;
    }
}
