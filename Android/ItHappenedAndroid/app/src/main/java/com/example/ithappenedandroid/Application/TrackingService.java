package com.example.ithappenedandroid.Application;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import ch.lambdaj.Lambda;
import ch.lambdaj.collection.LambdaList;


/**
 * Created by Ded on 12.12.2017.
 */

public class TrackingService
{
    public TrackingService(String userNickname)
    {
        this.userNickname = userNickname;
        trackingCollection = new ArrayList<Tracking>();
    }

    public void AddTracking(Tracking newTracking)
    {
        for (Tracking item: trackingCollection)
        {
            if (item.GetTrackingID() == newTracking.GetTrackingID() )
                throw new IllegalArgumentException("Tracking with such ID does not exist");
        }
        trackingCollection.add(newTracking);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void AddEvent(UUID trackingId, Event newEvent)
    {
        int index = 0;
        boolean contains = false;
        for (Iterator<Tracking> i = trackingCollection.iterator(); i.hasNext();)
        {
            Tracking item = i.next();
            if (item.GetTrackingID() == trackingId)
            {
                contains = true;
                break;
            }
            index++;
        }
        if (contains)
            trackingCollection.get(index).AddEvent(newEvent);
        else throw new IllegalArgumentException("Event with such ID does not exist");
    }

    public List<Tracking> GetTrackingCollection() {return  trackingCollection;}

    private List<Tracking> trackingCollection;
    private String userNickname;
}
