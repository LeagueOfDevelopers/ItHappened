package ru.lod_misis.ithappened;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.models.TrackingV1;

public class DayWithLargestEventCountFactUnitTest {

    /*@Test
    public void DayWIthLargestEventCountTest_GotCorrectCountAndListOfIds() {
        TrackingV1 t = GenerateTracking();
        List<TrackingV1> trackings = new ArrayList<>();
        trackings.add(t);
        Fact fact = new DayWithLargestEventCountFact(trackings);
        fact.calculateData();
        List<Date> borders = fact.getIllustration().getEventHistoryRef();

        Assert.assertTrue(borders.size() == 2);
        Assert.assertTrue(borders.get(0).equals(1));
    }*/

    private TrackingV1 GenerateTracking() {
        int[] days = {1, 2, 3, 4, 5, 5, 5, 6, 7, 8, 9, 1, 1, 1, 1, 1, 1, 1};
        TrackingV1 t = new TrackingV1();
        t.setTrackingId(UUID.randomUUID());
        t.setScaleCustomization(TrackingCustomization.None);
        t.setRatingCustomization(TrackingCustomization.None);
        t.setCommentCustomization(TrackingCustomization.None);
        t.setEventCollection(new RealmList<EventV1>());
        for (int day = 0; day < 11; day++) {
            UUID id = UUID.randomUUID();
            Date date = new DateTime(2000, 1, days[day], 0, 0).toDate();
            EventV1 e = new EventV1();
            e.setEventDate(date);
            e.setEventId(id);
            t.addEvent(e);
        }
        for (int day = 11; day < days.length; day++) {
            UUID id = UUID.randomUUID();
            Date date = new DateTime(2019, 1, days[day], 0, 0).toDate();
            EventV1 e = new EventV1();
            e.setEventDate(date);
            e.setEventId(id);
            t.addEvent(e);
        }
        return t;
    }
}
