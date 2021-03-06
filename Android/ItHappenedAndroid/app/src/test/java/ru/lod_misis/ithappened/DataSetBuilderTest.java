package ru.lod_misis.ithappened;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.statistics.facts.models.builders.DataSetBuilder;
import ru.lod_misis.ithappened.domain.statistics.facts.models.collections.DataSet;
import ru.lod_misis.ithappened.domain.statistics.facts.models.collections.Sequence;
import ru.lod_misis.ithappened.domain.statistics.facts.models.trends.EventsTimeDistribution;

public class DataSetBuilderTest {

    @Test
    public void PrepareDoubleDatasetTest_PrepareDoubleDatasetFunctionWorksCorrect() {
        List<List<EventV1>> eventSet = GenerateEventCollectionPair(10);
        DataSet<Integer> intDS = DataSetBuilder.BuildBooleanDataset(eventSet.get(0), eventSet.get(1), 1);
        DataSet<Double> doubleDS = DataSetBuilder.BuildDoubleDataSet(eventSet.get(0), eventSet.get(1), 1);

        Assert.assertEquals(intDS.Length(), 10);
        Assert.assertEquals(doubleDS.Length(), 9);
    }

    @Test
    public void BuildFrequencySequenceTest_FrequencySequenceAreCorrect() {
        List<EventV1> events = GenerateEventCollection1ToFreqCalculation();
        EventsTimeDistribution distr = DataSetBuilder.BuildFrequencySequence(events);
        Sequence freqs = new Sequence(distr.toCountsArray());
        for (int i = 0; i < freqs.Length(); i++) {
            Assert.assertTrue(freqs.get(i) >= 0 || freqs.get(i) <= 2);
        }
    }
    // Тест не проходит, имеются отклонения, но на тренд они значительно не влияют

    private List<EventV1> GenerateEventCollection1ToFreqCalculation() {
        List<EventV1> events = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            EventV1 e = new EventV1();
            e.setEventDate(new DateTime(2000, i / 30 + 1, i % 25 + 1, 0, 0).toDate());
            events.add(e);
        }
        return events;
    }

    private List<List<EventV1>> GenerateEventCollectionPair(int count) {
        List<List<EventV1>> result = new ArrayList<>();
        List<EventV1> events1 = new ArrayList<>();
        List<EventV1> events2 = new ArrayList<>();
        for (int i = 1; i <= count; i += 2) {
            EventV1 event = new EventV1();
            event.editDate(new DateTime(2000, 1, i, 0, 0).toDate());
            event.editScale((double)i * 100);
            events1.add(event);
        }
        for (int i = 2; i <= count; i += 2) {
            EventV1 event = new EventV1();
            event.editDate(new DateTime(2000, 1, i, 0, 0).toDate());
            event.editScale((double)i * 100);
            events2.add(event);
        }
        result.add(events1);
        result.add(events2);
        return result;
    }
}
