package com.example.ithappenedandroid.Infrastructure;

import com.example.ithappenedandroid.Domain.Tracking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ded on 13.12.2017.
 */

public class TrackingRepository
{
    public TrackingRepository()
    {
        trackingCollection = new ArrayList<Tracking>();
    }

    public Tracking GetTracking(UUID trackingId)
    {
        for (Tracking item: trackingCollection)
        {
            if (item.GetTrackingID() == trackingId)
                return item;
        }
        throw new IllegalArgumentException("Tracking with such ID does not exist");
    }

    public List<Tracking> GetTrackingCollection()
    {
        return trackingCollection;
    }

    public void ChangeTracking(Tracking tracking)
    {
        int index = 0;
        boolean contains = false;
        for (Tracking item: trackingCollection)
        {
            if (item.GetTrackingID() == tracking.GetTrackingID())
            {
                contains = true;
                break;
            }
            index++;
        }
        if (contains)
            trackingCollection.set(index, tracking);
        else new IllegalArgumentException("Tracking with such ID doesn't exists");
    }

    public void AddNewTracking(Tracking tracking)
    {
        int index = 0;
        boolean contains = false;
        for (Tracking item: trackingCollection)
        {
            if (item.GetTrackingID() == tracking.GetTrackingID())
            {
                contains = true;
                break;
            }
            index++;
        }
        if (!contains)
            trackingCollection.add(tracking);
        else new IllegalArgumentException("Tracking with such ID already exists");
    }

    private List<Tracking> trackingCollection;
}
