package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.Sequence;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;
import ru.lod_misis.ithappened.Statistics.Facts.Models.SequenceWork.SequenceAnalyzer;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingData;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingPoint;

public class ScaleTrendChangingFact extends Fact {

    private String TrackingName;
    private String ScaleName;
    private List<Event> Events;
    private TrendChangingPoint PointOfChange;
    private Double NewAverange;

    public ScaleTrendChangingFact(Tracking tracking) {
        ScaleName = tracking.getScaleName();
        trackingId = tracking.GetTrackingID();
        TrackingName = tracking.GetTrackingName();
        Events = new ArrayList<>();
        NewAverange = 0.0;
        for (Event e: tracking.GetEventCollection()) {
            if (!e.isDeleted() && e.GetScale() != null) {
                Events.add(e);
                NewAverange += e.GetScale();
            }
        }
        NewAverange = NewAverange / Events.size();
    }
    // Если событие не удалено и содержит значение шкалы, добавляем его

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
                DateTime.now().toDate().getTime())
                .toDuration() // Преобразование в класс длительности
                .toStandardDays() // Достаем дни
                .getDays();
        if (days != 0)
            priority = Math.abs(NewAverange - PointOfChange.getAverangeValue()) * 10 / days;
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
        return DescriptionBuilder.BuildScaleTrendReport(PointOfChange, NewAverange, TrackingName, ScaleName);
    }

    public boolean IsTrendDeltaSignificant() {
        return PointOfChange.getAverangeValue() - NewAverange != 0;
    }

    private void CalculateTrendDelta() {
        Sequence data = DataSetBuilder.BuildScaleSequence(SortEventsByDate(Events));
        TrendChangingData trendData = SequenceAnalyzer.DetectTrendChangingPoint(data);
        PointOfChange = new TrendChangingPoint(
                Events.get(trendData.getItemInCollectionId()).GetEventId(),
                data.Slice(0, trendData.getItemInCollectionId()).Mean(),
                trendData.getAlphaCoefficient(),
                Events.get(trendData.getItemInCollectionId()).GetEventDate());
    }
    // Сначала преобразовываем массив эвентов в массив скаляров. Затем вычисляем
    // точку изменения тренда. И наконец создаем обьект класса TrendChangingPoint,
    // представляющего из себя набор всех необходимых данных для описания точки
    // изменения тренда, который и возвращаем

    private List<Event> SortEventsByDate(List<Event> events) {
        List<Event> copy = new ArrayList<>(events);
        Collections.sort(copy, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return event.GetEventDate().compareTo(t1.GetEventDate());
            }
        });
        return copy;
    }
}
