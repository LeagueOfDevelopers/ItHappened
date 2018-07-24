package ru.lod_misis.ithappened;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.UUID;

import io.realm.RealmList;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;

public class FrequenceRaisingUpFactUnitTest {

    @Test
    public void FrequenceRaisingUpTest_GotCorrectLastPeriodEventCountAndLastPerionDuration() {

    }

    private TrackingV1 GenerateTracking() {
        int[] dates = {1, 3, 5, 6, 8, 9, 12, 13, 14, 15, 16, 17, 18, 18, 18};
        TrackingV1 tracking = new TrackingV1();
        tracking.setEventV1Collection(new RealmList<EventV1>());
        tracking.SetTrackingID(UUID.randomUUID());
        for (int day: dates) {
            EventV1 event = new EventV1();
            event.setEventId(UUID.randomUUID().toString());
            event.setEventDate(new DateTime(2000, ));
        }
    }
}
