package com.example.ithappenedandroid.Application;

import com.example.ithappenedandroid.Domain.Comparison;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Rating;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;

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

    public void AddTracking(Tracking newTracking)
    {
        trackingCollection.AddNewTracking(newTracking);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void EditTracking(UUID trackingId,
                             Optional<TrackingCustomization> editedCounter,
                             Optional<TrackingCustomization> editedScale,
                             Optional<TrackingCustomization> editedComment,
                             Optional<String> editedTrackingName)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.EditTracking(editedCounter, editedScale, editedComment, editedTrackingName);
        trackingCollection.ChangeTracking(tracking);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void AddEvent(UUID trackingId, Event newEvent)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.AddEvent(newEvent);
        trackingCollection.ChangeTracking(tracking);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void EditEvent(UUID trackingId, UUID eventId,
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                          Double newScale,
                          Rating newRating,
                          String newComment,
                          Date newDate)
=======
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
                          Optional<Double> newCount,
                          Optional<Scale> newScale,
                          Optional<String> newComment,
                          Optional<TimeZone> newDate)
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.EditEvent(eventId, newScale, newRating, newComment, newDate);
        trackingCollection.ChangeTracking(tracking);
    }

    public List<Event> FilterEventCollection (UUID trackingId, Date from, Date to,
                                              Comparison scaleComparison, Double scale,
                                              Comparison ratingComparison, Rating rating)
    {
        List<Event> events = trackingCollection.FilterEvents(trackingId, from, to,
                scaleComparison, scale,
                ratingComparison, rating);
        return events;
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
