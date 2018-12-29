package ru.lod_misis.ithappened.domain.statistics.facts;

import org.joda.time.DateTime;

import java.util.List;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.models.TrackingV1;

public class DataValidator {

    public boolean CheckTrackingForBinaryData(TrackingV1 tracking, int limitEventCount) {
        return tracking != null
                && !tracking.isDeleted()
                && tracking.getEventHistory() != null
                && CheckEventsForNotDeletedAndDate(tracking.getEventHistory(), limitEventCount);
    }

    public boolean CheckTrackingForScaleData(TrackingV1 tracking, int limitEventCount) {
        return tracking != null
                && !tracking.isDeleted()
                && tracking.getEventHistory() != null
                && tracking.getScaleCustomization() != TrackingCustomization.None
                && CheckScaleEventCollection(tracking.getEventHistory(), limitEventCount);
    }

    public boolean CheckTrackingForRatingData(TrackingV1 tracking, int limitEventCount) {
        return tracking != null
                && !tracking.isDeleted()
                && tracking.getEventHistory() != null
                && tracking.getRatingCustomization() != TrackingCustomization.None
                && CheckRatingEventCollection(tracking.getEventHistory(), limitEventCount);
    }

    public boolean CheckScaleEventCollection(List<EventV1> events, int eventCountLimit) {
        int count = 0;
        for (EventV1 e: events) {
            if (!e.isDeleted()
                    && e.getScale() != null
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
                    && e.getRating() != null
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
