package ru.lod_misis.ithappened.Infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.FunctionApplicability;

public class InMemoryFactRepository {

    List<Tracking> trackingCollection;

    public InMemoryFactRepository(){
        functionApplicability = new FunctionApplicability();
    }

    public rx.Observable calculateOneTrackingFacts(List<Tracking> trackingCollection)
    {
        oneTrackingFactCollection = new ArrayList<>();
        for (Tracking tracking : trackingCollection) {
            functionApplicabilityCheck(tracking);
        }

        return rx.Observable.from(oneTrackingFactCollection);
    }

    public rx.Observable<Fact> onChangeCalculateOneTrackingFacts(List<Tracking> trackingCollection, UUID trackingId)
    {
        Tracking changedTracking = null;
        List<Fact> factCollectionCheck = new ArrayList<>();

        for (Fact fact: oneTrackingFactCollection) {
            if (fact.getTrackingId().equals(trackingId))
                factCollectionCheck.add(fact);
        }

        oneTrackingFactCollection.removeAll(factCollectionCheck);

        for (Tracking tracking : trackingCollection) {
            if (tracking.GetTrackingID().equals(trackingId))
                changedTracking = tracking;
        }

        if (changedTracking == null)
            throw new IllegalArgumentException("tracking not exists");

        functionApplicabilityCheck(changedTracking);


        //sortOneTrackingFacts();

        return rx.Observable.from(oneTrackingFactCollection);
    }

    public rx.Observable<Fact> calculateAllTrackingsFacts(List<Tracking> trackingCollection) {
        Fact factToAdd;
        allTrackingsFactCollection = new ArrayList<>();

        factToAdd = FunctionApplicability.allEventsCountFactApplicability(trackingCollection);
        if (factToAdd != null) {
            factToAdd.calculateData();
            allTrackingsFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.mostFrequentEventApplicability(trackingCollection);
        if (factToAdd != null) {
            factToAdd.calculateData();
            allTrackingsFactCollection.add(factToAdd);
        }

        sortAllTrackingsFacts();

        return rx.Observable.from(allTrackingsFactCollection);
    }

    private void functionApplicabilityCheck(Tracking tracking)
    {
        Fact factToAdd;

        factToAdd = FunctionApplicability.avrgRatingApplicability(tracking);
        if (factToAdd != null) {
            factToAdd.calculateData();
            oneTrackingFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.avrgScaleApplicability(tracking);
        if (factToAdd != null) {
            factToAdd.calculateData();
            oneTrackingFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.sumScaleApplicability(tracking);
        if (factToAdd != null) {
            factToAdd.calculateData();
            oneTrackingFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.trackingEventsCountApplicability(tracking);
        if (factToAdd != null) {
            factToAdd.calculateData();
            oneTrackingFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.worstEventApplicability(tracking);
        if (factToAdd != null) {
            factToAdd.calculateData();
            oneTrackingFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.bestEventApplicability(tracking);
        if (factToAdd != null) {
            factToAdd.calculateData();
            oneTrackingFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.certainWeekDaysApplicability(tracking);
        if (factToAdd != null){
            oneTrackingFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.certainDayTimeApplicability(tracking);
        if (factToAdd != null) {
            oneTrackingFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.longTimeAgoApplicability(tracking);
        if (factToAdd != null) {
            oneTrackingFactCollection.add(factToAdd);
        }

        sortOneTrackingFacts();
    }

    public List<Fact> getAllTrackingsFactCollection()
    {
        return allTrackingsFactCollection;
    }

    public List<Fact> getOneTrackingFactCollection(UUID trackingId) {
        List<Fact> collectionToReturn = new ArrayList<>();
        for (Fact fact : oneTrackingFactCollection) {
            if(fact.getTrackingId()!=null && fact.getTrackingId().equals(trackingId)){
                collectionToReturn.add(fact);
            }
        }
        return collectionToReturn;
    }

    public void setOneTrackingFactCollection(List<Fact> oneTrackingFactCollection) {
        this.oneTrackingFactCollection = oneTrackingFactCollection;
    }

    private void sortOneTrackingFacts(){
        Collections.sort(oneTrackingFactCollection, new Comparator<Fact>() {
            @Override
            public int compare(Fact fact, Fact t1) {
                return fact.getPriority().compareTo(t1.getPriority());
            }
        });
    }

    private void sortAllTrackingsFacts(){
        Collections.sort(allTrackingsFactCollection, new Comparator<Fact>() {
            @Override
            public int compare(Fact fact, Fact t1) {
                return fact.getPriority().compareTo(t1.getPriority());
            }
        });
    }

    private List<Fact> oneTrackingFactCollection = new ArrayList<>();
    private List<Fact> allTrackingsFactCollection = new ArrayList<>();
    private FunctionApplicability functionApplicability;

}
