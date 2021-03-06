package ru.lod_misis.ithappened;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

import io.realm.RealmList;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.FunctionApplicability;

public class FrequencyTrendChangingFactUnitTest {

    @Test
    public void FrequencyTrendChangingTest_GotCorrectLastPeriodEventCountAndLastPerionDuration() {
        TrackingV1 tracking = GenerateTracking();
        Fact fact = FunctionApplicability.FrequencyTrendChangingFactApplicability(tracking);
        String descr = fact.textDescription();
        Assert.assertEquals(descr, "Событие <b>null</b> происходит <b>реже</b>: за последние <b>15 дней</b> - <b>10</b> раз.");
    }

    // STRESS TEST
//    @Test
//    public void FrequencyTrendChangingFactStressTest_FactDoesNotRaiseExceptions() {
//        Random gen = new Random();
//        int i = 1;
//        DateTime time = DateTime.now();
//        while (true) {
//            TrackingV1 tracking = GenerateRandomTracking(gen);
//            Fact fact = FunctionApplicability.FrequencyTrendChangingFactApplicability(tracking);
//            if (fact != null) {
//                String descr = fact.textDescription();
//                System.out.print(descr + "\n");
//            }
//            System.out.print("Test " + i + " finished\n");
//            System.out.print("data set length: " + tracking.getEventCollection().size() + "\n");
//            if (fact == null) {
//                System.out.print("Fact is null\n");
//            }
//            System.out.print("Time: " + new Interval(time.toInstant(), DateTime.now().toInstant()).toDuration().getMillis() + "\n");
//            time = DateTime.now();
//            i++;
//        }
//    }

    private TrackingV1 GenerateTracking() {
        int[] dates = {1, 3, 5, 6, 8, 9, 12, 13, 14, 15, 16, 17, 18, 18, 18};
        TrackingV1 tracking = new TrackingV1();
        tracking.setEventCollection(new RealmList<EventV1>());
        tracking.setTrackingId(UUID.randomUUID());
        tracking.setScaleCustomization(TrackingCustomization.None);
        tracking.setCommentCustomization(TrackingCustomization.None);
        tracking.setRatingCustomization(TrackingCustomization.None);
        tracking.setGeopositionCustomization(TrackingCustomization.None);
        for (int day: dates) {
            EventV1 event = new EventV1();
            event.setEventId(UUID.randomUUID());
            event.setEventDate(DateTime.now().minusDays(day).toDate());
            tracking.addEvent(event);
        }
        return tracking;
    }

    private TrackingV1 GenerateRandomTracking(Random r) {
        int eventCount = r.nextInt(1000);
        TrackingV1 tracking = new TrackingV1();
        tracking.setEventCollection(new RealmList<EventV1>());
        tracking.setTrackingId(UUID.randomUUID());
        tracking.setScaleCustomization(TrackingCustomization.None);
        tracking.setCommentCustomization(TrackingCustomization.None);
        tracking.setRatingCustomization(TrackingCustomization.None);
        tracking.setGeopositionCustomization(TrackingCustomization.None);
        for (int i = 0; i < eventCount; i++) {
            EventV1 e = new EventV1();
            e.setEventId(UUID.randomUUID());
            e.setEventDate(new DateTime(2017 + r.nextInt(1000) / 365, r.nextInt(12) + 1, r.nextInt(27) + 1, 0, 0).toDate());
            tracking.addEvent(e);
        }
        return tracking;
    }
}
