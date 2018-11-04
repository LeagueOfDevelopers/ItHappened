package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Models.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.AllTrackingsStatistics.AllEventsCountFact;

/**
 * Created by Пользователь on 15.03.2018.
 */

public class AllEventsCountFactUnitTests {

    Date evDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

    @Test
    public void HaveTrackingCollectionWithTwoTrackings_GetEventCountShouldReturnTwo(){
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
        EventV1 eventV1 = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 secondEventV1 = new EventV1(UUID.randomUUID(), secondTrackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        trackingV1.addEvent(eventV1);
        secondTrackingV1.addEvent(secondEventV1);

        List<TrackingV1> trackingV1Collection = new ArrayList<>();
        trackingV1Collection.add(trackingV1);
        trackingV1Collection.add(secondTrackingV1);

        AllEventsCountFact fact = new AllEventsCountFact(trackingV1Collection);
        fact.calculateData();
        Assert.assertEquals(fact.getEventCount(), 2);
    }

    @Test
    public void HaveTrackingCollectionWithTwoTrackings_GetTextDescriptionShouldReturnValidData(){
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
        EventV1 eventV1 = new EventV1(UUID.randomUUID(), trackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        EventV1 secondEventV1 = new EventV1(UUID.randomUUID(), secondTrackingV1.getTrackingId(),
                evDate, null, null, "122", null, null, "");
        trackingV1.addEvent(eventV1);
        secondTrackingV1.addEvent(secondEventV1);

        List<TrackingV1> trackingV1Collection = new ArrayList<>();
        trackingV1Collection.add(trackingV1);
        trackingV1Collection.add(secondTrackingV1);

        AllEventsCountFact fact = new AllEventsCountFact(trackingV1Collection);
        fact.calculateData();
        Assert.assertEquals(fact.textDescription(), "У вас произошло уже <b>2</b> события!");
    }
}
