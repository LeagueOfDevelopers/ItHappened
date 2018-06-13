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
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.AllEventsCountFact;

/**
 * Created by Пользователь on 15.03.2018.
 */

public class AllEventsCountFactUnitTests {

    Date evDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

    @Test
    public void HaveTrackingCollectionWithTwoTrackings_GetEventCountShouldReturnTwo(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        NewTracking secondNewTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        NewEvent newEvent = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent secondNewEvent = new NewEvent(UUID.randomUUID(), secondNewTracking.GetTrackingID(), evDate, null, null,  "122");
        newTracking.AddEvent(newEvent);
        secondNewTracking.AddEvent(secondNewEvent);

        List<NewTracking> newTrackingCollection = new ArrayList<>();
        newTrackingCollection.add(newTracking);
        newTrackingCollection.add(secondNewTracking);

        AllEventsCountFact fact = new AllEventsCountFact(newTrackingCollection);
        fact.calculateData();
        Assert.assertEquals(fact.getEventCount(), 2);
    }

    @Test
    public void HaveTrackingCollectionWithTwoTrackings_GetTextDescriptionShouldReturnValidData(){
        NewTracking newTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        NewTracking secondNewTracking = new NewTracking("", UUID.randomUUID(), TrackingCustomization.Optional, TrackingCustomization.None, TrackingCustomization.Required, "");
        NewEvent newEvent = new NewEvent(UUID.randomUUID(), newTracking.GetTrackingID(), evDate, null, null,  "122");
        NewEvent secondNewEvent = new NewEvent(UUID.randomUUID(), secondNewTracking.GetTrackingID(), evDate, null, null,  "122");
        newTracking.AddEvent(newEvent);
        secondNewTracking.AddEvent(secondNewEvent);

        List<NewTracking> newTrackingCollection = new ArrayList<>();
        newTrackingCollection.add(newTracking);
        newTrackingCollection.add(secondNewTracking);

        AllEventsCountFact fact = new AllEventsCountFact(newTrackingCollection);
        fact.calculateData();
        Assert.assertEquals(fact.textDescription(), "У вас произошло уже <b>2</b> событий!");
    }



}
