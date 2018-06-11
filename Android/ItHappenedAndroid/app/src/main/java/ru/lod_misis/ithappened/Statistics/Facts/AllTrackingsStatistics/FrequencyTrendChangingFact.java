package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
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

public class FrequencyTrendChangingFact extends Fact {

    private String TrackingName;
    private List<Event> Events;
    private TrendDelta TrendDelta;
    private Double NewAverange;
    private int LastPeriodEventCount;
    private Interval LastInterval;

    public FrequencyTrendChangingFact(Tracking tracking){
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

    @Override
    public void calculateData() {
        CalculateTrendData();
    }

    @Override
    protected void calculatePriority() {
        if (TrendDelta.getAverangeDelta() > 0)
            priority = (double)LastPeriodEventCount / LastInterval.toDuration().toStandardDays().getDays();
        else
            priority = (double)LastInterval.toDuration().toStandardDays().getDays() / LastPeriodEventCount;
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildFrequencyTrendReport(TrendDelta, TrackingName,
                new Interval(DateTime.now().toDate().getTime()
                        - TrendDelta.getPoint().getPointEventDate().getTime()), LastPeriodEventCount);
    }

    public boolean IsTrendDeltaSignificant() {
        return TrendDelta.getAverangeDelta() != 0;
    }

    private void CalculateTrendData() {
        Sequence data = DataSetBuilder.BuildFrequencySequence(Events);
        TrendChangingData trendData = SequenceAnalyzer.DetectTrendChangingPoint(data);

        LastPeriodEventCount = (int)(double)data.Slice(trendData.getEventInCollectionId(),
                data.Length() - 1).Sum();
        LastInterval = new Interval(Events.get(Events.size() - 1).GetEventDate().getTime() -
                Events.get(trendData.getEventInCollectionId()).GetEventDate().getTime());

        TrendChangingPoint point = new TrendChangingPoint(
                Events.get(trendData.getEventInCollectionId()).GetEventId(),
                data.Slice(0, trendData.getEventInCollectionId()).Mean(),
                trendData.getAlphaCoefficient(),
                Events.get(trendData.getEventInCollectionId()).GetEventDate());
        TrendDelta = new TrendDelta(point, NewAverange);
    }
}
