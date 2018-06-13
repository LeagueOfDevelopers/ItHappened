package ru.lod_misis.ithappened;

/**
 * Created by Пользователь on 15.03.2018.
 */

public class MostFrequentEventFactUnitTests {
/*
   /* @Test
    public void allTrackingsHaveTheSamePeriod_PeriodsShouldBeEquals(){
        NewTracking tracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        NewTracking secondTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);

        NewEvent eventFirst = new NewEvent(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        NewEvent eventSecond = new NewEvent(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        NewEvent eventThird = new NewEvent(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");

        NewEvent secondEventFirst = new NewEvent(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        NewEvent secondEventSecond = new NewEvent(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        NewEvent secondEventThird = new NewEvent(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");

        tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);
        tracking.AddEvent(eventThird);

        secondTracking.AddEvent(secondEventFirst);
        secondTracking.AddEvent(secondEventSecond);
        secondTracking.AddEvent(secondEventThird);

        List<NewTracking> trackingList = new ArrayList<>();
        trackingList.add(tracking);
        trackingList.add(secondTracking);

        MostFrequentEventFact fact = new MostFrequentEventFact(trackingList);

        List<FrequentEventsFactModel> firstModel = fact.getFrequency();

        Assert.assertTrue(firstModel.get(0).getPeriod().doubleValue()==firstModel.get(1).getPeriod().doubleValue());

    }


    @Test
    public void allTrackingsHaveDiffPeriod_PeriodsShouldntBeEquals(){
        NewTracking tracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        NewTracking secondTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);

        NewEvent eventFirst = new NewEvent(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Date date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        eventFirst.EditDate(date);
        NewEvent eventSecond = new NewEvent(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        eventSecond.EditDate(date);
        NewEvent eventThird = new NewEvent(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        eventThird.EditDate(date);

        NewEvent secondEventFirst = new NewEvent(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        secondEventFirst.EditDate(date);
        NewEvent secondEventSecond = new NewEvent(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        secondEventSecond.EditDate(date);
        NewEvent secondEventThird = new NewEvent(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        secondEventThird.EditDate(date);

        tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);
        tracking.AddEvent(eventThird);

        secondTracking.AddEvent(secondEventFirst);
        secondTracking.AddEvent(secondEventSecond);
        secondTracking.AddEvent(secondEventThird);

        List<NewTracking> trackingList = new ArrayList<>();
        trackingList.add(tracking);
        trackingList.add(secondTracking);

        MostFrequentEventFact fact = new MostFrequentEventFact(trackingList);

        List<FrequentEventsFactModel> firstModel = fact.getFrequency();

        Assert.assertTrue(firstModel.get(0).getPeriod().compareTo(firstModel.get(1).getPeriod().doubleValue())==1);

    }


    @Test
    public void allTrackingsHaveDiffPeriod_FirstPeriodMinTheSecond(){
        NewTracking tracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        NewTracking secondTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);

        NewEvent eventFirst = new NewEvent(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2018, 3, 15);
        Date date = Calendar.getInstance().getTime();
        eventFirst.EditDate(date);

        NewEvent eventSecond = new NewEvent(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2018, 4, 16);
        date = Calendar.getInstance().getTime();
        eventSecond.EditDate(date);

        NewEvent eventThird = new NewEvent(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2018, 4, 17);
        date = Calendar.getInstance().getTime();
        eventThird.EditDate(date);

        NewEvent secondEventFirst = new NewEvent(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2010, 10, 1);
        date = Calendar.getInstance().getTime();
        secondEventFirst.EditDate(date);

        NewEvent secondEventSecond = new NewEvent(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2011, 10, 11);
        date = Calendar.getInstance().getTime();
        secondEventSecond.EditDate(date);

        NewEvent secondEventThird = new NewEvent(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2018, 10, 1);
        date = Calendar.getInstance().getTime();
        secondEventThird.EditDate(date);

        tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);
        tracking.AddEvent(eventThird);

        secondTracking.AddEvent(secondEventFirst);
        secondTracking.AddEvent(secondEventSecond);
        secondTracking.AddEvent(secondEventThird);

        List<NewTracking> trackingList = new ArrayList<>();
        trackingList.add(tracking);
        trackingList.add(secondTracking);

        MostFrequentEventFact fact = new MostFrequentEventFact(trackingList);

        List<FrequentEventsFactModel> firstModel = fact.getFrequency();
        double firstPeriod = firstModel.get(0).getPeriod().doubleValue();
        double firstSecond = firstModel.get(1).getPeriod().doubleValue();

        Assert.assertTrue(firstModel.get(0).getPeriod().doubleValue()>firstModel.get(1).getPeriod().doubleValue());

    }
*/
}
