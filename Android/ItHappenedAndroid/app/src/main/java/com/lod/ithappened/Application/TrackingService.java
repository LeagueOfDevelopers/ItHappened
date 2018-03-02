package com.lod.ithappened.Application;

import com.lod.ithappened.Domain.Comparison;
import com.lod.ithappened.Domain.Event;
import com.lod.ithappened.Domain.Rating;
import com.lod.ithappened.Domain.Tracking;
import com.lod.ithappened.Domain.TrackingCustomization;
import com.lod.ithappened.Infrastructure.ITrackingRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;



public class TrackingService
{
    public TrackingService(String userNickname, ITrackingRepository trackingRepository)
    {
        this.userNickname = userNickname;
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
                             String editedTrackingName)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.EditTracking(editedCounter, editedScale, editedComment, editedTrackingName);
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

    public List<Event> FilterEventCollection (List<UUID> trackingId, Date from, Date to,
                                              Comparison scaleComparison, Double scale,
                                              Comparison ratingComparison, Rating rating)
    {
        List<Event> events = trackingCollection.FilterEvents(trackingId, from, to,
                scaleComparison, scale,
                ratingComparison, rating);
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
    private String userNickname;
}
