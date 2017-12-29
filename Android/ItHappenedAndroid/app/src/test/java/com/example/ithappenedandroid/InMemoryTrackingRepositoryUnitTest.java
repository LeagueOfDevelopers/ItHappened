package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Rating;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;
import com.example.ithappenedandroid.Infrastructure.InMemoryTrackingRepository;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ded on 14.12.2017.
 */

public class InMemoryTrackingRepositoryUnitTest
{
    @Test
    public void AddNewTrackingToTrackingRepositoryAndGetBackThisTracking_ThereIsNoException()
    {
        TrackingCustomization count = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();

        Tracking tracking = new Tracking(trackingName, trackingID, count, scale, comment);

        inMemoryTrackingRepositoryImpl.AddNewTracking(tracking);

        Tracking receivedBackTracking = inMemoryTrackingRepositoryImpl.GetTracking(trackingID);

        Assert.assertEquals(tracking, receivedBackTracking);
    }

    @Test
    public void AddNewTrackingToTrackingRepositoryAndGetBackTrackingWithAnotherID_ThrowException()
    {
        boolean thrown = false;
        TrackingCustomization count = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();

        Tracking tracking = new Tracking(trackingName, trackingID, count, scale, comment);

        inMemoryTrackingRepositoryImpl.AddNewTracking(tracking);

        UUID newId = UUID.randomUUID();
        try
        {
            Tracking receivedBackTracking = inMemoryTrackingRepositoryImpl.GetTracking(newId);
        }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void UseMethodGetTrackingCollection_MustReturnTrackingCollection()
    {
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();
        List<Tracking> collection = new ArrayList<>();
        List<Tracking> returnedTrackingCollection;

        returnedTrackingCollection = inMemoryTrackingRepositoryImpl.GetTrackingCollection();

        Assert.assertArrayEquals(collection.toArray(), returnedTrackingCollection.toArray());
    }

    @Test
    public void AddExistingTrackingToTrackingRepository_ThrowException()
    {
        boolean thrown = false;
        TrackingCustomization count = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();

        Tracking newTracking = new Tracking(trackingName, trackingID, count, scale, comment);

        inMemoryTrackingRepositoryImpl.AddNewTracking(newTracking);

        try { inMemoryTrackingRepositoryImpl.AddNewTracking(newTracking);}
        catch ( IllegalArgumentException e)
        {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void ChangeExistingTracking_ReturnChangedTracking()
    {
        TrackingCustomization count = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();

        Tracking tracking = new Tracking(trackingName, trackingID, count, scale, comment);
        Tracking returnedChangedTracking;

        inMemoryTrackingRepositoryImpl.AddNewTracking(tracking);

        tracking.AddEvent(new Event(UUID.randomUUID(), UUID.randomUUID(), null, null, null));
        inMemoryTrackingRepositoryImpl.ChangeTracking(tracking);
        returnedChangedTracking = inMemoryTrackingRepositoryImpl.GetTracking(trackingID);

        Assert.assertEquals(tracking, returnedChangedTracking);
    }

    @Test
    public void ChangeExistingTrackingWithAnotherID_ThrowException()
    {
        boolean thrown = false;
        TrackingCustomization count = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingID = UUID.randomUUID();
        String trackingName = "Tracking name";
        InMemoryTrackingRepository inMemoryTrackingRepositoryImpl = new InMemoryTrackingRepository();

        Tracking tracking = new Tracking(trackingName, trackingID, count, scale, comment);
        Tracking newTracking = new Tracking(trackingName, UUID.randomUUID(), count, scale, comment);

        inMemoryTrackingRepositoryImpl.AddNewTracking(tracking);

        tracking.AddEvent(new Event(UUID.randomUUID(), UUID.randomUUID(), null, null, null));
        try {
            inMemoryTrackingRepositoryImpl.ChangeTracking(newTracking);
        }
        catch (IllegalArgumentException e) { thrown = true; }
        Assert.assertTrue(thrown);
    }

    @Test
    public void FilterEventsWithOutFiltersMustReturnAllEvents()
    {
        InMemoryTrackingRepository repository = new InMemoryTrackingRepository();
        UUID firstTrackingId = UUID.randomUUID();
        UUID secondTrackingId = UUID.randomUUID();

        Tracking firstTracking = new Tracking("name",
                firstTrackingId,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None);
        Tracking secondTracking = new Tracking("name",
                secondTrackingId,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None);

        Event firstEventOfFirstTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, new Rating(null), null);
        Event secondEventOfFirstTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, new Rating(null), null);
        Event firstEventOfSecondTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, new Rating(null), null);
        Event secondEventOfSecondTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, new Rating(null), null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(firstEventOfFirstTracking);
        eventCollectionMustBe.add(secondEventOfFirstTracking);
        eventCollectionMustBe.add(firstEventOfSecondTracking);
        eventCollectionMustBe.add(secondEventOfSecondTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(null, null, null,
                null, null,
                null, null);

        Assert.assertArrayEquals(eventCollectionInRepository.toArray(), eventCollectionMustBe.toArray());
    }
}
