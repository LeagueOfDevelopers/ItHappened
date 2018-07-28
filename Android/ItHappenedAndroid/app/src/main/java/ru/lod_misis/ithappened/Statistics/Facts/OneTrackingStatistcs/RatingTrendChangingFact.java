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

public class RatingTrendChangingFact extends Fact {

    private String TrackingName;
    private List<EventV1> Events;
    private TrendChangingPoint PointOfChange;
    private Double NewAverage;

    public RatingTrendChangingFact(TrackingV1 tracking) {
        TrackingName = tracking.getTrackingName();
        trackingId = tracking.GetTrackingID();
        Events = new ArrayList<>();
        NewAverage = 0.0;
        Events = SelectNotDeletedEventsWithRatingInThePast(tracking.getEventV1Collection());
        for (EventV1 e: Events) {
            NewAverage += e.GetRating().getRating();
        }
        Events = SortEventsByDate(Events);
        NewAverage = NewAverage / Events.size();
    }

    private static List<EventV1> SelectNotDeletedEventsWithRatingInThePast(List<EventV1> events) {
        List<EventV1> validEvents = new ArrayList<>();
        for (EventV1 e: events) {
            if (e != null
                    && !e.isDeleted()
                    && new DateTime(e.getEventDate()).isBefore(DateTime.now())
                    && e.getRating() != null) {
                validEvents.add(e);
            }
        }
        return validEvents;
    }
    // Данный метод выбирает из коллекции эвентов только те, которые
    // соответствуют предикату, а то есть не удаленные, произошедшие
    // в прошедшем времени и имеющие рейтинг

    @Override
    public void calculateData() {
        CalculateTrendDelta();
        illustartion = new IllustartionModel(IllustrationType.GRAPH);
        illustartion.setGraphData(DataSetBuilder.BuildRatingSequence(Events).ToList());
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
        return DescriptionBuilder.BuildRatingTrendReport(PointOfChange, NewAverage, TrackingName);
    }

    public boolean IsTrendDeltaSignificant() {
        return NewAverage - PointOfChange.getAverageValue() != 0;
    }

    private void CalculateTrendDelta() {
        // Выбираем из эвентов только значения рейтинга и составляем последовательность
        Sequence values = DataSetBuilder.BuildRatingSequence(Events);
        // Вычисляем точку изменения тренда на последовательности
        int trendData = SequenceAnalyzer.DetectTrendChangingPoint(values);
        // Так как каждая точка соответствует одному событию, можно сказать,
        // что getItemInFrequencySequence возвращает нам номер эвента в коллекции эвентов
        Double mean;
        if (trendData == 0) {
            mean = 0.;
        }
        else {
            mean = values.Slice(0, trendData).Mean();
        }
        PointOfChange = new TrendChangingPoint(mean, Events.get(trendData).GetEventDate());
    }

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
