package ru.lod_misis.ithappened;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;

public class TrackingUnitTest {

    Date evDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

    @Test
    public void InTrackingWithRequiredCounterAddEventWithCounter_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization ratingCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), scaleCustomization, ratingCustomization, commentCustomization, "");
        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate,1.1, null , null);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithOptionalCounterAddEventWithCounter_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Optional;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, 1.1, null, null);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithRequiredCounterAddEventWithoutCounter_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Required;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            tracking.AddEvent(newEvent);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            tracking.AddEvent(newEvent);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            tracking.AddEvent(newEvent);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, rating, null);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithOptionalScaleAddEventWithScale_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Optional;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, rating, null);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithRequiredScaleAddEventWithoutScale_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            tracking.AddEvent(newEvent);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            tracking.AddEvent(newEvent);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, rating, null);

        try {
            tracking.AddEvent(newEvent);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");

        String comment = "comment";

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithOptionalCommentAddEventWithComment_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Optional;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");
        String comment = "comment";

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithRequiredCommentAddEventWithoutComment_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization ratingCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Required;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), scaleCustomization, ratingCustomization, commentCustomization, "");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            tracking.AddEvent(newEvent);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null);

        try {
            tracking.AddEvent(newEvent);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization, "");
        String comment = "comment";

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, comment);

        try {
            tracking.AddEvent(newEvent);
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
        Tracking tracking = new Tracking(trackingName, trackingId, counter, scale, comment, "");

        TrackingCustomization newScale = TrackingCustomization.Required;

        tracking.EditTracking(newScale, null, null, null, null);

        Assert.assertEquals(tracking.GetScaleCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyScaleCustomizationInEditTracking_ScaleMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        Tracking tracking = new Tracking(trackingName, trackingId, counter, scale, comment, "");

        TrackingCustomization newRating = TrackingCustomization.Required;

        tracking.EditTracking(null, newRating, null, null, null);

        Assert.assertEquals(tracking.GetRatingCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyCommentCustomizationInEditTracking_CommentMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        Tracking tracking = new Tracking(trackingName, trackingId, counter, scale, comment, "");

        TrackingCustomization newComment = TrackingCustomization.Required;

        tracking.EditTracking(null, null, newComment, null, null);

        Assert.assertEquals(tracking.GetCommentCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyTrackingNameInEditTracking_CounterMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        Tracking tracking = new Tracking(trackingName, trackingId, counter, scale, comment, "");

        String newName = "new name";
        String newTrackingName = newName;

        tracking.EditTracking(null, null, null, newTrackingName, null);

        Assert.assertEquals(tracking.GetTrackingName(), newName);
    }

    @Test
    public void TryToEditNotExistingEvent_ThrowException()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID idOfNotExistingEvent = UUID.randomUUID();
        Tracking tracking = new Tracking("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None, "");

        Event event = new Event(eventId, trackingID, evDate, null, null, null);

        tracking.AddEvent(event);

        try {
            tracking.EditEvent(idOfNotExistingEvent, null, null, null, null);
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
        Tracking tracking = new Tracking("name",
                trackingID,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None, "");

        Double newEventCount = 2.0;
        Event event = new Event(eventId, trackingID, evDate, 1.0, null, null);

        tracking.AddEvent(event);
        tracking.EditEvent(eventId, 2.0, null, null, null);

        List<Event> eventCollection = tracking.GetEventCollection();
        Event editedEvent = eventCollection.get(0);

        Assert.assertEquals(editedEvent.GetScale(), newEventCount);
    }

    @Test
    public void EditScaleOfEvent_Success()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Tracking tracking = new Tracking("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None, "");

        Rating eventRating = new Rating(1);
        Rating newEventRating = new Rating(2);
        Event event = new Event(eventId, trackingID, evDate, null, eventRating, null);

        tracking.AddEvent(event);
        tracking.EditEvent(eventId, null, newEventRating, null, null);

        List<Event> eventCollection = tracking.GetEventCollection();
        Event editedEvent = eventCollection.get(0);

        Assert.assertEquals(editedEvent.GetRating(), newEventRating);
    }

    @Test
    public void EditCommentOfEvent_Success()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Tracking tracking = new Tracking("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.Required, "");

        String eventComment = "name";
        String newEventComment = "new name";
        Event event = new Event(eventId, trackingID, evDate, null, null, eventComment);

        tracking.AddEvent(event);
        tracking.EditEvent(eventId, null, null, newEventComment, null);

        List<Event> eventCollection = tracking.GetEventCollection();
        Event editedEvent = eventCollection.get(0);

        Assert.assertEquals(editedEvent.GetComment(), newEventComment);
    }

    @Test
    public void EditDateOfEvent_Success()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Tracking tracking = new Tracking("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None, "");

        Event event = new Event(eventId, trackingID, evDate, null, null, null);

        Date newDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        tracking.AddEvent(event);
        tracking.EditEvent(eventId, null, null, null, newDate);

        List<Event> eventCollection = tracking.GetEventCollection();
        Event editedEvent = eventCollection.get(0);

        Assert.assertEquals(editedEvent.GetEventDate(), newDate);
    }
}
