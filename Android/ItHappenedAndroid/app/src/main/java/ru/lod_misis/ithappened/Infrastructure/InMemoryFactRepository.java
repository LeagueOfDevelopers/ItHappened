package ru.lod_misis.ithappened.Infrastructure;

import java.util.List;
import java.util.Observable;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.FunctionApplicability;

/**
 * Created by Ded on 15.03.2018.
 */

public class InMemoryFactRepository {

    List<Tracking> trackingCollection;

    public InMemoryFactRepository(){
        functionApplicability = new FunctionApplicability();
    }

    public rx.Observable calculateOneTrackingFacts(List<Tracking> trackingCollection)
    {
        for (Tracking tracking : trackingCollection) {
            functionApplicabilityCheck(tracking);
        }

        return rx.Observable.from(oneTrackingFactCollection);
    }

    public rx.Observable onChangeCalculateOneTrackingFacts(List<Tracking> trackingCollection, UUID trackingId)
    {
        Tracking changedTracking = null;

        for (Tracking tracking : trackingCollection) {
            if (tracking.GetTrackingID().equals(trackingId))
                changedTracking = tracking;
        }

        if (changedTracking == null)
            throw new IllegalArgumentException("tracking not exists");

        functionApplicabilityCheck(changedTracking);

        return rx.Observable.from(oneTrackingFactCollection);
    }

    public rx.Observable calculateAllTrackingsFacts(List<Tracking> trackingCollection) {
        Fact factToAdd;

        factToAdd = functionApplicability.allEventsCountFactApplicability(trackingCollection);
        if (factToAdd != null)
            allTrackingsFactCollection.add(factToAdd);

        factToAdd = functionApplicability.mostFrequentEventApplicability(trackingCollection);
        if (factToAdd != null)
            allTrackingsFactCollection.add(factToAdd);

        return rx.Observable.from(allTrackingsFactCollection);
    }

    private void functionApplicabilityCheck(Tracking tracking)
    {
        Fact factToAdd;

        factToAdd = functionApplicability.avrgRatingApplicability(tracking);
        if (factToAdd != null)
            oneTrackingFactCollection.add(factToAdd);

        factToAdd = functionApplicability.avrgScaleApplicability(tracking);
        if (factToAdd != null)
            oneTrackingFactCollection.add(factToAdd);

        factToAdd = functionApplicability.sumScaleApplicability(tracking);
        if (factToAdd != null)
            oneTrackingFactCollection.add(factToAdd);

        factToAdd = functionApplicability.trackingEventsCountApplicability(tracking);
        if (factToAdd != null)
            oneTrackingFactCollection.add(factToAdd);
    }


    List<Fact> oneTrackingFactCollection;
    List<Fact> allTrackingsFactCollection;
    FunctionApplicability functionApplicability;

}
