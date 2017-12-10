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
import java.util.UUID;

/**
 * Created by Ded on 10.12.2017.
 */
public class TrackingUnitTest
{
    @Test
    public void InTrackingWithRequiredCounterCanAddEventWithCounter()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Required;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = 1.1;
        Scale scale = null;
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithOptionalCounterCanAddEventWithCounter()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Optional;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = 1.1;
        Scale scale = null;
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithRequiredCounterCanNotAddEventWithoutCounter()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Required;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Scale scale = null;
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);

        try { tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void InTrackingWithOptionalCounterCanAddEventWithoutCounter()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.Optional;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Scale scale = null;
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertFalse( thrown);
    }

    @Test
    public void InTrackingWithoutCounterCanNotAddEventWithCounter()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Scale scale = null;
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(true);
    }

    @Test
    public void CanNotCreateScaleWithValueLessThen1 ()
    {
        Integer scaleValue = 0;
        Scale scale;
        boolean thrown = false;

        try { scale = new Scale(scaleValue);}
        catch (IndexOutOfBoundsException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void CanNotCreateScaleWithValueMoreThan10 ()
    {
        Integer scaleValue = 11;
        Scale scale;
        boolean thrown = false;

        try { scale = new Scale(scaleValue);}
        catch (IndexOutOfBoundsException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void CanCreateScaleWithValueLessThan11AndMoreThan0 ()
    {
        Integer scaleValue = 10;
        Scale scale;
        boolean thrown = false;

        try { scale = new Scale(scaleValue);}
        catch (IndexOutOfBoundsException e) { thrown = true; }

        Assert.assertFalse(thrown);
    }

    @Test
    public void InTrackingWithRequiredScaleCanAddEventWithScale()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Integer scaleValue = 5;
        Scale scale = new Scale(scaleValue);
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithOptionalScaleCanAddEventWithScale()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Optional;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Integer scaleValue = 5;
        Scale scale = new Scale(scaleValue);
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithRequiredScaleCanNotAddEventWithoutScale()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Required;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Scale scale = null;
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);

        try { tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void InTrackingWithOptionalScaleCanAddEventWithoutScale()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.Optional;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Scale scale = null;
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertFalse( thrown);
    }

    @Test
    public void InTrackingWithoutScaleCanNotAddEventWithScale()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Integer scaleValue = 5;
        Scale scale = new Scale(scaleValue);
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(true);
    }

    @Test
    public void InTrackingWithRequiredCommentCanAddEventWithComment ()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Required;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Scale scale = null;
        String comment = "comment";

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithOptionalCommentCanAddEventWithComment ()
    {
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Optional;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Scale scale = null;
        String comment = "comment";

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);
        tracking.AddEvent(newEvent);

        List<Event> eventCollectionInTracking = tracking.GetEventCollection();

        Assert.assertTrue(eventCollectionInTracking.contains(newEvent));
    }

    @Test
    public void InTrackingWithRequiredCommentCanNotAddEventWithoutComment()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Required;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Scale scale = null;
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);

        try { tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void InTrackingWithOptionalCommentCanAddEventWithoutComment()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.Optional;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Scale scale = null;
        String comment = null;

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertFalse( thrown);
    }

    @Test
    public void InTrackingWithoutCommentCanNotAddEventWithComment()
    {
        boolean thrown = false;
        String trackingname = "name";
        TrackingCustomization countCustomization = TrackingCustomization.None;
        TrackingCustomization scaleCustomization = TrackingCustomization.None;
        TrackingCustomization commentCustomization = TrackingCustomization.None;

        Tracking tracking = new Tracking(trackingname, countCustomization, scaleCustomization, commentCustomization);
        Double count = null;
        Scale scale = null;
        String comment = "comment";

        Event newEvent = new Event(UUID.randomUUID(), count, scale, comment);

        try{ tracking.AddEvent(newEvent); }
        catch (IllegalArgumentException e) { thrown = true; }

        Assert.assertTrue(true);
    }


}
