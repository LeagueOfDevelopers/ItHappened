package ru.lod_misis.ithappened;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import io.realm.RealmList;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.FrequencyTrendChangingFact;

public class FrequencyTrendChangingFactUnitTest {

    @Test
    public void FrequencyTrendChangingTest_GotCorrectLastPeriodEventCountAndLastPerionDuration() {
        TrackingV1 tracking = GenerateTracking();
        Fact fact = new FrequencyTrendChangingFact(tracking);
        fact.calculateData();
        String descr = fact.textDescription();
        Assert.assertEquals(descr, "Событие <b>null</b> происходит чаще: за последние <b>6</b> дней - <b>9</b> раз.");
    }

    private TrackingV1 GenerateTracking() {
        int[] dates = {1, 3, 5, 6, 8, 9, 12, 13, 14, 15, 16, 17, 18, 18, 18};
        TrackingV1 tracking = new TrackingV1();
        tracking.setEventV1Collection(new RealmList<EventV1>());
        tracking.SetTrackingID(UUID.randomUUID());
        tracking.SetScaleCustomization(TrackingCustomization.None);
        tracking.SetCommentCustomization(TrackingCustomization.None);
        tracking.SetRatingCustomization(TrackingCustomization.None);
        for (int day: dates) {
            EventV1 event = new EventV1();
            event.setEventId(UUID.randomUUID().toString());
            event.setEventDate(new DateTime(2000, 1, day, 0, 0).toDate());
            tracking.AddEvent(event);
        }
        return tracking;
    }
}
