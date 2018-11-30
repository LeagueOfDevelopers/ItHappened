package ru.lod_misis.ithappened.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.data.repository.InMemoryFactRepository;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.FunctionApplicability;
import rx.Observable;

public class FactService {

    private InMemoryFactRepository repository;

    public FactService(InMemoryFactRepository repository) {
        this.repository = repository;
    }

    public Observable calculateOneTrackingFacts(List<TrackingV1> trackingV1Collection) {
        for (TrackingV1 trackingV1 : trackingV1Collection) {
            functionApplicabilityCheck(trackingV1);
        }

        return Observable.from(repository.getOneTrackingFactCollection());
    }

    public Observable<Fact> onChangeCalculateOneTrackingFacts(List<TrackingV1> trackingV1Collection, UUID trackingId) {
        List<Fact> oneTrackingFactCollection = repository.getOneTrackingFactCollection();
        TrackingV1 changedTrackingV1 = null;
        List<Fact> factCollectionCheck = new ArrayList<>();

        for (Fact fact : oneTrackingFactCollection) {
            if (fact.getTrackingId().equals(trackingId))
                factCollectionCheck.add(fact);
        }

        oneTrackingFactCollection.removeAll(factCollectionCheck);

        for (TrackingV1 trackingV1 : trackingV1Collection) {
            if (trackingV1.getTrackingId().equals(trackingId))
                changedTrackingV1 = trackingV1;
        }

        if (changedTrackingV1 == null)
            throw new IllegalArgumentException("tracking not exists");

        functionApplicabilityCheck(changedTrackingV1);

        return Observable.from(oneTrackingFactCollection);
    }

    public Observable<Fact> calculateAllTrackingsFacts(List<TrackingV1> trackingV1Collection) {
        List<Fact> factsToSave = new ArrayList<>();
        Fact factToAdd;
        List<Fact> facts;

        factToAdd = FunctionApplicability.allEventsCountFactApplicability(trackingV1Collection);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.mostFrequentEventApplicability(trackingV1Collection);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.DayWithLargestEventCountApplicability(trackingV1Collection);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.WeekWithLargestEventCountApplicability(trackingV1Collection);
        factsToSave.add(factToAdd);

        facts = FunctionApplicability.BinaryCorrelationFactApplicability(trackingV1Collection);
        factsToSave.addAll(facts);

        facts = FunctionApplicability.MultinomialCorrelationApplicability(trackingV1Collection);
        factsToSave.addAll(facts);

        facts = FunctionApplicability.ScaleCorrelationFactApplicability(trackingV1Collection);
        factsToSave.addAll(facts);

        repository.addAllTrackingFacts(factsToSave);

        return Observable.from(repository.getAllTrackingsFactCollection());
    }

    public List<Fact> getAllTrackingsFactCollection() {
        return  repository.getAllTrackingsFactCollection();
    }

    public List<Fact> getOneTrackingsFactCollectionById(UUID id) {
        return  repository.getOneTrackingFactCollectionById(id);
    }

    private void functionApplicabilityCheck(TrackingV1 tracking) {
        Fact factToAdd;
        List<Fact> factsToSave = new ArrayList<>();

        factToAdd = FunctionApplicability.avrgRatingApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.avrgScaleApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.sumScaleApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.trackingEventsCountApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.worstEventApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.bestEventApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.certainWeekDaysApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.certainDayTimeApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.longTimeAgoApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.FrequencyTrendChangingFactApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.RatingTrendChangingFactApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.ScaleTrendChangingFactApplicability(tracking);
        factsToSave.add(factToAdd);

        factToAdd = FunctionApplicability.LongestBreakFactApplicability(tracking);
        factsToSave.add(factToAdd);

        repository.addOneTrackingFacts(factsToSave, tracking.getTrackingId());
    }
}
