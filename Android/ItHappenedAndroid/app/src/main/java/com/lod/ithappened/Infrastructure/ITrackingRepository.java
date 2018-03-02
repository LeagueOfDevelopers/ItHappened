package com.lod.ithappened.Infrastructure;

import com.lod.ithappened.Domain.Comparison;
import com.lod.ithappened.Domain.Event;
import com.lod.ithappened.Domain.Rating;
import com.lod.ithappened.Domain.Tracking;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface ITrackingRepository
{
    Tracking GetTracking(UUID trackingId);
    List<Tracking> GetTrackingCollection();
    void ChangeTracking(Tracking tracking);
    void AddNewTracking(Tracking tracking);
    List<Event> FilterEvents(List<UUID> trackingId, Date from, Date to,
                                    Comparison scaleComparison, Double scale,
                                    Comparison ratingComparison, Rating rating);
    void SaveTrackingCollection(List<Tracking> trackingCollection);

}
