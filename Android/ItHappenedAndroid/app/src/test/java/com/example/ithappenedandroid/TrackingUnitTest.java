package com.example.ithappenedandroid;

import android.support.annotation.Nullable;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Scale;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Ded on 10.12.2017.
 */
public class TrackingUnitTest {
    @Test
    public void InTrackingWithRequiredCounterAddEventWithCounter_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Required;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.ofNullable(1.1);
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.ofNullable(1.1);
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);

        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Integer scaleValue = 5;
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.ofNullable(new Scale(scaleValue));
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Integer scaleValue = 5;
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.ofNullable(new Scale(scaleValue));
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Integer scaleValue = 5;
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.ofNullable(new Scale(scaleValue));
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);

        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.ofNullable("comment");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.ofNullable("comment");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithRequiredCommentAddEventWithoutComment_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Required;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

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

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);

        Optional<Double> count = Optional.empty();
        Optional<Scale> scale = Optional.empty();
        Optional<String> comment = Optional.ofNullable("comment");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

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
        Tracking tracking = new Tracking(trackingName, trackingId, counter, scale, comment);

        Optional<TrackingCustomization> newCounter = Optional.ofNullable(TrackingCustomization.Required);
        Optional<TrackingCustomization> newScale = Optional.empty();
        Optional<TrackingCustomization> newComment = Optional.empty();
        Optional<String> newTrackingName = Optional.empty();

        tracking.EditTracking(newCounter, newScale, newComment, newTrackingName);

        Assert.assertEquals(tracking.GetCounterCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyScaleCustomizationInEditTracking_ScaleMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        Tracking tracking = new Tracking(trackingName, trackingId, counter, scale, comment);

        Optional<TrackingCustomization> newCounter = Optional.empty();
        Optional<TrackingCustomization> newScale = Optional.ofNullable(TrackingCustomization.Required);
        Optional<TrackingCustomization> newComment = Optional.empty();
        Optional<String> newTrackingName = Optional.empty();

        tracking.EditTracking(newCounter, newScale, newComment, newTrackingName);

        Assert.assertEquals(tracking.GetScaleCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyCommentCustomizationInEditTracking_CommentMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        Tracking tracking = new Tracking(trackingName, trackingId, counter, scale, comment);

        Optional<TrackingCustomization> newCounter = Optional.empty();
        Optional<TrackingCustomization> newScale = Optional.empty();
        Optional<TrackingCustomization> newComment = Optional.ofNullable(TrackingCustomization.Required);
        Optional<String> newTrackingName = Optional.empty();

        tracking.EditTracking(newCounter, newScale, newComment, newTrackingName);

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
        Tracking tracking = new Tracking(trackingName, trackingId, counter, scale, comment);

        String newName = "new name";
        Optional<TrackingCustomization> newCounter = Optional.empty();
        Optional<TrackingCustomization> newScale = Optional.empty();
        Optional<TrackingCustomization> newComment = Optional.empty();
        Optional<String> newTrackingName = Optional.ofNullable(newName);

        tracking.EditTracking(newCounter, newScale, newComment, newTrackingName);

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
                TrackingCustomization.None);

        Event event = new Event(eventId, trackingID, Optional.empty(), Optional.empty(), Optional.empty());

        tracking.AddEvent(event);

        try {
            tracking.EditEvent(idOfNotExistingEvent, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
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
                TrackingCustomization.None);

        Optional<Double> eventCount = Optional.ofNullable(1.0);
        Optional<Double> newEventCount = Optional.ofNullable(2.0);
        Event event = new Event(eventId, trackingID, eventCount, Optional.empty(), Optional.empty());

        tracking.AddEvent(event);
        tracking.EditEvent(eventId, newEventCount, Optional.empty(), Optional.empty(), Optional.empty());

        List<Event> eventCollection = tracking.GetEventCollection();
        Event editedEvent = eventCollection.get(0);

        Assert.assertEquals(editedEvent.GetCount(), newEventCount);
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
                TrackingCustomization.None);

        Optional<Scale> eventScale = Optional.ofNullable(new Scale(1));
        Optional<Scale> newEventScale = Optional.ofNullable(new Scale(2));
        Event event = new Event(eventId, trackingID, Optional.empty(), eventScale, Optional.empty());

        tracking.AddEvent(event);
        tracking.EditEvent(eventId, Optional.empty(), newEventScale, Optional.empty(), Optional.empty());

        List<Event> eventCollection = tracking.GetEventCollection();
        Event editedEvent = eventCollection.get(0);

        Assert.assertEquals(editedEvent.GetScale(), newEventScale);
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
                TrackingCustomization.Required);

        Optional<String> eventComment = Optional.ofNullable("name");
        Optional<String> newEventComment = Optional.ofNullable("new name");
        Event event = new Event(eventId, trackingID, Optional.empty(), Optional.empty(), eventComment);

        tracking.AddEvent(event);
        tracking.EditEvent(eventId, Optional.empty(), Optional.empty(), newEventComment, Optional.empty());

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
                TrackingCustomization.None);

        Event event = new Event(eventId, trackingID, Optional.empty(), Optional.empty(), Optional.empty());

        Optional<TimeZone> newDate = Optional.ofNullable(TimeZone.getDefault());

        tracking.AddEvent(event);
        tracking.EditEvent(eventId, Optional.empty(), Optional.empty(), Optional.empty(), newDate);

        List<Event> eventCollection = tracking.GetEventCollection();
        Event editedEvent = eventCollection.get(0);

        Assert.assertEquals(editedEvent.GetEventDate(), newDate.get());
    }
}
