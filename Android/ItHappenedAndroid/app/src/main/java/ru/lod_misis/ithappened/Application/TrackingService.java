package ru.lod_misis.ithappened.Application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Domain.Rating;
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

    public void SaveTrackingCollection(List<NewTracking> newTrackingList)
    {
        trackingCollection.SaveTrackingCollection(newTrackingList);
    }

    public void AddTracking(NewTracking newNewTracking)
    {
        trackingCollection.AddNewTracking(newNewTracking);
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

    public void AddEvent(UUID trackingId, NewEvent newNewEvent)
    {
        trackingCollection.addEvent(trackingId, newNewEvent);
    }

    public void EditEvent(UUID trackingId, UUID eventId,
                          Double newScale, Rating newRating, String newComment,
                          Date newDate)
    {
        trackingCollection.editEvent(trackingId, eventId, newScale, newRating, newComment, newDate);
    }

    public Observable<NewEvent> FilterEventCollection (List<UUID> trackingId, Date dateFrom, Date dateTo,
                                                       Comparison scaleComparison, Double scale,
                                                       Comparison ratingComparison, Rating rating)
    {
        List<NewEvent> newEvents = trackingCollection.FilterEvents(trackingId, dateFrom, dateTo,
                scaleComparison, scale,
                ratingComparison, rating);

        if(newEvents == null) newEvents = new ArrayList<>();

        return Observable.from(newEvents);
    }

    public List<NewEvent> getEventCollection(UUID trackingId){
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

    public NewEvent GetEvent (UUID eventId)
    {
        return trackingCollection.getEvent(eventId);
    }

    public List<NewTracking> GetTrackingCollection() {return  trackingCollection.GetTrackingCollection();}

    private ITrackingRepository trackingCollection;
    private String userId;
}