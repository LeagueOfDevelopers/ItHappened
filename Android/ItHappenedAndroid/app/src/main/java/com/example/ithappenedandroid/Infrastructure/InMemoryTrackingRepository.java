package com.example.ithappenedandroid.Infrastructure;

import com.example.ithappenedandroid.Domain.Comparison;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Rating;
import com.example.ithappenedandroid.Domain.Tracking;
import com.google.common.collect.Iterables;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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


    public List<Event> FilterEvents(UUID trackingId, Date dateFrom, Date dateTo,
                                    Comparison scaleComparison, Double scale,
                                    Comparison ratingComparison, Rating rating)
    {
        List<Event> events = new ArrayList<Event>();
        for (Tracking trackig : trackingCollection) {
            events.addAll(trackig.GetEventCollection());
        }

        Iterable<Event> iterable = events;

        if (!NullCheck(trackingId))
        {
            iterable = Iterables.filter(iterable, (event) -> event.GetTrackingId().equals(trackingId));
        }

        if (!NullCheck(dateFrom) && !NullCheck(dateTo))
        {
            iterable = Iterables.
                    filter(iterable, (event) -> event.GetEventDate().equals(dateTo) && event.GetEventDate().equals(dateFrom));
        }

        if (!NullCheck(scaleComparison) && !NullCheck(scale))
        {
            if (scaleComparison == Comparison.Less)
            {
                iterable = Iterables.
                        filter(iterable, (event) -> event.GetScale() < scale);
            }
            if (scaleComparison == Comparison.Equal)
            {
                iterable = Iterables.
                        filter(iterable, (event) -> event.GetScale().equals(scale));
            }
            if (scaleComparison == Comparison.More)
            {
                iterable = Iterables.
                        filter(iterable, (event) -> event.GetScale() > scale);
            }
        }

        if (!NullCheck(ratingComparison) && !NullCheck(rating))
        {
            if (ratingComparison == Comparison.Less)
            {
                iterable = Iterables.
                        filter(iterable, (event) -> event.GetRating().GetRatingValue() < rating.GetRatingValue());
            }
            if (ratingComparison == Comparison.Equal)
            {
                iterable = Iterables.
                        filter(iterable, (event) -> event.GetRating().GetRatingValue().equals(rating.GetRatingValue()));
            }
            if (ratingComparison == Comparison.More)
            {
                iterable = Iterables.
                        filter(iterable, (event) -> event.GetRating().GetRatingValue() > rating.GetRatingValue());
            }
        }
        events = Arrays.asList(Iterables.toArray(iterable, Event.class));
        return  events;
    }

    private boolean NullCheck(Object obj)
    {
        if (obj == null)
            return true;
        return false;
    }


    private List<Tracking> trackingCollection;
}
