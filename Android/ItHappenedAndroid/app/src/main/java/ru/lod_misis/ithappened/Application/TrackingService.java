package ru.lod_misis.ithappened.Application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import rx.Observable;


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
                             String scaleName,
                             String color)
    {
        trackingCollection.editTracking(trackingId,
                editedCounter, editedScale, editedComment,
                editedTrackingName, scaleName, color);
    }

    public void AddEvent(UUID trackingId, Event newEvent)
    {
        trackingCollection.addEvent(trackingId, newEvent);
    }

    public void EditEvent(UUID trackingId, UUID eventId,
                          Double newScale, Rating newRating, String newComment,
                          Date newDate)
    {
        trackingCollection.editEvent(trackingId, eventId, newScale, newRating, newComment, newDate);
    }

    public Observable<Event> FilterEventCollection (List<UUID> trackingId, Date dateFrom, Date dateTo,
                                                    Comparison scaleComparison, Double scale,
                                                    Comparison ratingComparison, Rating rating)
    {
        List<Event> events = trackingCollection.FilterEvents(trackingId, dateFrom, dateTo,
                scaleComparison, scale,
                ratingComparison, rating);

        if(events == null) events = new ArrayList<>();

        return Observable.from(events);
    }

    public List<Event> getEventCollection(UUID trackingId){
        return trackingCollection.getEventCollection(trackingId);
    }

    public void RemoveEvent(UUID eventId)
    {
        trackingCollection.deleteEvent(eventId);
    }

    public void RemoveTracking(UUID trackingId)
    {
        trackingCollection.deleteTracking(trackingId);
    }

    public Event GetEvent (UUID eventId)
    {
        return trackingCollection.getEvent(eventId);
    }

    public List<Tracking> GetTrackingCollection() {return  trackingCollection.GetTrackingCollection();}

    private ITrackingRepository trackingCollection;
    private String userId;
}