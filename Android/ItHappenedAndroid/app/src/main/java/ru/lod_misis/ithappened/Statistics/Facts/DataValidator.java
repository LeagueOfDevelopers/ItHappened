package ru.lod_misis.ithappened.Statistics.Facts;

import org.joda.time.DateTime;

import java.util.List;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.TrackingV1;

public class DataValidator {

    public boolean CheckTrackingForBinaryData(TrackingV1 tracking, int limitEventCount) {
        return tracking != null
                && !tracking.isDeleted()
                && tracking.GetEventHistory() != null
                && CheckEventsForNotDeletedAndDate(tracking.GetEventHistory(), limitEventCount);
    }

    public boolean CheckTrackingForScaleData(TrackingV1 tracking, int limitEventCount) {
        return tracking != null
                && !tracking.isDeleted()
                && tracking.GetEventHistory() != null
                && tracking.GetScaleCustomization() != TrackingCustomization.None
                && CheckScaleEventCollection(tracking.GetEventHistory(), limitEventCount);
    }

    public boolean CheckTrackingForRatingData(TrackingV1 tracking, int limitEventCount) {
        return tracking != null
                && !tracking.isDeleted()
                && tracking.GetEventHistory() != null
                && tracking.GetRatingCustomization() != TrackingCustomization.None
                && CheckRatingEventCollection(tracking.GetEventHistory(), limitEventCount);
    }

    public boolean CheckScaleEventCollection(List<EventV1> events, int eventCountLimit) {
        int count = 0;
        for (EventV1 e: events) {
            if (!e.isDeleted()
                    && e.GetScale() != null
                    && new DateTime(e.getEventDate()).isBefore(DateTime.now())) {
                count++;
            }
        }
        return count >= eventCountLimit;
    }

    public boolean CheckRatingEventCollection(List<EventV1> events, int eventCountLimit) {
        int count = 0;
        for (EventV1 e: events) {
            if (!e.isDeleted()
                    && e.GetRating() != null
                    && new DateTime(e.getEventDate()).isBefore(DateTime.now())) {
                count++;
            }
        }
        return count >= eventCountLimit;
    }

    public boolean CheckEventsForNotDeletedAndDate(List<EventV1> events, int eventCountLimit) {
        int count = 0;
        for (EventV1 e: events) {
            if (!e.isDeleted()
                    && new DateTime(e.getEventDate()).isBefore(DateTime.now())) {
                count++;
            }
        }
        return count >= eventCountLimit;
    }
}
