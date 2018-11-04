package ru.lod_misis.ithappened;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Models.Rating;
import ru.lod_misis.ithappened.Domain.Models.TrackingCustomization;

public class TrackingV1UnitTest {

    Date evDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

    @Test
    public void InTrackingWithRequiredCounterAddEventWithCounter_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization ratingCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(trackingname,
                UUID.randomUUID(),
                scaleCustomization,
                ratingCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization, "", "");
        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate,1.1, null, null, null, null, "");
        trackingV1.addEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.getEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithOptionalCounterAddEventWithCounter_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Optional;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, 1.1, null, null, null, null, "");
        trackingV1.addEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.getEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithRequiredCounterAddEventWithoutCounter_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Required;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, null, null, null, null, "");

        try {
            trackingV1.addEvent(newEventV1);
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
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, null, null, null, null, "");

        try {
            trackingV1.addEvent(newEventV1);
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
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),

                evDate, null, null, null, null, null, "");

        try {
            trackingV1.addEvent(newEventV1);
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
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, rating, null, null, null, "");
        trackingV1.addEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.getEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithOptionalScaleAddEventWithScale_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Optional;
        TrackingCustomization commentCustomization = TrackingCustomization.None;
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, rating, null, null, null, "");
        trackingV1.addEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.getEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithRequiredScaleAddEventWithoutScale_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization commentCustomization = TrackingCustomization.None;
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, null, null, null, null, "");

        try {
            trackingV1.addEvent(newEventV1);
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
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, null, null, null, null, "");

        try {
            trackingV1.addEvent(newEventV1);
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
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");
        Integer scaleValue = 5;
        Rating rating = new Rating(scaleValue);

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, rating, null, null, null, "");

        try {
            trackingV1.addEvent(newEventV1);
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
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        String comment = "comment";

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, null, comment, null, null, "");
        trackingV1.addEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.getEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithOptionalCommentAddEventWithComment_ThereIsNoException() {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Optional;
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");
        String comment = "comment";

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, null, comment, null, null, "");
        trackingV1.addEvent(newEventV1);

        List<EventV1> eventV1CollectionInTracking = trackingV1.getEventHistory();

        Assert.assertTrue(eventV1CollectionInTracking.contains(newEventV1));
    }

    @Test
    public void InTrackingWithRequiredCommentAddEventWithoutComment_ThrowException() {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization ratingCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Required;
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                scaleCustomization,
                ratingCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, null, null, null, null, "");

        try {
            trackingV1.addEvent(newEventV1);
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
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, null, null, null, null, "");

        try {
            trackingV1.addEvent(newEventV1);
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
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;

        TrackingV1 trackingV1 = new TrackingV1(
                trackingname,
                UUID.randomUUID(),
                countCustomization,
                scaleCustomization,
                commentCustomization,
                geopositionCustomization,
                photoCustomization,
                "",
                "");
        String comment = "comment";

        EventV1 newEventV1 = new EventV1(UUID.randomUUID(), UUID.randomUUID(),
                evDate, null, null, comment, null, null, "");

        try {
            trackingV1.addEvent(newEventV1);
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
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";

        TrackingV1 trackingV1 = new TrackingV1(
                trackingName,
                trackingId,
                counter,
                scale,
                comment,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        TrackingCustomization newScale = TrackingCustomization.Required;

        trackingV1.editTracking(newScale, null, null,
                null, null, null, null, null);

        Assert.assertEquals(trackingV1.getScaleCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyScaleCustomizationInEditTracking_ScaleMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";

        TrackingV1 trackingV1 = new TrackingV1(
                trackingName,
                trackingId,
                counter,
                scale,
                comment,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        TrackingCustomization newRating = TrackingCustomization.Required;

        trackingV1.editTracking(null, newRating, null, null,
                null, null, null, null);

        Assert.assertEquals(trackingV1.getRatingCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyCommentCustomizationInEditTracking_CommentMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";

        TrackingV1 trackingV1 = new TrackingV1(
                trackingName,
                trackingId,
                counter,
                scale,
                comment,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        TrackingCustomization newComment = TrackingCustomization.Required;

        trackingV1.editTracking(null, null, newComment, null,
                null, null, null, null);

        Assert.assertEquals(trackingV1.getCommentCustomization(), TrackingCustomization.Required);
    }

    @Test
    public void SendNotEmptyTrackingNameInEditTracking_CounterMustChange()
    {
        TrackingCustomization counter = TrackingCustomization.None;
        TrackingCustomization scale = TrackingCustomization.None;
        TrackingCustomization comment = TrackingCustomization.None;
        TrackingCustomization geopositionCustomization = TrackingCustomization.None;
        TrackingCustomization photoCustomization = TrackingCustomization.None;
        UUID trackingId = UUID.randomUUID();
        String trackingName = "name";

        TrackingV1 trackingV1 = new TrackingV1(
                trackingName,
                trackingId,
                counter,
                scale,
                comment,
                geopositionCustomization,
                photoCustomization,
                "",
                "");

        String newName = "new name";
        trackingV1.editTracking(null, null, null,
                null, null, newName, null, null);

        Assert.assertEquals(trackingV1.getTrackingName(), newName);
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
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1 = new EventV1(eventId, trackingID, evDate, null, null,
                null, null, null, "");

        trackingV1.addEvent(eventV1);

        try {
            trackingV1.editEvent(idOfNotExistingEvent, null, null,
                    null, null, null, null, null);
        }
        catch (IllegalArgumentException e)
        {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void EditScaleOfEvent_Success()
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
                TrackingCustomization.None,
                "",
                "");

        Double newEventCount = 2.0;
        EventV1 eventV1 = new EventV1(eventId, trackingID, evDate, 1.0,
                null, null, null, null, "");

        trackingV1.addEvent(eventV1);
        trackingV1.editEvent(eventId, 2.0, null, null,
                null, null, null, null);

        List<EventV1> eventV1Collection = trackingV1.getEventHistory();
        EventV1 editedEventV1 = eventV1Collection.get(0);

        Assert.assertEquals(editedEventV1.getScale(), newEventCount);
    }

    @Test
    public void EditRatingOfEvent_Success()
    {
        UUID trackingID = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        TrackingV1 trackingV1 = new TrackingV1("name",
                trackingID,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        Rating eventRating = new Rating(1);
        Rating newEventRating = new Rating(2);
        EventV1 eventV1 = new EventV1(eventId, trackingID, evDate,
                null, eventRating, null, null, null, "");

        trackingV1.addEvent(eventV1);
        trackingV1.editEvent(eventId, null, newEventRating, null,
                null, null, null, null);

        List<EventV1> eventV1Collection = trackingV1.getEventHistory();
        EventV1 editedEventV1 = eventV1Collection.get(0);

        Assert.assertEquals(editedEventV1.getRating(), newEventRating);
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
                TrackingCustomization.None,
                "",
                "");

        String eventComment = "name";
        String newEventComment = "new name";
        EventV1 eventV1 = new EventV1(eventId, trackingID, evDate,
                null, null, eventComment, null, null, "");

        trackingV1.addEvent(eventV1);
        trackingV1.editEvent(eventId, null, null, newEventComment,
                null, null, null, null);

        List<EventV1> eventV1Collection = trackingV1.getEventHistory();
        EventV1 editedEventV1 = eventV1Collection.get(0);

        Assert.assertEquals(editedEventV1.getComment(), newEventComment);
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
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1 = new EventV1(eventId, trackingID, evDate,
                null, null, null, null, null, null);

        Date newDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        trackingV1.addEvent(eventV1);
        trackingV1.editEvent(eventId, null, null, null,
                null, null, null, newDate);

        List<EventV1> eventV1Collection = trackingV1.getEventHistory();
        EventV1 editedEventV1 = eventV1Collection.get(0);

        Assert.assertEquals(editedEventV1.getEventDate(), newDate);
    }
}
