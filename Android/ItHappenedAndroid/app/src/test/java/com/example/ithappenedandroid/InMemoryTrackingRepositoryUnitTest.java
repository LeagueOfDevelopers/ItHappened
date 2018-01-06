package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Domain.Comparison;
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
import java.util.concurrent.TimeUnit;

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
                UUID.randomUUID(), null, null, null);
        Event secondEventOfFirstTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, null, null);
        Event firstEventOfSecondTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, null, null);
        Event secondEventOfSecondTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, null, null);

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

    @Test
    public void FilterEventsWithIdFilterReturnFilteredEventCollection()
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

        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(), firstTrackingId,
                null, null, null);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(), firstTrackingId,
                null, null, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(), secondTrackingId,
                null, null, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(), secondTrackingId,
                null, null, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(firstEventOfFirstTracking);
        eventCollectionMustBe.add(secondEventOfFirstTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(firstTrackingId, null, null,
                null, null,
                null, null);

        Assert.assertArrayEquals(eventCollectionInRepository.toArray(), eventCollectionMustBe.toArray());
    }

    @Test
    public void FilterEventsWithDateFilterReturnFilteredEventCollection() throws InterruptedException {
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

        Date dateFrom = Calendar.getInstance(TimeZone.getDefault()).getTime();
        TimeUnit.SECONDS.sleep(1);
        Event firstEventOfFirstTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, null, null);
        Event firstEventOfSecondTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, null, null);
        TimeUnit.SECONDS.sleep(1);
        Date dateTo = Calendar.getInstance(TimeZone.getDefault()).getTime();
        TimeUnit.SECONDS.sleep(1);
        Event secondEventOfFirstTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, null, null);
        Event secondEventOfSecondTracking = new Event(firstTrackingId,
                UUID.randomUUID(), null, null, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(firstEventOfFirstTracking);
        eventCollectionMustBe.add(firstEventOfSecondTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(null, dateFrom, dateTo,
                null, null,
                null, null);

        Assert.assertArrayEquals(eventCollectionInRepository.toArray(), eventCollectionMustBe.toArray());
    }

    @Test
    public void FilterEventsWithDateAndTrackingFilterReturnFilteredEventCollection() throws InterruptedException {
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

        Date dateFrom = Calendar.getInstance(TimeZone.getDefault()).getTime();
        TimeUnit.SECONDS.sleep(1);
        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId,null, null, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, null, null, null);
        TimeUnit.SECONDS.sleep(1);
        Date dateTo = Calendar.getInstance(TimeZone.getDefault()).getTime();
        TimeUnit.SECONDS.sleep(1);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId,null, null, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, null, null, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(firstEventOfFirstTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(firstTrackingId, dateFrom, dateTo,
                null, null,
                null, null);

        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInRepository.toArray());
    }

    @Test
    public void FilterEventsWithRatingFilterReturnEventsWithValueMoreThenArgument(){
        InMemoryTrackingRepository repository = new InMemoryTrackingRepository();
        UUID firstTrackingId = UUID.randomUUID();
        UUID secondTrackingId = UUID.randomUUID();

        Tracking firstTracking = new Tracking("name",
                firstTrackingId,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None);
        Tracking secondTracking = new Tracking("name",
                secondTrackingId,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None);

        Rating firstRating = new Rating(2);
        Rating secondRating = new Rating(6);
        Rating thirdRating = new Rating(4);
        Rating fourthRating = new Rating(8);
        Rating ratingFilter = new Rating(5);
        Comparison comparison = Comparison.More;

        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId,null, firstRating, null);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId,null, secondRating, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, null, thirdRating, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, null, fourthRating, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(secondEventOfFirstTracking);
        eventCollectionMustBe.add(secondEventOfSecondTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(null,
                null, null,
                null, null,
                comparison, ratingFilter);

        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInRepository.toArray());
    }

    @Test
    public void FilterEventsWithRatingFilterReturnEventsWithValueLessThenArgument(){
        InMemoryTrackingRepository repository = new InMemoryTrackingRepository();
        UUID firstTrackingId = UUID.randomUUID();
        UUID secondTrackingId = UUID.randomUUID();

        Tracking firstTracking = new Tracking("name",
                firstTrackingId,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None);
        Tracking secondTracking = new Tracking("name",
                secondTrackingId,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None);

        Rating firstRating = new Rating(2);
        Rating secondRating = new Rating(6);
        Rating thirdRating = new Rating(4);
        Rating fourthRating = new Rating(8);
        Rating ratingFilter = new Rating(5);
        Comparison comparison = Comparison.Less;

        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId,null, firstRating, null);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId,null, secondRating, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, null, thirdRating, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, null, fourthRating, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(firstEventOfFirstTracking);
        eventCollectionMustBe.add(firstEventOfSecondTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(null,
                null, null,
                null, null,
                comparison, ratingFilter);

        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInRepository.toArray());
    }

    @Test
    public void FilterEventsWithRatingFilterReturnEventsWithValueEqualToArgument(){
        InMemoryTrackingRepository repository = new InMemoryTrackingRepository();
        UUID firstTrackingId = UUID.randomUUID();
        UUID secondTrackingId = UUID.randomUUID();

        Tracking firstTracking = new Tracking("name",
                firstTrackingId,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None);
        Tracking secondTracking = new Tracking("name",
                secondTrackingId,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None);

        Rating firstRating = new Rating(2);
        Rating secondRating = new Rating(6);
        Rating thirdRating = new Rating(4);
        Rating fourthRating = new Rating(8);
        Rating ratingFilter = new Rating(6);
        Comparison comparison = Comparison.Equal;

        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId,null, firstRating, null);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId,null, secondRating, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, null, thirdRating, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, null, fourthRating, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(secondEventOfFirstTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(null,
                null, null,
                null, null,
                comparison, ratingFilter);

        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInRepository.toArray());
    }

    @Test
    public void FilterEventsWithRatingAndTrackingFilterReturnEventsWithValueEqualToArgument(){
        InMemoryTrackingRepository repository = new InMemoryTrackingRepository();
        UUID firstTrackingId = UUID.randomUUID();
        UUID secondTrackingId = UUID.randomUUID();

        Tracking firstTracking = new Tracking("name",
                firstTrackingId,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None);
        Tracking secondTracking = new Tracking("name",
                secondTrackingId,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None);

        Rating firstRating = new Rating(2);
        Rating secondRating = new Rating(6);
        Rating thirdRating = new Rating(4);
        Rating fourthRating = new Rating(6);
        Rating ratingFilter = new Rating(6);
        Comparison comparison = Comparison.Equal;

        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId,null, firstRating, null);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId,null, secondRating, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, null, thirdRating, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, null, fourthRating, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(secondEventOfFirstTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(firstTrackingId,
                null, null,
                null, null,
                comparison, ratingFilter);

        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInRepository.toArray());
    }

    @Test
    public void FilterEventsWithScaleFilterReturnEventsWithValueMoreThenArgument() {
        InMemoryTrackingRepository repository = new InMemoryTrackingRepository();
        UUID firstTrackingId = UUID.randomUUID();
        UUID secondTrackingId = UUID.randomUUID();

        Tracking firstTracking = new Tracking("name",
                firstTrackingId,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None);
        Tracking secondTracking = new Tracking("name",
                secondTrackingId,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None);

        Double firstScale = 2.0;
        Double secondScale = 6.0;
        Double thirdScale = 4.0;
        Double fourthScale = 8.0;
        Double scaleFilter = 5.0;
        Comparison comparison = Comparison.More;

        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId, firstScale, null, null);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId, secondScale, null, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, thirdScale, null, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, fourthScale, null, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(secondEventOfFirstTracking);
        eventCollectionMustBe.add(secondEventOfSecondTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(null,
                null, null,
                comparison, scaleFilter,
                null, null);

        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInRepository.toArray());
    }

    @Test
    public void FilterEventsWithScaleFilterReturnEventsWithValueLessThenArgument() {
        InMemoryTrackingRepository repository = new InMemoryTrackingRepository();
        UUID firstTrackingId = UUID.randomUUID();
        UUID secondTrackingId = UUID.randomUUID();

        Tracking firstTracking = new Tracking("name",
                firstTrackingId,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None);
        Tracking secondTracking = new Tracking("name",
                secondTrackingId,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None);

        Double firstScale = 2.0;
        Double secondScale = 6.0;
        Double thirdScale = 4.0;
        Double fourthScale = 8.0;
        Double scaleFilter = 5.0;
        Comparison comparison = Comparison.Less;

        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId, firstScale, null, null);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId, secondScale, null, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, thirdScale, null, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, fourthScale, null, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(firstEventOfFirstTracking);
        eventCollectionMustBe.add(firstEventOfSecondTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(null,
                null, null,
                comparison, scaleFilter,
                null, null);

        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInRepository.toArray());
    }

    @Test
    public void FilterEventsWithScaleFilterReturnEventsWithValueMoreToArgument() {
        InMemoryTrackingRepository repository = new InMemoryTrackingRepository();
        UUID firstTrackingId = UUID.randomUUID();
        UUID secondTrackingId = UUID.randomUUID();

        Tracking firstTracking = new Tracking("name",
                firstTrackingId,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None);
        Tracking secondTracking = new Tracking("name",
                secondTrackingId,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None);

        Double firstScale = 2.0;
        Double secondScale = 6.0;
        Double thirdScale = 4.0;
        Double fourthScale = 8.0;
        Double scaleFilter = 6.0;
        Comparison comparison = Comparison.Equal;

        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId, firstScale, null, null);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId, secondScale, null, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, thirdScale, null, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, fourthScale, null, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(secondEventOfFirstTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(null,
                null, null,
                comparison, scaleFilter,
                null, null);

        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInRepository.toArray());
    }

    @Test
    public void FilterEventsWithScaleAndTrackingFilter_ReturnEventOfFirstTrackingWithValueEqualToArgument() {
        InMemoryTrackingRepository repository = new InMemoryTrackingRepository();
        UUID firstTrackingId = UUID.randomUUID();
        UUID secondTrackingId = UUID.randomUUID();

        Tracking firstTracking = new Tracking("name",
                firstTrackingId,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None);
        Tracking secondTracking = new Tracking("name",
                secondTrackingId,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None);

        Double firstScale = 2.0;
        Double secondScale = 6.0;
        Double thirdScale = 4.0;
        Double fourthScale = 6.0;
        Double scaleFilter = 6.0;
        Comparison comparison = Comparison.Equal;

        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId, firstScale, null, null);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId, secondScale, null, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, thirdScale, null, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, fourthScale, null, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(secondEventOfFirstTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(firstTrackingId,
                null, null,
                comparison, scaleFilter,
                null, null);

        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInRepository.toArray());
    }

    @Test
    public void AllFilters_ReturnCollectionWithOnlyOneEvent() throws InterruptedException {
        InMemoryTrackingRepository repository = new InMemoryTrackingRepository();
        UUID firstTrackingId = UUID.randomUUID();
        UUID secondTrackingId = UUID.randomUUID();

        Tracking firstTracking = new Tracking("name",
                firstTrackingId,
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.None);
        Tracking secondTracking = new Tracking("name",
                secondTrackingId,
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.None);

        Rating firstRating = new Rating(8);
        Rating secondRating = new Rating(2);
        Rating thirdRating = new Rating(4);
        Rating fourthRating = new Rating(9);
        Rating ratingFilter = new Rating(6);
        Comparison ratingComparison = Comparison.More;

        Double firstScale = 1.0;
        Double secondScale = 2.0;
        Double thirdScale = 4.0;
        Double fourthScale = 6.0;
        Double scaleFilter = 3.0;
        Comparison scaleComparison = Comparison.Less;

        Date dateFrom = Calendar.getInstance(TimeZone.getDefault()).getTime();
        TimeUnit.SECONDS.sleep(1);
        Event firstEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId, firstScale, firstRating, null);
        Event firstEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, thirdScale, thirdRating, null);
        TimeUnit.SECONDS.sleep(1);
        Date dateTo = Calendar.getInstance(TimeZone.getDefault()).getTime();
        TimeUnit.SECONDS.sleep(1);
        Event secondEventOfFirstTracking = new Event(UUID.randomUUID(),
                firstTrackingId, secondScale, secondRating, null);
        Event secondEventOfSecondTracking = new Event(UUID.randomUUID(),
                secondTrackingId, fourthScale, fourthRating, null);

        List<Event> eventCollectionMustBe = new ArrayList<Event>();
        eventCollectionMustBe.add(firstEventOfFirstTracking);

        firstTracking.AddEvent(firstEventOfFirstTracking);
        firstTracking.AddEvent(secondEventOfFirstTracking);
        secondTracking.AddEvent(firstEventOfSecondTracking);
        secondTracking.AddEvent(secondEventOfSecondTracking);

        repository.AddNewTracking(firstTracking);
        repository.AddNewTracking(secondTracking);

        List<Event> eventCollectionInRepository = repository.FilterEvents(firstTrackingId,
                dateFrom, dateTo,
                scaleComparison, scaleFilter,
                ratingComparison, ratingFilter);

        Assert.assertArrayEquals(eventCollectionMustBe.toArray(), eventCollectionInRepository.toArray());
    }
}

