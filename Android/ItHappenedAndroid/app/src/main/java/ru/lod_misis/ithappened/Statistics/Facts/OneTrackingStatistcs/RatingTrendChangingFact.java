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
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingData;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingPoint;

public class RatingTrendChangingFact extends Fact {

    private String TrackingName;
    private List<EventV1> Events;
    private TrendChangingPoint PointOfChange;
    private Double NewAverange;

    public RatingTrendChangingFact(TrackingV1 tracking) {
        TrackingName = tracking.GetTrackingName();
        trackingId = tracking.GetTrackingID();
        Events = new ArrayList<>();
        NewAverange = 0.0;
        double sum = 0.0;
        for (EventV1 e: tracking.GetEventCollection()) {
            if (!e.isDeleted() && e.GetRating() != null) {
                Events.add(e);
                sum += e.GetRating().getRating();
            }
        }
        NewAverange = sum / Events.size();
    }

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
                DateTime.now().toDate().getTime())
                .toDuration() // Преобразование в класс длительности
                .toStandardDays() // Достаем дни
                .getDays();
        if (days != 0)
            priority = Math.abs(NewAverange - PointOfChange.getAverangeValue()) * 10 / days;
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
        return DescriptionBuilder.BuildRatingTrendReport(PointOfChange, NewAverange, TrackingName);
    }

    public boolean IsTrendDeltaSignificant() {
        return NewAverange - PointOfChange.getAverangeValue() != 0;
    }

    private void CalculateTrendDelta() {
        Sequence data = DataSetBuilder.BuildRatingSequence(SortEventsByDate(Events));
        TrendChangingData trendData = SequenceAnalyzer.DetectTrendChangingPoint(data);
        PointOfChange = new TrendChangingPoint(
                Events.get(trendData.getItemInCollectionId()).GetEventId(),
                data.Slice(0, trendData.getItemInCollectionId()).Mean(),
                trendData.getAlphaCoefficient(),
                Events.get(trendData.getItemInCollectionId()).GetEventDate());
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
