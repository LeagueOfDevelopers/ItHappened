package ru.lod_misis.ithappened.Application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
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

    public void SaveTrackingCollection(List<TrackingV1> trackingV1List)
    {
        trackingCollection.SaveTrackingCollection(trackingV1List);
    }

    public void AddTracking(TrackingV1 newTrackingV1)
    {
        trackingCollection.AddNewTracking(newTrackingV1);
    }

    public void EditTracking(UUID trackingId,
                             TrackingCustomization editedCounter,
                             TrackingCustomization editedScale,
                             TrackingCustomization editedComment,
                             TrackingCustomization editedGeoposition,
                             TrackingCustomization editedPhoto,
                             String editedTrackingName,
                             String scaleName,
                             String color)
    {
        TrackingV1 tracking = trackingCollection.GetTracking(trackingId);
        tracking.EditTracking(editedCounter, editedScale, editedComment,editedGeoposition,
                photo_custmization_with_Mvp_branch, editedTrackingName, scaleName, color);

        trackingCollection.editTracking(tracking);
    }

    public void AddEvent(UUID trackingId, EventV1 newEventV1)
    {
        trackingCollection.addEvent(trackingId, newEventV1);
    }

    public void EditEvent(UUID trackingId, UUID eventId,
                          Double newScale, Rating newRating, String newComment,Double newLotitude,
                          Double newLongitude,String newPhoto,
                          Date newDate)
    {
        EventV1 event = trackingCollection.getEvent(eventId);
        event.EditScale(newScale);
        event.EditComment(newComment);
        event.EditValueOfRating(newRating);
        event.EditGeoposition(newLotitude, newLongitude);
        event.EditPhoto(newPhoto);
        trackingCollection.editEvent(event);
    }

    public Observable<EventV1> FilterEventCollection (List<UUID> trackingId, Date dateFrom, Date dateTo,
                                                      Comparison scaleComparison, Double scale,
                                                      Comparison ratingComparison, Rating rating,
                                                      int indexFrom, int indexTo)
    {
        List<EventV1> eventV1s = trackingCollection.FilterEvents(trackingId, dateFrom, dateTo,
                scaleComparison, scale,
                ratingComparison, rating,
                indexFrom, indexTo);

        if(eventV1s == null) eventV1s = new ArrayList<>();

        return Observable.from(eventV1s);
    }

    public List<EventV1> getEventCollection(UUID trackingId){
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

    public EventV1 GetEvent (UUID eventId)
    {
        return trackingCollection.getEvent(eventId);
    }

    public List<TrackingV1> GetTrackingCollection() {return  trackingCollection.GetTrackingCollection();}

    private ITrackingRepository trackingCollection;
    private String userId;
}