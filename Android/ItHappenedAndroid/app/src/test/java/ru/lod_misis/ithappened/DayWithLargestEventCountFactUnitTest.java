package ru.lod_misis.ithappened;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.DayWithLargestEventCountFact;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;

public class DayWithLargestEventCountFactUnitTest {

    /*@Test
    public void DayWIthLargestEventCountTest_GotCorrectCountAndListOfIds() {
        TrackingV1 t = GenerateTracking();
        List<TrackingV1> trackings = new ArrayList<>();
        trackings.add(t);
        Fact fact = new DayWithLargestEventCountFact(trackings);
        fact.calculateData();
        List<UUID> uuids = fact.getIllustration().getEventHistoryRef();

        Assert.assertTrue(uuids.size() == 3);
        int i = 4;
        for (UUID uuid: uuids) {
            Assert.assertTrue(uuid.toString().equals(t.getEventV1Collection().get(i).getEventId().toString()));
            i++;
        }
    }*/

    private TrackingV1 GenerateTracking() {
        int[] days = {1, 2, 3, 4, 5, 5, 5, 6, 7, 8, 9};
        TrackingV1 t = new TrackingV1();
        t.SetTrackingID(UUID.randomUUID());
        t.SetScaleCustomization(TrackingCustomization.None);
        t.SetRatingCustomization(TrackingCustomization.None);
        t.SetCommentCustomization(TrackingCustomization.None);
        t.setEventV1Collection(new RealmList<EventV1>());
        for (int day : days) {
            UUID id = UUID.randomUUID();
            Date date = new DateTime(2000, 1, day, 0, 0).toDate();
            EventV1 e = new EventV1();
            e.setEventDate(date);
            e.setEventId(id.toString());
            t.AddEvent(e);
        }
        return t;
    }
}
