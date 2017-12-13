package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Scale;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Ded on 13.12.2017.
 */

public class TrackingServiceUnitTest {
    @Test
    public void AddNewTrackingTotrackingService_NewTrackingInTrcakingCollection() {
        String userNickname = "Name";
        TrackingCustomization count = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";

        Tracking newTracking = new Tracking(trackingName, trackingID, count, scale, comment);

        TrackingService service = new TrackingService(userNickname);
        service.AddTracking(newTracking);

        List<Tracking> trackingCollectionInService = service.GetTrackingCollection();
        List<Tracking> trackingCollectionMustBe = new ArrayList<Tracking>();
        trackingCollectionMustBe.add(newTracking);

        Assert.assertArrayEquals(trackingCollectionInService.toArray(), trackingCollectionMustBe.toArray());
    }

    @Test
    public void AddEventToTrackingInTrackingService_NewEventInEventCollection()
    {
        String userNickname = "Name";
        TrackingCustomization count = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";

        Tracking newTracking = new Tracking(trackingName, trackingID, count, scale, comment);

        TrackingService service = new TrackingService(userNickname);
        service.AddTracking(newTracking);

        UUID eventId = UUID.randomUUID();
        Optional<Double> countInEvent = Optional.empty();
        Optional<Scale> scaleInEvent = Optional.empty();
        Optional<String> commentInEvent = Optional.empty();

        Event newEvent = new Event(eventId, countInEvent, scaleInEvent, commentInEvent);

        service.AddEvent(trackingID, newEvent);

        List<Event> eventCollectionInTracking = service.GetTrackingCollection().get(0).GetEventCollection();
        List<Event> eventCollectionMustBe = new ArrayList<>();
        eventCollectionMustBe.add(newEvent);

        Assert.assertArrayEquals(eventCollectionInTracking.toArray(), eventCollectionMustBe.toArray());
    }
}
