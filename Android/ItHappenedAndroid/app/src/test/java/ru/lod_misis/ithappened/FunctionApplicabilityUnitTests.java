package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.FunctionApplicability;

public class FunctionApplicabilityUnitTests {

    Date evDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

    @Test
    public void WeHaveTrackingCollection_MostFrequentEventFAShouldReurnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");
        Event eventSecond = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");
        Event eventThird = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

               tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);
        tracking.AddEvent(eventThird);

        List<Tracking> trackingList = new ArrayList<>();
        trackingList.add(tracking);

        Assert.assertEquals(FunctionApplicability.mostFrequentEventApplicability(trackingList), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackings_MostFrequentEventFAShouldReturnValidFact(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        Tracking secondTracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");
        Event eventSecond = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");
        Event eventThird = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

        Event secondEventFirst = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), evDate, null, null,  "122");
        Event secondEventSecond = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), evDate, null, null,  "122");
        Event secondEventThird = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), evDate, null, null,  "122");

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
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        Tracking secondTracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        List<Tracking> trackingList = new ArrayList<>();
        trackingList.add(tracking);
        trackingList.add(secondTracking);

        Assert.assertEquals(FunctionApplicability.mostFrequentEventApplicability(trackingList), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackings_AllEventsCountFAShouldReturnValidFact(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        Tracking secondTracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");
        Event eventSecond = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");
        Event eventThird = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

        Event secondEventFirst = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), evDate, null, null,  "122");
        Event secondEventSecond = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), evDate, null, null,  "122");
        Event secondEventThird = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), evDate, null, null,  "122");

        tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);
        tracking.AddEvent(eventThird);

        secondTracking.AddEvent(secondEventFirst);
        secondTracking.AddEvent(secondEventSecond);
        secondTracking.AddEvent(secondEventThird);

        List<Tracking> trackingList = new ArrayList<>();
        trackingList.add(tracking);
        trackingList.add(secondTracking);

        Assert.assertNotEquals(FunctionApplicability.allEventsCountFactApplicability(trackingList), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackingsButWithThreeEvents_AllEventsCountFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        Tracking secondTracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

        Event secondEventFirst = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), evDate, null, null,  "122");
        Event secondEventSecond = new Event(UUID.randomUUID(), secondTracking.GetTrackingID(), evDate, null, null,  "122");

        tracking.AddEvent(eventFirst);

        secondTracking.AddEvent(secondEventFirst);
        secondTracking.AddEvent(secondEventSecond);

        List<Tracking> trackingList = new ArrayList<>();
        trackingList.add(tracking);
        trackingList.add(secondTracking);

        Assert.assertEquals(FunctionApplicability.allEventsCountFactApplicability(trackingList), null);
    }

    @Test
    public void WeHaveTrackingWithoutEvents_TrackingsEventCountFAShouldReturnNull(){
        Tracking tracking = new Tracking("",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required, "");

        Assert.assertEquals(FunctionApplicability.trackingEventsCountApplicability(tracking), null);
    }

    @Test
    public void WeHaveTrackingWithEvents_TrackingsEventsFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

        tracking.AddEvent(eventFirst);

        Assert.assertNotEquals(FunctionApplicability.trackingEventsCountApplicability(tracking), null);
    }

    @Test
    public void WeHaveTrackingWithoutRating_AvrgRatingFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

        tracking.AddEvent(eventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }

    @Test
    public void WeHaveTrackingWithRatingButWithoutEvents_AvrgRatingFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Required, TrackingCustomization.Required, "");

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }

    @Test
    public void WeHaveTrackingWithOptionalRatingButWithNullRatingInEvent_AvrgRatingFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Optional, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

        tracking.AddEvent(eventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }


    @Test
    public void WeHaveValidTracking_AvrgRatingFAShouldntReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Required, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, new Rating(4),  "122");
        Event eventSecond = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, new Rating(5),  "122");

        tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);

        Assert.assertNotEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }


    @Test
    public void WeHaveTrackingWithoutScale_AvrgScaleFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

        tracking.AddEvent(eventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }

    @Test
    public void WeHaveTrackingWithScaleButWithoutEvents_AvrgScaleFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Required, TrackingCustomization.Required, TrackingCustomization.Required, "");

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }

    @Test
    public void WeHaveTrackingWithOptionalScaleButWithNullRatingInEvent_AvrgScaleFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Optional, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

        tracking.AddEvent(eventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }


    @Test
    public void WeHaveValidTracking_AvrgScaleFAShouldntReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Required, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, 10.0, new Rating(4),  "122");
        Event eventSecond = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, -15.4, new Rating(5),  "122");

        tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);

        Assert.assertNotEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }



    @Test
    public void WeHaveTrackingWithoutScale_SumScaleFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

        tracking.AddEvent(eventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }

    @Test
    public void WeHaveTrackingWithScaleButWithoutEvents_SumScaleFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Required, TrackingCustomization.Required, TrackingCustomization.Required, "");

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }

    @Test
    public void WeHaveTrackingWithOptionalScaleButWithNullRatingInEvent_SumScaleFAShouldReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Optional, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, null, null,  "122");

        tracking.AddEvent(eventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }


    @Test
    public void WeHaveValidTracking_SumScaleFAShouldntReturnNull(){
        Tracking tracking = new Tracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Required, TrackingCustomization.Required, "");

        Event eventFirst = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, 10.0, new Rating(4),  "122");
        Event eventSecond = new Event(UUID.randomUUID(), tracking.GetTrackingID(), evDate, -15.4, new Rating(5),  "122");

        tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);

        Assert.assertNotEquals(FunctionApplicability.avrgRatingApplicability(tracking), null);
    }
}
