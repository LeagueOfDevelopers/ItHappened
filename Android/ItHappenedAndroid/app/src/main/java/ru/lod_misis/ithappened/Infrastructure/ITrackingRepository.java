package ru.lod_misis.ithappened.Infrastructure;

import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface ITrackingRepository
{
    NewTracking GetTracking(UUID trackingId);
    List<NewTracking> GetTrackingCollection();
    void ChangeTracking(NewTracking newTracking);
    void AddNewTracking(NewTracking newTracking);
    List<NewEvent> FilterEvents(List<UUID> trackingId, Date from, Date to,
                                Comparison scaleComparison, Double scale,
                                Comparison ratingComparison, Rating rating);
    void SaveTrackingCollection(List<NewTracking> newTrackingCollection);
    void configureRealm();
    void setUserId(String userId);
    NewEvent getEvent(UUID eventId);
    void deleteEvent(UUID eventId);
    void deleteTracking(UUID trackingId);
    void editEvent(UUID trackingId, UUID eventId,
                   Double newScale,
                   Rating newRating,
                   String newComment,
                   Date newDate);
    void editTracking(UUID trackingId,
                      TrackingCustomization editedCounter,
                      TrackingCustomization editedScale,
                      TrackingCustomization editedComment,
                      String editedTrackingName,
                      String scaleName,
                      String color);
    void addEvent(UUID trackingId, NewEvent newNewEvent);
    List<NewEvent> getEventCollection(UUID trackingId);
}
