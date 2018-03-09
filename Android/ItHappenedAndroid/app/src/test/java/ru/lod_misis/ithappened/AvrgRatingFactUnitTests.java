package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.AvrgRatingFact;

public class AvrgRatingFactUnitTests {

    @Test
    public void TrackingDontHaveRatingCustomization_ApplicabilityFunctionShouldReturnFalse(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Required,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "");

        for(int i=0;i<5;i++){
            Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 12.0, null, null);
            testTracking.AddEvent(newEvent);
        }

        Assert.assertFalse(new AvrgRatingFact(testTracking).applicabilityFunction());
    }

    @Test
    public void TrackingHasRatingCustomizationButDosentHaveAnyEventsWithScale_ApplicabilityFunctionShouldReturnFalse(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                "");

        for(int i=0;i<5;i++){
            Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, null, null);
            testTracking.AddEvent(newEvent);
        }

        Assert.assertFalse(new AvrgRatingFact(testTracking).applicabilityFunction());
    }

    @Test
    public void TrackingHaveOnlyOneEvent_ApplicabilityFunctionShouldReturnFalse(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                "");


        Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 11.0, new Rating(2), null);
        testTracking.AddEvent(newEvent);


        Assert.assertFalse(new AvrgRatingFact(testTracking).applicabilityFunction());
    }

    @Test
    public void TrackingHaveScaleCustomization_ApplicabilityFunctionShouldReturnTrue(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Required,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                "");

        for(int i=0;i<5;i++){
            Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 11.0, new Rating(2), null);
            testTracking.AddEvent(newEvent);
        }

        Assert.assertTrue(new AvrgRatingFact(testTracking).applicabilityFunction());
    }

    @Test
    public void TrackingHaveOptionalScaleCustomization_ApplicabilityFunctionShouldReturnTrue(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Required,
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                "");

        Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 1.0, null, null);
        testTracking.AddEvent(newEvent);

        Event secondEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 12.0, new Rating(2), null);
        testTracking.AddEvent(secondEvent);

        Event thirdEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 14.1, new Rating(2), null);
        testTracking.AddEvent(thirdEvent);

        Assert.assertTrue(new AvrgRatingFact(testTracking).applicabilityFunction());
    }

    @Test
    public void TrackingHaveOptionalScaleCustomizationAndTwoEventsOneOfWichHaveNullScal_ApplicabilityFunctionShouldReturnFalse(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                "");

        Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, null, null);
        testTracking.AddEvent(newEvent);

        Event secondEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 12.0, new Rating(2), null);
        testTracking.AddEvent(secondEvent);

        Assert.assertFalse(new AvrgRatingFact(testTracking).applicabilityFunction());
    }

    @Test
    public void TrackingHaveRequariedScaleCustomization_GetSumValueShouldBeCorrectWorking(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                "");

        Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, new Rating(5), null);
        testTracking.AddEvent(newEvent);

        Event secondEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 12.0, new Rating(5), null);
        testTracking.AddEvent(secondEvent);

        Event thirdEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 12.0, new Rating(5), null);
        testTracking.AddEvent(thirdEvent);

        Double sumValue = new AvrgRatingFact(testTracking).getAvrgValue();

        Assert.assertTrue(sumValue.doubleValue() == 5.0);
    }

    @Test
    public void TrackingHaveRequariedScaleCustomization_GetSumValueShouldBeReturnNull(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Optional,
                TrackingCustomization.None,
                "");

        Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, null, null);
        testTracking.AddEvent(newEvent);

        Event secondEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, null, null);
        testTracking.AddEvent(secondEvent);

        Event thirdEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 14.1, new Rating(3), null);
        testTracking.AddEvent(thirdEvent);

        Double avrgValue = new AvrgRatingFact(testTracking).getAvrgValue();

        Assert.assertTrue(avrgValue == null);
    }

}
