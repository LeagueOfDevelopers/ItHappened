package ru.lod_misis.ithappened.Data.Repositories;

import ru.lod_misis.ithappened.Domain.Models.Comparison;
import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Models.Rating;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface ITrackingRepository
{
    TrackingV1 GetTracking(UUID trackingId);
    List<TrackingV1> GetTrackingCollection();
    void ChangeTracking(TrackingV1 trackingV1);
    void AddNewTracking(TrackingV1 trackingV1);
    List<EventV1> FilterEvents(List<UUID> trackingId, Date from, Date to,
                               Comparison scaleComparison, Double scale,
                               Comparison ratingComparison, Rating rating,
                               int indexFrom, int indexTo);
    void SaveTrackingCollection(List<TrackingV1> trackingV1Collection);
    void configureRealm();
    void setUserId(String userId);
    EventV1 getEvent(UUID eventId);
    void deleteEvent(UUID eventId);
    void deleteTracking(UUID trackingId);
    void editEvent(EventV1 event);
    void editTracking(TrackingV1 tracking);
    void addEvent(UUID trackingId, EventV1 newEventV1);
    List<EventV1> getEventCollection(UUID trackingId);
}
