package ru.lod_misis.ithappened.domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.domain.models.Comparison;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.models.Rating;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import rx.Observable;


public class TrackingService {
    public TrackingService(TrackingDataSource trackingRepository) {
        trackingCollection = trackingRepository;
    }

    public void AddTracking(TrackingV1 newTrackingV1) {
        trackingCollection.createTracking(newTrackingV1);
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
        TrackingV1 tracking = trackingCollection.getTracking(trackingId);
        tracking.editTracking(editedCounter, editedScale, editedComment,editedGeoposition,
                editedPhoto, editedTrackingName, scaleName, color);

        trackingCollection.updateTracking(tracking);
    }

    public void AddEvent(UUID trackingId, EventV1 newEventV1) {
        trackingCollection.createEvent(trackingId, newEventV1);
    }

    public void EditEvent(UUID eventId,
                          Double newScale, Rating newRating, String newComment, Double newLotitude,
                          Double newLongitude, String newPhoto,
                          Date newDate)
    {
        EventV1 event = trackingCollection.getEvent(eventId);
        event.editScale(newScale);
        event.editComment(newComment);
        event.editValueOfRating(newRating);
        event.editGeoposition(newLotitude, newLongitude);
        event.editPhoto(newPhoto);
        event.editDate(newDate);
        trackingCollection.updateEvent(event);
    }

    public Observable<EventV1> FilterEventCollection(List<UUID> trackingId, Date dateFrom, Date dateTo,
                                                     Comparison scaleComparison, Double scale,
                                                     Comparison ratingComparison, Rating rating,
                                                     int indexFrom, int indexTo) {
        Log.d("CheckMbBagHere","check");
        List<EventV1> eventV1s = trackingCollection.filterEvents(trackingId, dateFrom, dateTo,
                scaleComparison, scale,
                ratingComparison, rating,
                indexFrom, indexTo);

        if (eventV1s == null) eventV1s = new ArrayList<>();

        return Observable.from(eventV1s);
    }

    public List<EventV1> getEventCollection(UUID trackingId) {
        return trackingCollection.getEventCollection(trackingId);
    }

    public void RemoveEvent(UUID eventId) {
        trackingCollection.deleteEvent(eventId);
    }

    public void RemoveTracking(UUID trackingId) {
        trackingCollection.deleteTracking(trackingId);
    }

    public TrackingV1 GetTrackingById(UUID trackingId) {
        return trackingCollection.getTracking(trackingId);
    }

    public List<TrackingV1> GetTrackingCollection() {
        return trackingCollection.getTrackingCollection();
    }

    private TrackingDataSource trackingCollection;
}