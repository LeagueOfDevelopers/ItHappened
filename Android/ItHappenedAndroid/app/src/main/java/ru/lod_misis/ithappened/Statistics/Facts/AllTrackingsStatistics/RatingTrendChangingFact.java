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
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendDelta;

public class RatingTrendChangingFact extends Fact {

    private String TrackingName;
    private List<Event> Events;
    private TrendDelta TrendDelta;
    private Double NewAverange;

    public RatingTrendChangingFact(Tracking tracking) {
        TrackingName = tracking.GetTrackingName();
        trackingId = tracking.GetTrackingID();
        Events = new ArrayList<>();
        NewAverange = 0.0;
        double sum = 0.0;
        for (Event e: tracking.GetEventCollection()) {
            if (!e.isDeleted() && e.GetRating() != null) {
                Events.add(e);
                sum += e.GetRating().getRating();
            }
        }
        NewAverange = sum / Events.size();
    }

    @Override
    public void calculateData() {
        TrendDelta = CalculateTrendDelta();
        illustartion = new IllustartionModel(IllustrationType.GRAPH);
        illustartion.setGraphData(DataSetBuilder.BuildRatingSequence(Events).ToList());
        calculatePriority();
    }

    @Override
    protected void calculatePriority() {
        Integer days = new Interval(TrendDelta.getPoint().getPointEventDate().getTime(),
                DateTime.now().toDate().getTime())
                .toDuration() // Преобразование в класс длительности
                .toStandardDays() // Достаем дни
                .getDays();
        if (days != 0)
            priority = Math.abs(NewAverange - TrendDelta.getPoint().getAverangeValue()) * 10 / days;
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
        return DescriptionBuilder.BuildRatingTrendReport(TrendDelta, TrackingName);
    }

    public boolean IsTrendDeltaSignificant() {
        return TrendDelta.getAverangeDelta() != 0;
    }

    private TrendDelta CalculateTrendDelta() {
        Sequence data = DataSetBuilder.BuildRatingSequence(SortEventsByDate(Events));
        TrendChangingData trendData = SequenceAnalyzer.DetectTrendChangingPoint(data);
        TrendChangingPoint point = new TrendChangingPoint(
                Events.get(trendData.getEventInCollectionId()).GetEventId(),
                data.Slice(0, trendData.getEventInCollectionId()).Mean(),
                trendData.getAlphaCoefficient(),
                Events.get(trendData.getEventInCollectionId()).GetEventDate());
        return new TrendDelta(point, NewAverange);
    }

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
