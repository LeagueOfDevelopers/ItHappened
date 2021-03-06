package ru.lod_misis.ithappened;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.LongestBreakFact;

public class LongestBreakFactUnitTest {

    @Test
    public void IsBreakSignificantTest_SignificanceTestWorksCorrect() {
        TrackingV1 tracking1 = new TrackingV1("tracking",
                UUID.randomUUID(),
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                TrackingCustomization.None,
                "scale", ""); // significant tracking
        Integer[] days = {1, 2, 2, 3, 3, 4, 5, 5, 20};
        for (int i = 0; i < 9; i++) {
            EventV1 event = new EventV1();
            event.setEventId(UUID.randomUUID());
            event.setEventDate(new DateTime(2000, 1, days[i], 0, 0).toDate());
            tracking1.addEvent(event);
        }
        LongestBreakFact fact = new LongestBreakFact(tracking1);
        fact.calculateData();
        Assert.assertTrue(fact.getLongestBreak() != null);
        long duration = fact.getLongestBreak().getDuration().getStandardDays();
        Assert.assertEquals(duration, 15);
        Assert.assertEquals(fact.getLongestBreak().getFirstEventDate().getTime(),
                new DateTime(2000, 1, 5, 0, 0).toDate().getTime());
        Assert.assertEquals(fact.getLongestBreak().getSecondEventDate().getTime(),
                new DateTime(2000, 1, 20, 0, 0).toDate().getTime());
    }
}
