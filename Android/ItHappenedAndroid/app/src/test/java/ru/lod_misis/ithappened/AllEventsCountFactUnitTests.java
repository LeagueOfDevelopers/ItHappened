package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.AllEventsCountFact;

/**
 * Created by Пользователь on 15.03.2018.
 */

public class AllEventsCountFactUnitTests {

    @Test
    public void HaveTrackingCollectionWithTwoTrackings_GetEventCountShouldReturnTwo(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        Tracking secondTracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        Event event = new Event(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Event secondEvent = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        tracking.AddEvent(event);
        secondTracking.AddEvent(secondEvent);

        List<Tracking> trackingCollection = new ArrayList<>();
        trackingCollection.add(tracking);
        trackingCollection.add(secondTracking);

        AllEventsCountFact fact = new AllEventsCountFact(trackingCollection);
        Assert.assertEquals(fact.getEventCount(), 2);
    }

    @Test
    public void HaveTrackingCollectionWithTwoTrackings_GetTextDescriptionShouldReturnValidData(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        Tracking secondTracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        Event event = new Event(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Event secondEvent = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        tracking.AddEvent(event);
        secondTracking.AddEvent(secondEvent);

        List<Tracking> trackingCollection = new ArrayList<>();
        trackingCollection.add(tracking);
        trackingCollection.add(secondTracking);

        AllEventsCountFact fact = new AllEventsCountFact(trackingCollection);
        fact.getEventCount();
        Assert.assertEquals(fact.TextDescription(), "У вас произошло уже 2 событий!");
    }



}
