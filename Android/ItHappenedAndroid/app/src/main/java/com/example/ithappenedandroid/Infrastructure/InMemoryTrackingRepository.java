package com.example.ithappenedandroid.Infrastructure;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.ithappenedandroid.Domain.Tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class InMemoryTrackingRepository implements ITrackingRepository
{
    public InMemoryTrackingRepository()
    {
        trackingCollection = new ArrayList<Tracking>();
    }

    public Tracking GetTracking(UUID trackingId)
    {
        boolean contains = false;
        for (Tracking item: trackingCollection)
        {
            if (item.GetTrackingID().equals(trackingId))
            {
                return item;
            }
        }
        throw new IllegalArgumentException("Tracking with such ID doesn't exists");
    }

    public List<Tracking> GetTrackingCollection()
    {
        return trackingCollection;
    }

    public void ChangeTracking(final Tracking tracking)
    {
        int index = 0;
        boolean contains = false;
        for (Tracking item: trackingCollection)
        {
            if (item.GetTrackingID().equals(tracking.GetTrackingID()))
            {
                contains = true;
                break;
            }
            index++;
        }
        if (contains)
            trackingCollection.set(index, tracking);
        else throw new IllegalArgumentException("Tracking with such ID doesn't exists");
    }

    public void AddNewTracking(Tracking tracking)
    {
        boolean contains = false;
        for (Tracking item: trackingCollection)
        {
            if (item.GetTrackingID().equals(tracking.GetTrackingID()))
            {
                contains = true;
                break;
            }
        }
        if (!contains)
            trackingCollection.add(tracking);
        else throw new IllegalArgumentException("Tracking with such ID already exists");
    }

    private List<Tracking> trackingCollection;
}
