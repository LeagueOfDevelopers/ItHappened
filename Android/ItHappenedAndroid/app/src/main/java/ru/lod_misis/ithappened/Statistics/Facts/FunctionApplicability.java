package ru.lod_misis.ithappened.Statistics.Facts;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.AllEventsCountFact;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.MostFrequentEventFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.AvrgRatingFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.AvrgScaleFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.TrackingEventsCountFact;

public final class FunctionApplicability  {
    public FunctionApplicability(){}

    public static Fact mostFrequentEventApplicability(List<Tracking> trackingCollection)
    {
        if (trackingCollection.size() < 2) return null;
        List<Tracking> processingTracking = new ArrayList<>();
        for (Tracking tracking: trackingCollection) {
            int eventCount = 0;
            for (Event event: tracking.getEventCollection()) {
                if (!event.isDeleted()) eventCount++;
            }
            if (eventCount > 2) processingTracking.add(tracking);
        }
        if (processingTracking.size() < 2) return null;
        return new MostFrequentEventFact(processingTracking);
    }

    public static Fact allEventsCountFactApplicability(List<Tracking> trackingCollection)
    {
        int count=0;
        for (Tracking tracking: trackingCollection) {
            for (Event event: tracking.getEventCollection()) {
                if (!event.isDeleted()) count++;
            }
        }
        if (count <= 5) return null;
        return new AllEventsCountFact(trackingCollection);
    }

    public static Fact trackingEventsCountApplicability(Tracking tracking)
    {
        int eventCount = 0;
        for (Event event: tracking.getEventCollection()) {
            if (!event.isDeleted()) eventCount++;
        }
        if (eventCount == 0) return null;
        return new TrackingEventsCountFact(tracking);
    }

    public static Fact avrgRatingApplicability(Tracking tracking)
    {
        if (tracking.GetRatingCustomization() == TrackingCustomization.None) return null;

        int eventsWithRating = 0;
        for (Event event: tracking.getEventCollection()) {
            if (event.getRating() != null) eventsWithRating++;
        }
        if (eventsWithRating <= 1) return null;

        return new AvrgRatingFact(tracking);
    }

    public static Fact avrgScaleApplicability(Tracking tracking)
    {
        if (tracking.GetScaleCustomization() == TrackingCustomization.None) return null;

        int eventsWithScale = 0;
        for (Event event: tracking.getEventCollection()) {
            if (event.getScale() != null) eventsWithScale++;
        }
        if (eventsWithScale <= 1) return null;

        return new AvrgScaleFact(tracking);
    }

    public static Fact sumScaleApplicability(Tracking tracking)
    {
        if (tracking.GetScaleCustomization() == TrackingCustomization.None) return null;

        int eventsWithScale = 0;
        for (Event event: tracking.getEventCollection()) {
            if (event.getScale() != null) eventsWithScale++;
        }
        if (eventsWithScale <= 1) return null;

        return new AvrgScaleFact(tracking);
    }
}
