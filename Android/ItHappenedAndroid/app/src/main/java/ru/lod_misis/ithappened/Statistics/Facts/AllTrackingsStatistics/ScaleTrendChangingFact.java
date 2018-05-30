package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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

public class ScaleTrendChangingFact extends Fact {

    private String TrackingName;
    private String ScaleName;
    private List<Event> Events;
    private TrendDelta TrendDelta;
    private Double NewAverange;

    public ScaleTrendChangingFact(Tracking tracking) {
        ScaleName = tracking.getScaleName();
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

    @Override
    public void calculateData() {
        TrendDelta = CalculateTrendDelta();
    }

    @Override
    protected void calculatePriority() {

    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildScaleTrendReport(TrendDelta, TrackingName, ScaleName);
    }

    private TrendDelta CalculateTrendDelta() {
        Sequence data = DataSetBuilder.BuildScaleSequence(SortEventsByDate(Events));
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
