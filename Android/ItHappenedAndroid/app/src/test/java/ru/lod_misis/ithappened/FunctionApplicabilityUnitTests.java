package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.FunctionApplicability;

/**
 * Created by Пользователь on 15.03.2018.
 */

public class FunctionApplicabilityUnitTests {

    @Test
    public void WeHaveTrackingCollection_MostFrequentEventFAShouldReurnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Event eventSecond = new Event(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Event eventThird = new Event(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");

               tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);
        tracking.AddEvent(eventThird);

        List<Tracking> trackingList = new ArrayList<>();
        trackingList.add(tracking);

        Assert.assertEquals(FunctionApplicability.mostFrequentEventApplicability(trackingList), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackings_MostFrequentEventFAShouldReturnValidFact(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        Tracking secondTracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Event eventSecond = new Event(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Event eventThird = new Event(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");

        Event secondEventFirst = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        Event secondEventSecond = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        Event secondEventThird = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");

        tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);
        tracking.AddEvent(eventThird);

        secondTracking.AddEvent(secondEventFirst);
        secondTracking.AddEvent(secondEventSecond);
        secondTracking.AddEvent(secondEventThird);

        List<Tracking> trackingList = new ArrayList<>();
        trackingList.add(tracking);
        trackingList.add(secondTracking);

        Assert.assertNotEquals(FunctionApplicability.mostFrequentEventApplicability(trackingList), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackingsButWithoutEvents_MostFrequentEventFAShouldReurnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        Tracking secondTracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);

        List<Tracking> trackingList = new ArrayList<>();
        trackingList.add(tracking);
        trackingList.add(secondTracking);

        Assert.assertEquals(FunctionApplicability.mostFrequentEventApplicability(trackingList), null);
    }


}
