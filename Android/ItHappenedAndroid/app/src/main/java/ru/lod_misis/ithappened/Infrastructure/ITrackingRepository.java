package ru.lod_misis.ithappened.Infrastructure;

import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;

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
    void editEvent(UUID trackingId, UUID eventId,
                   Double newScale,
                   Rating newRating,
                   String newComment,
                   Double newLotitude,
                   Double newLongitude,
                   Date newDate);
    void editTracking(UUID trackingId,
                      TrackingCustomization editedCounter,
                      TrackingCustomization editedScale,
                      TrackingCustomization editedComment,
                      TrackingCustomization editedGeoposition,
                      String editedTrackingName,
                      String scaleName,
                      String color);
    void addEvent(UUID trackingId, EventV1 newEventV1);
    List<EventV1> getEventCollection(UUID trackingId);
}
