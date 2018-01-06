package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Rating;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.InMemoryTrackingRepository;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

        Event newEvent = new Event(eventId, trackingID, null, null, null);

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

        TrackingCustomization newScale = TrackingCustomization.Required;
        TrackingCustomization newRating = TrackingCustomization.Required;
        TrackingCustomization newComment = TrackingCustomization.Required;
        String newTrackingName = "New tracking name";

        service.EditTracking(trackingID, newScale, newRating, newComment, newTrackingName);
        newTracking.EditTracking(newScale, newRating, newComment, newTrackingName);

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
        Double eventScale = 1.0;
        Rating eventRating = new Rating(1);
        String eventComment = "comment";

        Event newEvent = new Event(eventId, trackingID, eventScale, eventRating, eventComment);

        service.AddEvent(trackingID, newEvent);

        Double newEventScale = 2.0;
        Rating newEventRating = new Rating(2);
        String newEventComment = "new comment";
        Date newEventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        service.EditEvent(trackingID, eventId, newEventScale, newEventRating, newEventComment, newEventDate);
        Event editedEvent = new Event (eventId, trackingID, newEventScale, newEventRating, newEventComment);
        editedEvent.EditDate(newEventDate);

        List<Event> eventCollection = service.GetTrackingCollection().get(0).GetEventCollection();

        Assert.assertEquals(eventCollection.get(0), newEvent);
    }

    @Test
    public void RemoveEventFromCollection()
    {
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();
        TrackingService service = new TrackingService("name", inMemoryTrackingRepositoryImpl);

        UUID trackingId = UUID.randomUUID();

        Tracking tracking = new Tracking("name", trackingId,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None);

        UUID firstEventId = UUID.randomUUID();
        UUID secondEventId = UUID.randomUUID();

        Event firstEvent = new Event(firstEventId, trackingId,
                null, null, null);
        Event secondEvent = new Event(secondEventId, trackingId,
                null, null, null);

        tracking.AddEvent(firstEvent);
        tracking.AddEvent(secondEvent);

        service.AddTracking(tracking);

        List<Event> eventCollectionMustBe = new ArrayList<>();
        eventCollectionMustBe.add(firstEvent);

        service.RemoveEvent(trackingId, secondEventId);

        List<Event> eventCollectionInTracking = service.GetTrackingCollection().get(0).GetEventCollection();
        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInTracking.toArray());
    }

    @Test
    public void RemoveDoesNotExistingEventFromCollection_ThrowException()
    {
        boolean thrown = false;
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();
        TrackingService service = new TrackingService("name", inMemoryTrackingRepositoryImpl);

        UUID trackingId = UUID.randomUUID();

        Tracking tracking = new Tracking("name", trackingId,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None);

        UUID firstEventId = UUID.randomUUID();
        UUID IdOFDoesNotExistEvent = UUID.randomUUID();

        Event firstEvent = new Event(firstEventId, trackingId,
                null, null, null);

        tracking.AddEvent(firstEvent);

        service.AddTracking(tracking);

        try {
            service.RemoveEvent(trackingId, IdOFDoesNotExistEvent);
        }
        catch (IllegalArgumentException e)
        {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void RemoveEventFromCollectionOfDoesNotExistingTracking_ThrowException()
    {
        boolean thrown = false;
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();
        TrackingService service = new TrackingService("name", inMemoryTrackingRepositoryImpl);

        UUID trackingId = UUID.randomUUID();

        Tracking tracking = new Tracking("name", trackingId,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None);

        UUID firstEventId = UUID.randomUUID();
        UUID IdOfDoesNotTracking = UUID.randomUUID();

        Event firstEvent = new Event(firstEventId, trackingId,
                null, null, null);

        tracking.AddEvent(firstEvent);

        service.AddTracking(tracking);

        try {
            service.RemoveEvent(IdOfDoesNotTracking, firstEventId);
        }
        catch (IllegalArgumentException e)
        {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }

}


