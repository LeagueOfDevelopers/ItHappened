package ru.lod_misis.ithappened.Application;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;


public class TrackingService
{
    public TrackingService(String userId, ITrackingRepository trackingRepository)
    {
        this.userId = userId;
        trackingCollection = trackingRepository;
    }

    public void SaveTrackingCollection(List<Tracking> trackingList)
    {
        trackingCollection.SaveTrackingCollection(trackingList);
    }

    public void AddTracking(Tracking newTracking)
    {
        trackingCollection.AddNewTracking(newTracking);
    }

    public void EditTracking(UUID trackingId,
                             TrackingCustomization editedCounter,
                             TrackingCustomization editedScale,
                             TrackingCustomization editedComment,
                             String editedTrackingName,
                             String scaleName)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.EditTracking(editedCounter, editedScale, editedComment, editedTrackingName, scaleName);
        trackingCollection.ChangeTracking(tracking);
    }

    public void AddEvent(UUID trackingId, Event newEvent)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.AddEvent(newEvent);
        trackingCollection.ChangeTracking(tracking);
    }

    public void EditEvent(UUID trackingId, UUID eventId,
                          Double newScale,
                          Rating newRating,
                          String newComment,
                          Date newDate)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.EditEvent(eventId, newScale, newRating, newComment, newDate);
        trackingCollection.ChangeTracking(tracking);
    }

    public List<Event> FilterEventCollection (List<UUID> trackingId, Date dateFrom, Date dateTo,
                                              Comparison scaleComparison, Double scale,
                                              Comparison ratingComparison, Rating rating,
                                              int fromElement, int count)
    {
        List<Event> events = trackingCollection.FilterEvents(trackingId, dateFrom, dateTo,
                scaleComparison, scale,
                ratingComparison, rating, fromElement, count);
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return t1.GetEventDate().compareTo(event.GetEventDate());
            }
        });
        return events;
    }

    public void RemoveEvent(UUID trackingId, UUID eventId)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.RemoveEvent(eventId);
        trackingCollection.ChangeTracking(tracking);
    }

    public void RemoveTracking(UUID trackingId)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.DeleteTracking();
        trackingCollection.ChangeTracking(tracking);
    }

    public Event GetEvent (UUID trackingId, UUID eventId)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        Event event = tracking.GetEvent(eventId);
        return event;
    }

    public List<Tracking> GetTrackingCollection() {return  trackingCollection.GetTrackingCollection();}

    private ITrackingRepository trackingCollection;
    private String userId;
}
