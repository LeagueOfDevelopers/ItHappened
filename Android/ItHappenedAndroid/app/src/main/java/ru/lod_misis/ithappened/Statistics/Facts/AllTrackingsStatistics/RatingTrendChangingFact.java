package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

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
    }

    @Override
    protected void calculatePriority() {

    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildRatingTrendReport(TrendDelta, TrackingName);
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
