package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.FunctionApplicability;

public class FunctionApplicabilityUnitTests {

    Date evDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

    @Test
    public void WeHaveTrackingCollection_MostFrequentEventFAShouldReurnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent newEventSecond = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent newEventThird = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

               newTracking.AddEvent(newEventFirst);
        newTracking.AddEvent(newEventSecond);
        newTracking.AddEvent(newEventThird);

        List<NewTracking> newTrackingList = new ArrayList<>();
        newTrackingList.add(newTracking);

        Assert.assertEquals(FunctionApplicability.mostFrequentEventApplicability(newTrackingList), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackings_MostFrequentEventFAShouldReturnValidFact(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        NewTracking secondNewTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent newEventSecond = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent newEventThird = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

        NewEvent secondNewEventFirst = new NewEvent(UUID.randomUUID(), secondNewTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent secondNewEventSecond = new NewEvent(UUID.randomUUID(), secondNewTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent secondNewEventThird = new NewEvent(UUID.randomUUID(), secondNewTracking.GetTrackingID(), evDate, null, null,  "122");

        newTracking.AddEvent(newEventFirst);
        newTracking.AddEvent(newEventSecond);
        newTracking.AddEvent(newEventThird);

        secondNewTracking.AddEvent(secondNewEventFirst);
        secondNewTracking.AddEvent(secondNewEventSecond);
        secondNewTracking.AddEvent(secondNewEventThird);

        List<NewTracking> newTrackingList = new ArrayList<>();
        newTrackingList.add(newTracking);
        newTrackingList.add(secondNewTracking);

        Assert.assertNotEquals(FunctionApplicability.mostFrequentEventApplicability(newTrackingList), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackingsButWithoutEvents_MostFrequentEventFAShouldReurnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        NewTracking secondNewTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        List<NewTracking> newTrackingList = new ArrayList<>();
        newTrackingList.add(newTracking);
        newTrackingList.add(secondNewTracking);

        Assert.assertEquals(FunctionApplicability.mostFrequentEventApplicability(newTrackingList), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackings_AllEventsCountFAShouldReturnValidFact(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        NewTracking secondNewTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent newEventSecond = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent newEventThird = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

        NewEvent secondNewEventFirst = new NewEvent(UUID.randomUUID(), secondNewTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent secondNewEventSecond = new NewEvent(UUID.randomUUID(), secondNewTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent secondNewEventThird = new NewEvent(UUID.randomUUID(), secondNewTracking.GetTrackingID(), evDate, null, null,  "122");

        newTracking.AddEvent(newEventFirst);
        newTracking.AddEvent(newEventSecond);
        newTracking.AddEvent(newEventThird);

        secondNewTracking.AddEvent(secondNewEventFirst);
        secondNewTracking.AddEvent(secondNewEventSecond);
        secondNewTracking.AddEvent(secondNewEventThird);

        List<NewTracking> newTrackingList = new ArrayList<>();
        newTrackingList.add(newTracking);
        newTrackingList.add(secondNewTracking);

        Assert.assertNotEquals(FunctionApplicability.allEventsCountFactApplicability(newTrackingList), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackingsButWithThreeEvents_AllEventsCountFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        NewTracking secondNewTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

        NewEvent secondNewEventFirst = new NewEvent(UUID.randomUUID(), secondNewTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent secondNewEventSecond = new NewEvent(UUID.randomUUID(), secondNewTracking.GetTrackingID(), evDate, null, null,  "122");

        newTracking.AddEvent(newEventFirst);

        secondNewTracking.AddEvent(secondNewEventFirst);
        secondNewTracking.AddEvent(secondNewEventSecond);

        List<NewTracking> newTrackingList = new ArrayList<>();
        newTrackingList.add(newTracking);
        newTrackingList.add(secondNewTracking);

        Assert.assertEquals(FunctionApplicability.allEventsCountFactApplicability(newTrackingList), null);
    }

    @Test
    public void WeHaveTrackingWithoutEvents_TrackingsEventCountFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required, "");

        Assert.assertEquals(FunctionApplicability.trackingEventsCountApplicability(newTracking), null);
    }

    @Test
    public void WeHaveTrackingWithEvents_TrackingsEventsFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

        newTracking.AddEvent(newEventFirst);

        Assert.assertNotEquals(FunctionApplicability.trackingEventsCountApplicability(newTracking), null);
    }

    @Test
    public void WeHaveTrackingWithoutRating_AvrgRatingFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

        newTracking.AddEvent(newEventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }

    @Test
    public void WeHaveTrackingWithRatingButWithoutEvents_AvrgRatingFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Required, TrackingCustomization.Required, "");

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }

    @Test
    public void WeHaveTrackingWithOptionalRatingButWithNullRatingInEvent_AvrgRatingFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Optional, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

        newTracking.AddEvent(newEventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }


    @Test
    public void WeHaveValidTracking_AvrgRatingFAShouldntReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Required, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, new Rating(4),  "122");
        NewEvent newEventSecond = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, new Rating(5),  "122");

        newTracking.AddEvent(newEventFirst);
        newTracking.AddEvent(newEventSecond);

        Assert.assertNotEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }


    @Test
    public void WeHaveTrackingWithoutScale_AvrgScaleFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

        newTracking.AddEvent(newEventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }

    @Test
    public void WeHaveTrackingWithScaleButWithoutEvents_AvrgScaleFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Required, TrackingCustomization.Required, TrackingCustomization.Required, "");

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }

    @Test
    public void WeHaveTrackingWithOptionalScaleButWithNullRatingInEvent_AvrgScaleFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Optional, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

        newTracking.AddEvent(newEventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }


    @Test
    public void WeHaveValidTracking_AvrgScaleFAShouldntReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Required, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, 10.0, new Rating(4),  "122");
        NewEvent newEventSecond = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, -15.4, new Rating(5),  "122");

        newTracking.AddEvent(newEventFirst);
        newTracking.AddEvent(newEventSecond);

        Assert.assertNotEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }



    @Test
    public void WeHaveTrackingWithoutScale_SumScaleFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

        newTracking.AddEvent(newEventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }

    @Test
    public void WeHaveTrackingWithScaleButWithoutEvents_SumScaleFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Required, TrackingCustomization.Required, TrackingCustomization.Required, "");

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }

    @Test
    public void WeHaveTrackingWithOptionalScaleButWithNullRatingInEvent_SumScaleFAShouldReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Optional, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");

        newTracking.AddEvent(newEventFirst);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }


    @Test
    public void WeHaveValidTracking_SumScaleFAShouldntReturnNull(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.Required, TrackingCustomization.Required, "");

        NewEvent newEventFirst = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, 10.0, new Rating(4),  "122");
        NewEvent newEventSecond = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, -15.4, new Rating(5),  "122");

        newTracking.AddEvent(newEventFirst);
        newTracking.AddEvent(newEventSecond);

        Assert.assertNotEquals(FunctionApplicability.avrgRatingApplicability(newTracking), null);
    }


}
