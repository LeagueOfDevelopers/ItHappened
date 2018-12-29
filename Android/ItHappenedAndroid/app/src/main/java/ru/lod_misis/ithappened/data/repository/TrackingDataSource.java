package ru.lod_misis.ithappened.data.repository;

import ru.lod_misis.ithappened.domain.models.Comparison;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.models.Rating;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface TrackingDataSource
{
    TrackingV1 getTracking(UUID trackingId);
    List<TrackingV1> getTrackingCollection();
    void createTracking(TrackingV1 trackingV1);
    List<EventV1> filterEvents(List<UUID> trackingId, Date from, Date to,
                               Comparison scaleComparison, Double scale,
                               Comparison ratingComparison, Rating rating,
                               int indexFrom, int indexTo);
    void updateTrackingCollection(List<TrackingV1> trackingV1Collection);
    void configureRealm();
    void setUserId(String userId);
    EventV1 getEvent(UUID eventId);
    void deleteEvent(UUID eventId);
    void deleteTracking(UUID trackingId);
    void updateEvent(EventV1 event);
    void updateTracking(TrackingV1 tracking);
    void createEvent(UUID trackingId, EventV1 newEventV1);
    List<EventV1> getEventCollection(UUID trackingId);
}
