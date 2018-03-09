package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.AvrgScaleFact;

public class AvrgScaleUnitTest {

    @Test
    public void TrackingDontHaveScaleCustomization_ApplicabilityFunctionShouldReturnFalse(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.None,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                "");

        for(int i=0;i<5;i++){
            Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, new Rating(2), null);
            testTracking.AddEvent(newEvent);
        }

        Assert.assertFalse(new AvrgScaleFact(testTracking).applicabilityFunction());
    }

    @Test
    public void TrackingHasScaleCustomizationButDosentHaveAnyEventsWithScale_ApplicabilityFunctionShouldReturnFalse(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                "");

        for(int i=0;i<5;i++){
            Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, new Rating(2), null);
            testTracking.AddEvent(newEvent);
        }

        Assert.assertFalse(new AvrgScaleFact(testTracking).applicabilityFunction());
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


        Assert.assertFalse(new AvrgScaleFact(testTracking).applicabilityFunction());
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

        Assert.assertTrue(new AvrgScaleFact(testTracking).applicabilityFunction());
    }

    @Test
    public void TrackingHaveOptionalScaleCustomization_ApplicabilityFunctionShouldReturnTrue(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                "");

        Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, new Rating(2), null);
        testTracking.AddEvent(newEvent);

        Event secondEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 12.0, new Rating(2), null);
        testTracking.AddEvent(secondEvent);

        Event thirdEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 14.1, new Rating(2), null);
        testTracking.AddEvent(thirdEvent);

        Assert.assertTrue(new AvrgScaleFact(testTracking).applicabilityFunction());
    }

    @Test
    public void TrackingHaveOptionalScaleCustomizationAndTwoEventsOneOfWichHaveNullScal_ApplicabilityFunctionShouldReturnFalse(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                "");

        Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, new Rating(2), null);
        testTracking.AddEvent(newEvent);

        Event secondEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 12.0, new Rating(2), null);
        testTracking.AddEvent(secondEvent);

        Assert.assertFalse(new AvrgScaleFact(testTracking).applicabilityFunction());
    }

    @Test
    public void TrackingHaveRequariedScaleCustomization_GetSumValueShouldBeCorrectWorking(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                "");

        Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, new Rating(2), null);
        testTracking.AddEvent(newEvent);

        Event secondEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 12.0, new Rating(2), null);
        testTracking.AddEvent(secondEvent);

        Event thirdEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 12.0, new Rating(2), null);
        testTracking.AddEvent(thirdEvent);

        Double sumValue = new AvrgScaleFact(testTracking).getAvrgValue().doubleValue();

        Assert.assertTrue(sumValue.doubleValue() == 12.0);
    }

    @Test
    public void TrackingHaveRequariedScaleCustomization_GetSumValueShouldBeReturnNull(){

        Tracking testTracking = new Tracking("Тест", UUID.randomUUID(),
                TrackingCustomization.Optional,
                TrackingCustomization.Required,
                TrackingCustomization.None,
                "");

        Event newEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, new Rating(2), null);
        testTracking.AddEvent(newEvent);

        Event secondEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), null, new Rating(2), null);
        testTracking.AddEvent(secondEvent);

        Event thirdEvent = new Event(UUID.randomUUID(), testTracking.GetTrackingID(), 14.1, new Rating(2), null);
        testTracking.AddEvent(thirdEvent);

        Double avrgValue = new AvrgScaleFact(testTracking).getAvrgValue();

        Assert.assertTrue(avrgValue == null);
    }


}
