package com.example.ithappenedandroid.Application;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;

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
    public void AddEvent(UUID trackingId, Event newEvent)
    {
        Tracking tracking = trackingCollection.GetTracking(trackingId);
        tracking.AddEvent(newEvent);
        trackingCollection.ChangeTracking(tracking);
    }

    public List<Tracking> GetTrackingCollection() {return  trackingCollection.GetTrackingCollection();}

    private ITrackingRepository trackingCollection;
    private String userNickname;
}
