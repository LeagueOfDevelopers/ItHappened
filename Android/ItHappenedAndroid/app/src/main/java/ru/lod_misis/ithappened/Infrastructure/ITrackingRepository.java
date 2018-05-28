package ru.lod_misis.ithappened.Infrastructure;

import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface ITrackingRepository
{
    Tracking GetTracking(UUID trackingId);
    List<Tracking> GetTrackingCollection();
    void ChangeTracking(Tracking tracking);
    void AddNewTracking(Tracking tracking);
    List<Event> FilterEvents(List<UUID> trackingId, Date from, Date to,
                             Comparison scaleComparison, Double scale,
                             Comparison ratingComparison, Rating rating);
    void SaveTrackingCollection(List<Tracking> trackingCollection);
    void configureRealm();
    void setUserId(String userId);
    Event getEvent(UUID eventId);
    void deleteEventFromRealm(UUID eventId);
    void deleteTrackingFromRealm(UUID trackingId);
    void editEvent(Event event);
}
