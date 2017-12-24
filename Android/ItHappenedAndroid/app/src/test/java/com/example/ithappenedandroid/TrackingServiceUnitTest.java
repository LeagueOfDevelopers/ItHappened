package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Scale;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.InMemoryTrackingRepository;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Ded on 13.12.2017.
 */

public class TrackingServiceUnitTest {
    @Test
    public void AddNewTrackingToTrackingService_NewTrackingInTrcakingCollection() {
        String userNickname = "Name";
        TrackingCustomization count = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();


        Tracking newTracking = new Tracking(trackingName, trackingID, count, scale, comment);

        TrackingService service = new TrackingService(userNickname, inMemoryTrackingRepositoryImpl);
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
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();

        Tracking newTracking = new Tracking(trackingName, trackingID, count, scale, comment);

        TrackingService service = new TrackingService(userNickname, inMemoryTrackingRepositoryImpl);
        service.AddTracking(newTracking);

        UUID eventId = UUID.randomUUID();
        Optional<Double> countInEvent = Optional.empty();
        Optional<Scale> scaleInEvent = Optional.empty();
        Optional<String> commentInEvent = Optional.empty();

        Event newEvent = new Event(eventId, trackingID, countInEvent, scaleInEvent, commentInEvent);

        service.AddEvent(trackingID, newEvent);

        List<Event> eventCollectionInTracking = service.GetTrackingCollection().get(0).GetEventCollection();
        List<Event> eventCollectionMustBe = new ArrayList<>();
        eventCollectionMustBe.add(newEvent);

        Assert.assertArrayEquals(eventCollectionInTracking.toArray(), eventCollectionMustBe.toArray());
    }

    @Test
    public void AddExistingTrackingToTrackingService_ThrowException()
    {
        boolean thrown = false;
        String userNickname = "Name";
        TrackingCustomization count = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();

        Tracking newTracking = new Tracking(trackingName, trackingID, count, scale, comment);

        TrackingService service = new TrackingService(userNickname, inMemoryTrackingRepositoryImpl);
        service.AddTracking(newTracking);

        try { service.AddTracking(newTracking);}
        catch ( IllegalArgumentException e)
        {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void GetTrackingCollectionFromServiceWithoutTracking_ReturnedCollectionDoesNotHaveValues()
    {
        String userNickname = "Name";
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();
        TrackingService service = new TrackingService(userNickname, inMemoryTrackingRepositoryImpl);
        List<Tracking> emptyCollection = new ArrayList<Tracking>();
        List<Tracking> returnedCollection;

        returnedCollection = service.GetTrackingCollection();

        Assert.assertEquals(emptyCollection, returnedCollection);
    }

    @Test
    public void EditTracking_EditedTrackingInCollection() {
        String userNickname = "Name";
        TrackingCustomization count = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();

        Tracking newTracking = new Tracking(trackingName, trackingID, count, scale, comment);

        TrackingService service = new TrackingService(userNickname, inMemoryTrackingRepositoryImpl);
        service.AddTracking(newTracking);

        Optional<TrackingCustomization> newCount = Optional.ofNullable(TrackingCustomization.Required);
        Optional<TrackingCustomization> newScale = Optional.ofNullable(TrackingCustomization.Required);
        Optional<TrackingCustomization> newComment = Optional.ofNullable(TrackingCustomization.Required);
        Optional<String> newTrackingName = Optional.ofNullable("New tracking name");

        service.EditTracking(trackingID, newCount, newScale, newComment, newTrackingName);
        newTracking.EditTracking(newCount, newScale, newComment, newTrackingName);

        List<Tracking> trackingCollectionInService = service.GetTrackingCollection();

        Assert.assertEquals(trackingCollectionInService.get(0), newTracking);
    }

    @Test
    public void EditEvent_EditedEventInCollection() {
        String userNickname = "Name";
        TrackingCustomization count = TrackingCustomization.Required;
        TrackingCustomization scale = TrackingCustomization.Required;
        TrackingCustomization comment = TrackingCustomization.Required;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();

        Tracking newTracking = new Tracking(trackingName, trackingID, count, scale, comment);

        TrackingService service = new TrackingService(userNickname, inMemoryTrackingRepositoryImpl);
        service.AddTracking(newTracking);

        UUID eventId = UUID.randomUUID();
        Optional<Double> eventCount = Optional.ofNullable(1.0);
        Optional<Scale> eventScale = Optional.ofNullable(new Scale(1));
        Optional<String> eventComment = Optional.ofNullable("comment");

        Event newEvent = new Event(eventId, trackingID, eventCount, eventScale, eventComment);

        service.AddEvent(trackingID, newEvent);

        Optional<Double> newEventCount = Optional.ofNullable(2.0);
        Optional<Scale> newEventScale = Optional.ofNullable(new Scale(2));
        Optional<String> newEventComment = Optional.ofNullable("new comment");
        Optional<TimeZone> newEventDate = Optional.ofNullable(TimeZone.getDefault());

        service.EditEvent(trackingID, eventId, newEventCount, newEventScale, newEventComment, newEventDate);
        Event editedEvent = new Event (eventId, trackingID, newEventCount, newEventScale, newEventComment);
        editedEvent.EditDate(newEventDate.get());

        List<Event> eventCollection = service.GetTrackingCollection().get(0).GetEventCollection();

        Assert.assertEquals(eventCollection.get(0), newEvent);
    }
}


