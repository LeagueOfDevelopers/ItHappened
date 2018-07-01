package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.LongestBreakFact;

public class LongestBreakFactUnitTest {

    @Test
    public void IsBreakSignificantTest_SignificanceTestWorksCorrect() {
        TrackingV1 tracking1 = new TrackingV1("tracking",
                UUID.randomUUID(),
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "scale", ""); // significant tracking
        Integer[] days = {1, 2, 2, 3, 3, 4, 5, 5, 20};
        for (int i = 0; i < 9; i++) {
            EventV1 event = new EventV1();
            event.SetEventId(UUID.randomUUID());
            event.SetEventDate(new Date(2000, 1, days[i]));
            tracking1.AddEvent(event);
        }
        LongestBreakFact fact = new LongestBreakFact(tracking1);
        fact.calculateData();
        Assert.assertTrue(fact.getLongestBreak() != null);
    }
}
