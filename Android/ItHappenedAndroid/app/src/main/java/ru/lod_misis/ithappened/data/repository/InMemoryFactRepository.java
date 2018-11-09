package ru.lod_misis.ithappened.data.repository;

import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.domain.statistics.facts.Fact;

public interface InMemoryFactRepository {


    void addOneTrackingFacts(List<Fact> factToAdd, UUID trackingId);

    void addAllTrackingFacts(List<Fact> facts);

    List<Fact> getOneTrackingFactCollection();

    List<Fact> getAllTrackingsFactCollection();

    List<Fact> getOneTrackingFactCollectionById(UUID trackingId);
}
