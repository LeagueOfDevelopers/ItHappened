package com.example.ithappenedandroid.Infrastructure;

import com.example.ithappenedandroid.Domain.Comparison;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Rating;
import com.example.ithappenedandroid.Domain.Tracking;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface ITrackingRepository
{
    Tracking GetTracking(UUID trackingId);
    List<Tracking> GetTrackingCollection();
    void ChangeTracking(Tracking tracking);
    void AddNewTracking(Tracking tracking);
    List<Event> FilterEvents(UUID trackingId, Date from, Date to,
                                    Comparison scaleComparison, Double scale,
                                    Comparison ratingComparison, Rating rating);

}
