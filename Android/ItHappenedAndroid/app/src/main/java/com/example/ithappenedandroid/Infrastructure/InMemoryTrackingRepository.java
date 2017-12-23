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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Tracking GetTracking(UUID trackingId)
    {
        Optional<Tracking> tracking;
        tracking = trackingCollection.stream()
                .filter((item) -> item.GetTrackingID().equals(trackingId))
                .findFirst();
        if (tracking.isPresent())
            return tracking.get();
        else throw new IllegalArgumentException("Tracking with such ID does not exist");
    }

    public List<Tracking> GetTrackingCollection()
    {
        return trackingCollection;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void AddNewTracking(Tracking tracking)
    {
        if (!trackingCollection.stream()
                .anyMatch((item) -> item.GetTrackingID().equals(tracking.GetTrackingID())))
            trackingCollection.add(tracking);
        else throw new IllegalArgumentException("Tracking with such ID already exists");
    }

    private List<Tracking> trackingCollection;
}
