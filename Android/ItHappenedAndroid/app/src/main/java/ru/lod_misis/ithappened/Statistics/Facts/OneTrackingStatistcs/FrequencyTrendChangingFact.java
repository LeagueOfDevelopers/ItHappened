package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Tracking;
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

public class FrequencyTrendChangingFact extends Fact {

    private String TrackingName;
    private List<EventV1> Events;
    private TrendChangingPoint PointOfChange;
    private Double NewAverange;
    private int LastPeriodEventCount;
    private Interval LastInterval;

    public FrequencyTrendChangingFact(TrackingV1 tracking){
        trackingId = tracking.GetTrackingID();
        TrackingName = tracking.GetTrackingName();
        Events = new ArrayList<>();
        NewAverange = 0.0;
        for (EventV1 e: tracking.GetEventCollection()) {
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
        illustartion = new IllustartionModel(IllustrationType.GRAPH);
        illustartion.setGraphData(DataSetBuilder.BuildFrequencySequence(Events).ToList());
        calculatePriority();
    }

    @Override
    protected void calculatePriority() {
        Integer days = LastInterval.toDuration().toStandardDays().getDays();
        if (NewAverange - PointOfChange.getAverangeValue() > 0)
            if (days != 0)
                priority = (double)LastPeriodEventCount / days;
            else
                priority = Double.MAX_VALUE;
        else
            if (LastPeriodEventCount != 0)
                priority = (double)days / LastPeriodEventCount;
            else
                priority = Double.MAX_VALUE;
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildFrequencyTrendReport(PointOfChange, NewAverange, TrackingName,
                new Interval(PointOfChange.getPointEventDate().getTime(),
                        DateTime.now().toDate().getTime()), LastPeriodEventCount);
    }

    public boolean IsTrendDeltaSignificant() {
        return NewAverange - PointOfChange.getAverangeValue() != 0;
    }

    private void CalculateTrendData() {
        Sequence data = DataSetBuilder.BuildFrequencySequence(Events);
        TrendChangingData trendData = SequenceAnalyzer.DetectTrendChangingPoint(data);

        LastPeriodEventCount = (int)(double)data.Slice(trendData.getItemInCollectionId(),
                data.Length() - 1).Sum();
        LastInterval = new Interval(Events.get(Events.size() - 1).GetEventDate().getTime(),
                Events.get(trendData.getItemInCollectionId()).GetEventDate().getTime());

        PointOfChange = new TrendChangingPoint(
                Events.get(trendData.getItemInCollectionId()).GetEventId(),
                data.Slice(0, trendData.getItemInCollectionId()).Mean(),
                trendData.getAlphaCoefficient(),
                Events.get(trendData.getItemInCollectionId()).GetEventDate());
    }
}
