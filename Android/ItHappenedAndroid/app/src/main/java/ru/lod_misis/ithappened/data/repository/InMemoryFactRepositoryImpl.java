package ru.lod_misis.ithappened.data.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.FunctionApplicability;

public class InMemoryFactRepositoryImpl implements InMemoryFactRepository {

    private List<Fact> oneTrackingFactCollection;
    private List<Fact> allTrackingsFactCollection;

    public InMemoryFactRepositoryImpl() {
        oneTrackingFactCollection = new ArrayList<>();
        allTrackingsFactCollection = new ArrayList<>();
    }

    public void addOneTrackingFacts(List<Fact> facts, UUID trackingID) {
        List<Fact> factsToRemove = new ArrayList<>();

        for (Fact fact : oneTrackingFactCollection) {
            if (fact.getTrackingId().equals(trackingID))
                factsToRemove.add(fact);
        }

        for (Fact fact: factsToRemove) {
            oneTrackingFactCollection.remove(fact);
        }

        for (Fact fact: facts) {
            if (fact != null) {
                fact.calculateData();
                oneTrackingFactCollection.add(fact);
            }
        }
    }

    @Override
    public void addAllTrackingFacts(List<Fact> facts) {
        allTrackingsFactCollection = new ArrayList<>();
        for (Fact factToAdd : facts) {
            if (factToAdd != null) {
                factToAdd.calculateData();
                allTrackingsFactCollection.add(factToAdd);
            }
        }
    }

    @Override
    public List<Fact> getOneTrackingFactCollection() {
        return sortFactCollection(oneTrackingFactCollection);
    }

    public List<Fact> getAllTrackingsFactCollection() {
        return sortFactCollection(allTrackingsFactCollection);
    }

    public List<Fact> getOneTrackingFactCollectionById(UUID trackingId) {
        List<Fact> collectionToReturn = new ArrayList<>();
        for (Fact fact : oneTrackingFactCollection) {
            if (fact.getTrackingId() != null && fact.getTrackingId().equals(trackingId)) {
                collectionToReturn.add(fact);
            }
        }
        return sortFactCollection(collectionToReturn);
    }

    public void setOneTrackingFactCollection(List<Fact> oneTrackingFactCollection) {
        this.oneTrackingFactCollection = oneTrackingFactCollection;
    }

    private List<Fact> sortFactCollection(List<Fact> facts) {
        Collections.sort(facts, new Comparator<Fact>() {
            @Override
            public int compare(Fact fact, Fact t1) {
                return t1.getPriority().compareTo(fact.getPriority());
            }
        });

        return facts;
    }
}
