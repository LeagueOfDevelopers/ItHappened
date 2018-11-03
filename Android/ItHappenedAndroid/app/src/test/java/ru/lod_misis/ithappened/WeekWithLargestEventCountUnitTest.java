package ru.lod_misis.ithappened;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Models.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;

public class WeekWithLargestEventCountUnitTest {

    /*@Test
    public void WeekWithLargestEventCountTest_GotCorrectCountAndIdsOfEvents() {
        TrackingV1 t = GenerateTracking();
        List<TrackingV1> trackings = new ArrayList<>();
        trackings.add(t);
        Fact fact = new WeekWithLargestEventCountFact(trackings);
        fact.calculateData();
        List<UUID> uuids = fact.getIllustration().getEventHistoryRef();

        Assert.assertTrue(uuids.size() == 9);
        int i = 0;
        for (UUID uuid : uuids) {
            Assert.assertEquals(uuid.toString(), t.getEventV1Collection().get(i).GetEventId().toString());
            i++;
        }
    }*/

    private TrackingV1 GenerateTracking() {
        int[] days = {2, 3, 4, 5, 5, 5, 6, 7, 8, 9, 12, 15, 17};
        TrackingV1 t = new TrackingV1();
        t.SetTrackingID(UUID.randomUUID());
        t.SetScaleCustomization(TrackingCustomization.None);
        t.SetRatingCustomization(TrackingCustomization.None);
        t.SetCommentCustomization(TrackingCustomization.None);
        t.setEventV1Collection(new RealmList<EventV1>());
        for (int day : days) {
            UUID id = UUID.randomUUID();
            Date date = new DateTime(2018, 7, day, 0, 0).toDate();
            EventV1 e = new EventV1();
            e.setEventDate(date);
            e.setEventId(id.toString());
            t.AddEvent(e);
        }
        return t;
    }
}
