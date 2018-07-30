package ru.lod_misis.ithappened;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

import io.realm.RealmList;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.FunctionApplicability;

public class RatingOrScaleTrendChangingFact {

    @Test
    public void RatingIsChangingFactStressTest_FactDoesNotRaiseExceptions() {
        Random gen = new Random();
        int i = 1;
        DateTime time = DateTime.now();
        while (true) {
            TrackingV1 tracking = GenerateRandomTracking(gen);
            Fact fact = FunctionApplicability.RatingTrendChangingFactApplicability(tracking);
            if (fact != null) {
                String descr = fact.textDescription();
            }
            System.out.print("Test " + i + " finished\n");
            System.out.print("Data set length: " + tracking.getEventV1Collection().size() + "\n");
            if (fact == null) {
                System.out.print("Fact is null\n");
            }
            System.out.print("Time: " + new Interval(time.toInstant(), DateTime.now().toInstant()).toDuration().getMillis() + "\n");
            time = DateTime.now();
            i++;
        }
    }

    private TrackingV1 GenerateRandomTracking(Random r) {
        int eventCount = r.nextInt(1000);
        TrackingV1 tracking = new TrackingV1();
        tracking.setEventV1Collection(new RealmList<EventV1>());
        tracking.SetTrackingID(UUID.randomUUID());
        tracking.SetScaleCustomization(TrackingCustomization.None);
        tracking.SetCommentCustomization(TrackingCustomization.None);
        tracking.SetRatingCustomization(TrackingCustomization.Optional);
        for (int i = 0; i < eventCount; i++) {
            EventV1 e = new EventV1();
            e.setEventId(UUID.randomUUID().toString());
            if (r.nextInt() % 10 == 1) {
                Rating rating = new Rating();
                rating.setRating(r.nextInt(10));
                e.setRating(rating);
            }
            else {
                e.setRating(null);
            }
            e.setEventDate(new DateTime(2017 + r.nextInt(1000) / 365, r.nextInt(12) + 1, r.nextInt(27) + 1, 0, 0).toDate());
            tracking.AddEvent(e);
        }
        return tracking;
    }
}
