package ru.lod_misis.ithappened.Infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.FunctionApplicability;

public class InMemoryFactRepository {

    public InMemoryFactRepository(){

    }

    public rx.Observable calculateOneTrackingFacts(List<TrackingV1> trackingV1Collection)
    {
        oneTrackingFactCollection = new ArrayList<>();
        for (TrackingV1 trackingV1 : trackingV1Collection) {
            functionApplicabilityCheck(trackingV1);
        }

        return rx.Observable.from(oneTrackingFactCollection);
    }

    public rx.Observable<Fact> onChangeCalculateOneTrackingFacts(List<TrackingV1> trackingV1Collection, UUID trackingId)
    {
        TrackingV1 changedTrackingV1 = null;
        List<Fact> factCollectionCheck = new ArrayList<>();

        for (Fact fact: oneTrackingFactCollection) {
            if (fact.getTrackingId().equals(trackingId))
                factCollectionCheck.add(fact);
        }

        oneTrackingFactCollection.removeAll(factCollectionCheck);

        for (TrackingV1 trackingV1 : trackingV1Collection) {
            if (trackingV1.GetTrackingID().equals(trackingId))
                changedTrackingV1 = trackingV1;
        }

        if (changedTrackingV1 == null)
            throw new IllegalArgumentException("tracking not exists");

        functionApplicabilityCheck(changedTrackingV1);


        if(allTrackingsFactCollection.size() > 1)
            sortAllTrackingsFacts();

        return rx.Observable.from(oneTrackingFactCollection);
    }

    public rx.Observable<Fact> calculateAllTrackingsFacts(List<TrackingV1> trackingV1Collection) {
        Fact factToAdd;
        List<Fact> facts = new ArrayList<>();
        allTrackingsFactCollection = new ArrayList<>();

        factToAdd = FunctionApplicability.allEventsCountFactApplicability(trackingV1Collection);
        addFactToAllFactCollection(factToAdd);

        factToAdd = FunctionApplicability.mostFrequentEventApplicability(trackingV1Collection);
        addFactToAllFactCollection(factToAdd);

        factToAdd = FunctionApplicability.DayWithLargestEventCountApplicability(trackingV1Collection);
        if (factToAdd != null) allTrackingsFactCollection.add(factToAdd);
        //addFactToAllFactCollection(factToAdd);

        factToAdd = FunctionApplicability.WeekWithLargestEventCountApplicability(trackingV1Collection);
        if (factToAdd != null) allTrackingsFactCollection.add(factToAdd);
        //addFactToAllFactCollection(factToAdd);

        facts = FunctionApplicability.BinaryCorrelationFactApplicability(trackingV1Collection);
        allTrackingsFactCollection.addAll(facts);
        //for (Fact fact: facts) {
        //    addFactToAllFactCollection(fact);
        //}

        //facts = new ArrayList<>();
        facts = FunctionApplicability.MultinomialCorrelationApplicability(trackingV1Collection);
        //addFactToAllFactCollection(fact);
        allTrackingsFactCollection.addAll(facts);

        //facts = new ArrayList<>();
        facts = FunctionApplicability.ScaleCorrelationFactApplicability(trackingV1Collection);
        allTrackingsFactCollection.addAll(facts);
        //for (Fact fact: facts) {
        //    addFactToAllFactCollection(fact);
        //}

        if(allTrackingsFactCollection.size() > 1)
            sortAllTrackingsFacts();

        return rx.Observable.from(allTrackingsFactCollection);
    }

    private void functionApplicabilityCheck(TrackingV1 trackingV1)
    {
        Fact factToAdd;

        factToAdd = FunctionApplicability.avrgRatingApplicability(trackingV1);
        addFactToOneTrackingFactCollection(factToAdd);

        factToAdd = FunctionApplicability.avrgScaleApplicability(trackingV1);
        addFactToOneTrackingFactCollection(factToAdd);

        factToAdd = FunctionApplicability.sumScaleApplicability(trackingV1);
        addFactToOneTrackingFactCollection(factToAdd);

        factToAdd = FunctionApplicability.trackingEventsCountApplicability(trackingV1);
        addFactToOneTrackingFactCollection(factToAdd);

        factToAdd = FunctionApplicability.worstEventApplicability(trackingV1);
        addFactToOneTrackingFactCollection(factToAdd);

        factToAdd = FunctionApplicability.bestEventApplicability(trackingV1);
        addFactToOneTrackingFactCollection(factToAdd);

        factToAdd = FunctionApplicability.certainWeekDaysApplicability(trackingV1);
        if (factToAdd != null) {
            oneTrackingFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.certainDayTimeApplicability(trackingV1);
        if (factToAdd != null) {
            oneTrackingFactCollection.add(factToAdd);
        }

        factToAdd = FunctionApplicability.longTimeAgoApplicability(trackingV1);
        addFactToOneTrackingFactCollection(factToAdd);

        factToAdd = FunctionApplicability.FrequencyTrendChangingFactApplicability(trackingV1);
        if (factToAdd != null) oneTrackingFactCollection.add(factToAdd);
        //addFactToOneTrackingFactCollection(factToAdd);

        factToAdd = FunctionApplicability.LongestBreakFactApplicability(trackingV1);
        if (factToAdd != null) oneTrackingFactCollection.add(factToAdd);
        //addFactToOneTrackingFactCollection(factToAdd);

        factToAdd = FunctionApplicability.RatingTrendChangingFactApplicability(trackingV1);
        if (factToAdd != null) oneTrackingFactCollection.add(factToAdd);
        //addFactToOneTrackingFactCollection(factToAdd);

        factToAdd = FunctionApplicability.ScaleTrendChangingFactApplicability(trackingV1);
        if (factToAdd != null) oneTrackingFactCollection.add(factToAdd);
        //addFactToOneTrackingFactCollection(factToAdd);

        if (oneTrackingFactCollection.size() > 1)
            sortOneTrackingFacts();
    }

    private void addFactToOneTrackingFactCollection(Fact factToAdd){
        if (factToAdd != null) {
            factToAdd.calculateData();
            oneTrackingFactCollection.add(factToAdd);
        }
    }

    private void addFactToAllFactCollection(Fact factToAdd){
        if (factToAdd != null) {
            factToAdd.calculateData();
            allTrackingsFactCollection.add(factToAdd);
        }
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
                return t1.getPriority().compareTo(fact.getPriority());
            }
        });
    }

    private void sortAllTrackingsFacts(){
        Collections.sort(allTrackingsFactCollection, new Comparator<Fact>() {
            @Override
            public int compare(Fact fact, Fact t1) {
                return t1.getPriority().compareTo(fact.getPriority());
            }
        });
    }

    private List<Fact> oneTrackingFactCollection = new ArrayList<>();
    private List<Fact> allTrackingsFactCollection = new ArrayList<>();
}
