package ru.lod_misis.ithappened;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;

public class TrackingV1UnitTest {

    Date evDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

    @Test
    public void InTrackingWithRequiredCounterAddEventWithCounter_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization ratingCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                scaleCustomization,
                ratingCustomization,
                commentCustomization,
                TrackingCustomization.None, "", "");
        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate,1.1, null, null, 0., 0.);
        trackingV1.AddEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.GetEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithOptionalCounterAddEventWithCounter_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Optional;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, 1.1, null, null, 0., 0.);
        trackingV1.AddEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.GetEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithRequiredCounterAddEventWithoutCounter_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Required;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null, 0., 0.);

        try {
            trackingV1.AddEvent(newEventV1);
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

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null, 0., 0.);

        try {
            trackingV1.AddEvent(newEventV1);
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

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null, 0., 0.);

        try {
            trackingV1.AddEvent(newEventV1);
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

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, rating, null, 0., 0.);
        trackingV1.AddEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.GetEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithOptionalScaleAddEventWithScale_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Optional;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, rating, null, 0., 0.);
        trackingV1.AddEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.GetEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithRequiredScaleAddEventWithoutScale_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null, 0., 0.);

        try {
            trackingV1.AddEvent(newEventV1);
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

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null, 0., 0.);

        try {
            trackingV1.AddEvent(newEventV1);
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

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, rating, null, 0., 0.);

        try {
            trackingV1.AddEvent(newEventV1);
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

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");

        String comment = "comment";

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, comment, 0., 0.);
        trackingV1.AddEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.GetEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithOptionalCommentAddEventWithComment_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Optional;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");
        String comment = "comment";

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, comment, 0., 0.);
        trackingV1.AddEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.GetEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithRequiredCommentAddEventWithoutComment_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization ratingCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Required;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                scaleCustomization,
                ratingCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null, 0., 0.);

        try {
            trackingV1.AddEvent(newEventV1);
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

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, null, 0., 0.);

        try {
            trackingV1.AddEvent(newEventV1);
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

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                TrackingCustomization.None,
                "",
                "");
        String comment = "comment";

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(), evDate, null, null, comment, 0., 0.);

        try {
            trackingV1.AddEvent(newEventV1);
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
        TrackingV1 trackingV1 = new TrackingV1(
                trackingName,
                trackingId,
                counter,
                scale,
                comment,
                TrackingCustomization.None,
                "",
                "");

        TrackingCustomization newScale = TrackingCustomization.Required;

        trackingV1.EditTracking(newScale, null, null, null, null, null, null);

        Assert.assertEquals(trackingV1.GetScaleCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyScaleCustomizationInEditTracking_ScaleMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        TrackingV1 trackingV1 = new TrackingV1(
                trackingName,
                trackingId,
                counter,
                scale,
                comment,
                TrackingCustomization.None,
                "",
                "");

        TrackingCustomization newRating = TrackingCustomization.Required;

        trackingV1.EditTracking(null, newRating, null, null, null, null, null);

        Assert.assertEquals(trackingV1.GetRatingCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyCommentCustomizationInEditTracking_CommentMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        TrackingV1 trackingV1 = new TrackingV1(
                trackingName,
                trackingId,
                counter,
                scale,
                comment,
                TrackingCustomization.None,
                "",
                "");

        TrackingCustomization newComment = TrackingCustomization.Required;

        trackingV1.EditTracking(null, null, newComment, null, null, null, null);

        Assert.assertEquals(trackingV1.GetCommentCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyTrackingNameInEditTracking_CounterMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";
        TrackingV1 trackingV1 = new TrackingV1(
                trackingName,
                trackingId,
                counter,
                scale,
                comment,
                TrackingCustomization.None,
                "",
                "");

        String newName = "new name";
        String newTrackingName = newName;

        trackingV1.EditTracking(null, null, null, TrackingCustomization.None, newTrackingName, null, null);

        Assert.assertEquals(trackingV1.GetTrackingName(), newName);
    }

    @Test
    public void TryToEditNotExistingEvent_ThrowException()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID idOfNotExistingEvent = UUID.randomUUID();
        TrackingV1 trackingV1 = new TrackingV1("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1 = new EventV1(eventId, trackingID, evDate, null, null, null, 0., 0.);

        trackingV1.AddEvent(eventV1);

        try {
            trackingV1.EditEvent(idOfNotExistingEvent, null, null, null, null, null, null);
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
        TrackingV1 trackingV1 = new TrackingV1("name",
                trackingID,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        Double newEventCount = 2.0;
        EventV1 eventV1 = new EventV1(eventId, trackingID, evDate, 1.0, null, null, 0., 0.);

        trackingV1.AddEvent(eventV1);
        trackingV1.EditEvent(eventId, 2.0, null, null, 0., 0., null);

        List<EventV1> eventV1Collection = trackingV1.GetEventHistory();
        EventV1 editedEventV1 = eventV1Collection.get(0);

        Assert.assertEquals(editedEventV1.GetScale(), newEventCount);
    }

    @Test
    public void EditScaleOfEvent_Success()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        TrackingV1 trackingV1 = new TrackingV1("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        Rating eventRating = new Rating(1);
        Rating newEventRating = new Rating(2);
        EventV1 eventV1 = new EventV1(eventId, trackingID, evDate, null, eventRating, null, 0., 0.);

        trackingV1.AddEvent(eventV1);
        trackingV1.EditEvent(eventId, null, newEventRating, null, 0., 0., null);

        List<EventV1> eventV1Collection = trackingV1.GetEventHistory();
        EventV1 editedEventV1 = eventV1Collection.get(0);

        Assert.assertEquals(editedEventV1.GetRating(), newEventRating);
    }

    @Test
    public void EditCommentOfEvent_Success()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        TrackingV1 trackingV1 = new TrackingV1("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                "",
                "");

        String eventComment = "name";
        String newEventComment = "new name";
        EventV1 eventV1 = new EventV1(eventId, trackingID, evDate, null, null, eventComment, 0., 0.);

        trackingV1.AddEvent(eventV1);
        trackingV1.EditEvent(eventId, null, null, newEventComment, 0., 0., null);

        List<EventV1> eventV1Collection = trackingV1.GetEventHistory();
        EventV1 editedEventV1 = eventV1Collection.get(0);

        Assert.assertEquals(editedEventV1.GetComment(), newEventComment);
    }

    @Test
    public void EditDateOfEvent_Success()
    {
        boolean thrown = false;
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        TrackingV1 trackingV1 = new TrackingV1("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1 = new EventV1(eventId, trackingID, evDate, null, null, null, 0., 0.);

        Date newDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        trackingV1.AddEvent(eventV1);
        trackingV1.EditEvent(eventId, null, null, null, 0., 0., newDate);

        List<EventV1> eventV1Collection = trackingV1.GetEventHistory();
        EventV1 editedEventV1 = eventV1Collection.get(0);

        Assert.assertEquals(editedEventV1.GetEventDate(), newDate);
    }
}
