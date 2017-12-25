package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Rating;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;

import junit.framework.Assert;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Ded on 10.12.2017.
 */
public class TrackingUnitTest
{
    @Test
    public void InTrackingWithRequiredCounterAddEventWithCounter_ThereIsNoException()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Required;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.ofNullable(1.1);
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithOptionalCounterAddEventWithCounter_ThereIsNoException()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Optional;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.ofNullable(1.1);
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithRequiredCounterAddEventWithoutCounter_ThrowException()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Required;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(),  countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

        try { tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void InTrackingWithOptionalCounterAddEventWithoutCounter_ThereIsNoException()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Optional;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);

        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e)
        { thrown = true; }

        Assert.assertFalse( thrown);
    }

    @Test
    public void InTrackingWithoutCounterAddEventWithCounter_ThrowException()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(true);
    }

    @Test
    public void InTrackingWithRequiredScaleAddEventWithScale_ThereIsNoException()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Integer scaleValue = 5;
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.ofNullable(new Rating(scaleValue));
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithOptionalScaleAddEventWithScale_ThereIsNoException()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Optional;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Integer scaleValue = 5;
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.ofNullable(new Rating(scaleValue));
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithRequiredScaleAddEventWithoutScale_ThrowException()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

        try { tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void InTrackingWithOptionalScaleAddEventWithoutScale_ThereIsNoException()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Optional;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertFalse( thrown);
    }

    @Test
    public void InTrackingWithoutScaleAddEventWithScale_ThrowException()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Integer scaleValue = 5;
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.ofNullable(new Rating(scaleValue));
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(true);
    }

    @Test
    public void InTrackingWithRequiredCommentAddEventWithComment_ThereIsNoException ()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Required;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);

        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.ofNullable("comment");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithOptionalCommentAddEventWithComment_ThereIsNoException ()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Optional;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.ofNullable("comment");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithRequiredCommentAddEventWithoutComment_ThrowException()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Required;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

        try { tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void InTrackingWithOptionalCommentAddEventWithoutComment_ThereIsNoException()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Optional;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);
        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.empty();

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertFalse( thrown);
    }

    @Test
    public void InTrackingWithoutCommentAddEventWithComment_ThereIsNoException()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, UUID.randomUUID(), countCustomization, scaleCustomization, commentCustomization);

        Optional<Double> count = Optional.empty();
        Optional<Rating> scale = Optional.empty();
        Optional<String> comment = Optional.ofNullable("comment");

        Event newEvent = new Event(UUID.randomUUID(), UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(true);
    }


}
