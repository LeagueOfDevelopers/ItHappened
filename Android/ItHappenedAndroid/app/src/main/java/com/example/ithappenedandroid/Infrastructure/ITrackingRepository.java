package com.example.ithappenedandroid.Infrastructure;

import com.example.ithappenedandroid.Domain.Tracking;

import java.util.List;
import java.util.UUID;


public interface ITrackingRepository
{
    Tracking GetTracking(UUID trackingId);
    List<Tracking> GetTrackingCollection();
    void ChangeTracking(Tracking tracking);
    void AddNewTracking(Tracking tracking);

}
