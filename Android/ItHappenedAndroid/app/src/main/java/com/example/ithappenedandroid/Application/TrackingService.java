package com.example.ithappenedandroid.Application;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Scale;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;

import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
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
                          Double newCount,
                          Scale newScale,
                          String newComment,
                          TimeZone newDate)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.EditEvent(eventId, newCount, newScale, newComment, newDate);
        trackingCollection.ChangeTracking(tracking);
    }

    public List<Tracking> GetTrackingCollection() {return  trackingCollection.GetTrackingCollection();}

    private ITrackingRepository trackingCollection;
    private String userNickname;
}
