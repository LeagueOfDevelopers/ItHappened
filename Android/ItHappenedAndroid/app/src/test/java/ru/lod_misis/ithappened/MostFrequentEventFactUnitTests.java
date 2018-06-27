package ru.lod_misis.ithappened;

/**
 * Created by Пользователь on 15.03.2018.
 */

public class MostFrequentEventFactUnitTests {
/*
   /* @Test
    public void allTrackingsHaveTheSamePeriod_PeriodsShouldBeEquals(){
        TrackingV1 tracking = new TrackingV1("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        TrackingV1 secondTracking = new TrackingV1("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);

        EventV1 eventFirst = new EventV1(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        EventV1 eventSecond = new EventV1(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        EventV1 eventThird = new EventV1(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");

        EventV1 secondEventFirst = new EventV1(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        EventV1 secondEventSecond = new EventV1(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        EventV1 secondEventThird = new EventV1(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");

        tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);
        tracking.AddEvent(eventThird);

        secondTracking.AddEvent(secondEventFirst);
        secondTracking.AddEvent(secondEventSecond);
        secondTracking.AddEvent(secondEventThird);

        List<TrackingV1> trackingList = new ArrayList<>();
        trackingList.add(tracking);
        trackingList.add(secondTracking);

        MostFrequentEventFact fact = new MostFrequentEventFact(trackingList);

        List<FrequentEventsFactModel> firstModel = fact.getFrequency();

        Assert.assertTrue(firstModel.get(0).getPeriod().doubleValue()==firstModel.get(1).getPeriod().doubleValue());

    }


    @Test
    public void allTrackingsHaveDiffPeriod_PeriodsShouldntBeEquals(){
        TrackingV1 tracking = new TrackingV1("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        TrackingV1 secondTracking = new TrackingV1("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);

        EventV1 eventFirst = new EventV1(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Date date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        eventFirst.EditDate(date);
        EventV1 eventSecond = new EventV1(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        eventSecond.EditDate(date);
        EventV1 eventThird = new EventV1(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        eventThird.EditDate(date);

        EventV1 secondEventFirst = new EventV1(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        secondEventFirst.EditDate(date);
        EventV1 secondEventSecond = new EventV1(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        date = new Date();
        date.setDate(15);
        date.setMonth(03);
        date.setYear(2018-1900);
        secondEventSecond.EditDate(date);
        EventV1 secondEventThird = new EventV1(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
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

        List<TrackingV1> trackingList = new ArrayList<>();
        trackingList.add(tracking);
        trackingList.add(secondTracking);

        MostFrequentEventFact fact = new MostFrequentEventFact(trackingList);

        List<FrequentEventsFactModel> firstModel = fact.getFrequency();

        Assert.assertTrue(firstModel.get(0).getPeriod().compareTo(firstModel.get(1).getPeriod().doubleValue())==1);

    }


    @Test
    public void allTrackingsHaveDiffPeriod_FirstPeriodMinTheSecond(){
        TrackingV1 tracking = new TrackingV1("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);
        TrackingV1 secondTracking = new TrackingV1("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required);

        EventV1 eventFirst = new EventV1(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2018, 3, 15);
        Date date = Calendar.getInstance().getTime();
        eventFirst.EditDate(date);

        EventV1 eventSecond = new EventV1(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2018, 4, 16);
        date = Calendar.getInstance().getTime();
        eventSecond.EditDate(date);

        EventV1 eventThird = new EventV1(UUID.randomUUID(), tracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2018, 4, 17);
        date = Calendar.getInstance().getTime();
        eventThird.EditDate(date);

        EventV1 secondEventFirst = new EventV1(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2010, 10, 1);
        date = Calendar.getInstance().getTime();
        secondEventFirst.EditDate(date);

        EventV1 secondEventSecond = new EventV1(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2011, 10, 11);
        date = Calendar.getInstance().getTime();
        secondEventSecond.EditDate(date);

        EventV1 secondEventThird = new EventV1(UUID.randomUUID(), secondTracking.GetTrackingID(), null, null,  "122");
        Calendar.getInstance().set(2018, 10, 1);
        date = Calendar.getInstance().getTime();
        secondEventThird.EditDate(date);

        tracking.AddEvent(eventFirst);
        tracking.AddEvent(eventSecond);
        tracking.AddEvent(eventThird);

        secondTracking.AddEvent(secondEventFirst);
        secondTracking.AddEvent(secondEventSecond);
        secondTracking.AddEvent(secondEventThird);

        List<TrackingV1> trackingList = new ArrayList<>();
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
