package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.models.Rating;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.statistics.facts.FunctionApplicability;

public class FunctionApplicabilityUnitTests {

    Date evDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

    @Test
    public void WeHaveTrackingCollection_MostFrequentEventFAShouldReurnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 eventV1Second = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 eventV1Third = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

               trackingV1.addEvent(eventV1First);
        trackingV1.addEvent(eventV1Second);
        trackingV1.addEvent(eventV1Third);

        List<TrackingV1> trackingV1List = new ArrayList<>();
        trackingV1List.add(trackingV1);

        Assert.assertEquals(FunctionApplicability.mostFrequentEventApplicability(trackingV1List), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackings_MostFrequentEventFAShouldReturnValidFact(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");
        TrackingV1 secondTrackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 eventV1Second = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 eventV1Third = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        EventV1 secondEventV1First = new EventV1(UUID.randomUUID(), secondTrackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 secondEventV1Second = new EventV1(UUID.randomUUID(), secondTrackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 secondEventV1Third = new EventV1(UUID.randomUUID(), secondTrackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        trackingV1.addEvent(eventV1First);
        trackingV1.addEvent(eventV1Second);
        trackingV1.addEvent(eventV1Third);

        secondTrackingV1.addEvent(secondEventV1First);
        secondTrackingV1.addEvent(secondEventV1Second);
        secondTrackingV1.addEvent(secondEventV1Third);

        List<TrackingV1> trackingV1List = new ArrayList<>();
        trackingV1List.add(trackingV1);
        trackingV1List.add(secondTrackingV1);

        Assert.assertNotEquals(FunctionApplicability.mostFrequentEventApplicability(trackingV1List), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackingsButWithoutEvents_MostFrequentEventFAShouldReurnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");
        TrackingV1 secondTrackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        List<TrackingV1> trackingV1List = new ArrayList<>();
        trackingV1List.add(trackingV1);
        trackingV1List.add(secondTrackingV1);

        Assert.assertEquals(FunctionApplicability.mostFrequentEventApplicability(trackingV1List), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackings_AllEventsCountFAShouldReturnValidFact(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");
        TrackingV1 secondTrackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 eventV1Second = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 eventV1Third = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        EventV1 secondEventV1First = new EventV1(UUID.randomUUID(), secondTrackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 secondEventV1Second = new EventV1(UUID.randomUUID(), secondTrackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 secondEventV1Third = new EventV1(UUID.randomUUID(), secondTrackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        trackingV1.addEvent(eventV1First);
        trackingV1.addEvent(eventV1Second);
        trackingV1.addEvent(eventV1Third);

        secondTrackingV1.addEvent(secondEventV1First);
        secondTrackingV1.addEvent(secondEventV1Second);
        secondTrackingV1.addEvent(secondEventV1Third);

        List<TrackingV1> trackingV1List = new ArrayList<>();
        trackingV1List.add(trackingV1);
        trackingV1List.add(secondTrackingV1);

        Assert.assertNotEquals(FunctionApplicability.allEventsCountFactApplicability(trackingV1List), null);
    }

    @Test
    public void WeHaveTrackingCollectionWithTwoTrackingsButWithThreeEvents_AllEventsCountFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");
        TrackingV1 secondTrackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        EventV1 secondEventV1First = new EventV1(UUID.randomUUID(), secondTrackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 secondEventV1Second = new EventV1(UUID.randomUUID(), secondTrackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        trackingV1.addEvent(eventV1First);

        secondTrackingV1.addEvent(secondEventV1First);
        secondTrackingV1.addEvent(secondEventV1Second);

        List<TrackingV1> trackingV1List = new ArrayList<>();
        trackingV1List.add(trackingV1);
        trackingV1List.add(secondTrackingV1);

        Assert.assertEquals(FunctionApplicability.allEventsCountFactApplicability(trackingV1List), null);
    }

    @Test
    public void WeHaveTrackingWithoutEvents_TrackingsEventCountFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1("",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        Assert.assertEquals(FunctionApplicability.trackingEventsCountApplicability(trackingV1), null);
    }

    @Test
    public void WeHaveTrackingWithEvents_TrackingsEventsFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        trackingV1.addEvent(eventV1First);

        Assert.assertNotEquals(FunctionApplicability.trackingEventsCountApplicability(trackingV1), null);
    }

    @Test
    public void WeHaveTrackingWithoutRating_AvrgRatingFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        trackingV1.addEvent(eventV1First);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }

    @Test
    public void WeHaveTrackingWithRatingButWithoutEvents_AvrgRatingFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }

    @Test
    public void WeHaveTrackingWithOptionalRatingButWithNullRatingInEvent_AvrgRatingFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        trackingV1.addEvent(eventV1First);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }


    @Test
    public void WeHaveValidTracking_AvrgRatingFAShouldntReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, new Rating(4),
                "122", null, null, "");
        EventV1 eventV1Second = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, new Rating(5),
                "122", null, null, "");

        trackingV1.addEvent(eventV1First);
        trackingV1.addEvent(eventV1Second);

        Assert.assertNotEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }


    @Test
    public void WeHaveTrackingWithoutScale_AvrgScaleFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        trackingV1.addEvent(eventV1First);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }

    @Test
    public void WeHaveTrackingWithScaleButWithoutEvents_AvrgScaleFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }

    @Test
    public void WeHaveTrackingWithOptionalScaleButWithNullRatingInEvent_AvrgScaleFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        trackingV1.addEvent(eventV1First);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }


    @Test
    public void WeHaveValidTracking_AvrgScaleFAShouldntReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, 10.0, new Rating(4),
                "122", null, null, "");
        EventV1 eventV1Second = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, -15.4, new Rating(5),
                "122", null, null, "");

        trackingV1.addEvent(eventV1First);
        trackingV1.addEvent(eventV1Second);

        Assert.assertNotEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }



    @Test
    public void WeHaveTrackingWithoutScale_SumScaleFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null,
                "122", null, null, "");

        trackingV1.addEvent(eventV1First);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }

    @Test
    public void WeHaveTrackingWithScaleButWithoutEvents_SumScaleFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }

    @Test
    public void WeHaveTrackingWithOptionalScaleButWithNullRatingInEvent_SumScaleFAShouldReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");

        trackingV1.addEvent(eventV1First);

        Assert.assertEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }


    @Test
    public void WeHaveValidTracking_SumScaleFAShouldntReturnNull(){
        TrackingV1 trackingV1 = new TrackingV1(
                "",
                UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "",
                "");

        EventV1 eventV1First = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, 10.0, new Rating(4),
                "122", null, null, "");
        EventV1 eventV1Second = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, -15.4, new Rating(5),
                "122", null, null, "");

        trackingV1.addEvent(eventV1First);
        trackingV1.addEvent(eventV1Second);

        Assert.assertNotEquals(FunctionApplicability.avrgRatingApplicability(trackingV1), null);
    }
}
