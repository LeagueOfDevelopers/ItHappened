package ru.lod_misis.ithappened;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;

public class NewTrackingUnitTest {

    Date evDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

    @Test
    public void InTrackingWithRequiredCounterAddEventWithCounter_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization ratingCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), scaleCustomization, ratingCustomization, commentCustomization, "", "");
        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate,1.1, null , null);
        newTracking.AddEvent(newNewEvent);

        List<NewEvent> newEventCollectionInTracking = newTracking.GetEventCollection();

        Assert.assertTrue(newEventCollectionInTracking.contains(newNewEvent));
    }

    @Test
    public void InTrackingWithOptionalCounterAddEventWithCounter_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Optional;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, 1.1, null, null);
        newTracking.AddEvent(newNewEvent);

        List<NewEvent> newEventCollectionInTracking = newTracking.GetEventCollection();

        Assert.assertTrue(newEventCollectionInTracking.contains(newNewEvent));
    }

    @Test
    public void InTrackingWithRequiredCounterAddEventWithoutCounter_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Required;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            newTracking.AddEvent(newNewEvent);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void InTrackingWithOptionalCounterAddEventWithoutCounter_ThereIsNoException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Optional;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            newTracking.AddEvent(newNewEvent);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        Assert.assertFalse(thrown);
    }

    @Test
    public void InTrackingWithoutCounterAddEventWithCounter_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            newTracking.AddEvent(newNewEvent);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        Assert.assertTrue(true);
    }

    @Test
    public void InTrackingWithRequiredScaleAddEventWithScale_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, rating, null);
        newTracking.AddEvent(newNewEvent);

        List<NewEvent> newEventCollectionInTracking = newTracking.GetEventCollection();

        Assert.assertTrue(newEventCollectionInTracking.contains(newNewEvent));
    }

    @Test
    public void InTrackingWithOptionalScaleAddEventWithScale_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Optional;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, rating, null);
        newTracking.AddEvent(newNewEvent);

        List<NewEvent> newEventCollectionInTracking = newTracking.GetEventCollection();

        Assert.assertTrue(newEventCollectionInTracking.contains(newNewEvent));
    }

    @Test
    public void InTrackingWithRequiredScaleAddEventWithoutScale_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            newTracking.AddEvent(newNewEvent);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void InTrackingWithOptionalScaleAddEventWithoutScale_ThereIsNoException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Optional;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            newTracking.AddEvent(newNewEvent);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        Assert.assertFalse(thrown);
    }

    @Test
    public void InTrackingWithoutScaleAddEventWithScale_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, rating, null);

        try {
            newTracking.AddEvent(newNewEvent);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        Assert.assertTrue(true);
    }

    @Test
    public void InTrackingWithRequiredCommentAddEventWithComment_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Required;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");

        String comment = "comment";

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, comment);
        newTracking.AddEvent(newNewEvent);

        List<NewEvent> newEventCollectionInTracking = newTracking.GetEventCollection();

        Assert.assertTrue(newEventCollectionInTracking.contains(newNewEvent));
    }

    @Test
    public void InTrackingWithOptionalCommentAddEventWithComment_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Optional;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");
        String comment = "comment";

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, comment);
        newTracking.AddEvent(newNewEvent);

        List<NewEvent> newEventCollectionInTracking = newTracking.GetEventCollection();

        Assert.assertTrue(newEventCollectionInTracking.contains(newNewEvent));
    }

    @Test
    public void InTrackingWithRequiredCommentAddEventWithoutComment_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization ratingCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Required;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), scaleCustomization, ratingCustomization, commentCustomization, "", "");

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            newTracking.AddEvent(newNewEvent);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void InTrackingWithOptionalCommentAddEventWithoutComment_ThereIsNoException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Optional;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            newTracking.AddEvent(newNewEvent);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        Assert.assertFalse(thrown);
    }

    @Test
    public void InTrackingWithoutCommentAddEventWithComment_ThereIsNoException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        NewTracking newTracking = new NewTracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "", "");
        String comment = "comment";

        NewEvent newNewEvent = new NewEvent(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, comment);

        try {
            newTracking.AddEvent(newNewEvent);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        Assert.assertTrue(true);
    }

    @Test
    public void SendNotEmptyCounterCustomizationInEditTracking_CounterMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        NewTracking newTracking = new NewTracking(trackingName, trackingId, counter, scale, comment, "", "");

        TrackingCustomization newScale = TrackingCustomization.Required;

        newTracking.EditTracking(newScale, null, null, null, null, null);

        Assert.assertEquals(newTracking.GetScaleCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyScaleCustomizationInEditTracking_ScaleMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        NewTracking newTracking = new NewTracking(trackingName, trackingId, counter, scale, comment, "", "");

        TrackingCustomization newRating = TrackingCustomization.Required;

        newTracking.EditTracking(null, newRating, null, null, null, null);

        Assert.assertEquals(newTracking.GetRatingCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyCommentCustomizationInEditTracking_CommentMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        NewTracking newTracking = new NewTracking(trackingName, trackingId, counter, scale, comment, "", "");

        TrackingCustomization newComment = TrackingCustomization.Required;

        newTracking.EditTracking(null, null, newComment, null, null, null);

        Assert.assertEquals(newTracking.GetCommentCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyTrackingNameInEditTracking_CounterMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        NewTracking newTracking = new NewTracking(trackingName, trackingId, counter, scale, comment, "", "");

        String newName = "new name";
        String newTrackingName = newName;

        newTracking.EditTracking(null, null, null, newTrackingName, null, null);

        Assert.assertEquals(newTracking.GetTrackingName(), newName);
    }

    @Test
    public void TryToEditNotExistingEvent_ThrowException()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID idOfNotExistingEvent = UUID.randomUUID();
        NewTracking newTracking = new NewTracking("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None, "", "");

        NewEvent newEvent = new NewEvent(eventId, trackingID, evDate, null, null, null);

        newTracking.AddEvent(newEvent);

        try {
            newTracking.EditEvent(idOfNotExistingEvent, null, null, null, null);
        }
        catch (IllegalArgumentException e)
        {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void EditCounterOfEvent_Success()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        NewTracking newTracking = new NewTracking("name",
                trackingID,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None, "", "");

        Double newEventCount = 2.0;
        NewEvent newEvent = new NewEvent(eventId, trackingID, evDate, 1.0, null, null);

        newTracking.AddEvent(newEvent);
        newTracking.EditEvent(eventId, 2.0, null, null, null);

        List<NewEvent> newEventCollection = newTracking.GetEventCollection();
        NewEvent editedNewEvent = newEventCollection.get(0);

        Assert.assertEquals(editedNewEvent.GetScale(), newEventCount);
    }

    @Test
    public void EditScaleOfEvent_Success()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        NewTracking newTracking = new NewTracking("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None, "", "");

        Rating eventRating = new Rating(1);
        Rating newEventRating = new Rating(2);
        NewEvent newEvent = new NewEvent(eventId, trackingID, evDate, null, eventRating, null);

        newTracking.AddEvent(newEvent);
        newTracking.EditEvent(eventId, null, newEventRating, null, null);

        List<NewEvent> newEventCollection = newTracking.GetEventCollection();
        NewEvent editedNewEvent = newEventCollection.get(0);

        Assert.assertEquals(editedNewEvent.GetRating(), newEventRating);
    }

    @Test
    public void EditCommentOfEvent_Success()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        NewTracking newTracking = new NewTracking("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.Required, "", "");

        String eventComment = "name";
        String newEventComment = "new name";
        NewEvent newEvent = new NewEvent(eventId, trackingID, evDate, null, null, eventComment);

        newTracking.AddEvent(newEvent);
        newTracking.EditEvent(eventId, null, null, newEventComment, null);

        List<NewEvent> newEventCollection = newTracking.GetEventCollection();
        NewEvent editedNewEvent = newEventCollection.get(0);

        Assert.assertEquals(editedNewEvent.GetComment(), newEventComment);
    }

    @Test
    public void EditDateOfEvent_Success()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        NewTracking newTracking = new NewTracking("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None, "", "");

        NewEvent newEvent = new NewEvent(eventId, trackingID, evDate, null, null, null);

        Date newDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        newTracking.AddEvent(newEvent);
        newTracking.EditEvent(eventId, null, null, null, newDate);

        List<NewEvent> newEventCollection = newTracking.GetEventCollection();
        NewEvent editedNewEvent = newEventCollection.get(0);

        Assert.assertEquals(editedNewEvent.GetEventDate(), newDate);
    }
}
