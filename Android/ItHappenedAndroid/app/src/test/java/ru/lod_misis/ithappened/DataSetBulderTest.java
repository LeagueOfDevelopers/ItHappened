package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.Sequence;

public class DataSetBulderTest {

    @Test
    public void PrepareDoubleDatasetTest_PrepareDoubleDatasetFunctionWorksCorrect() {
        List<List<Event>> eventSet = GenerateEventCollectionPair(10);
        DataSet<Integer> intDS = DataSetBuilder.BuildBooleanDataset(eventSet.get(0), eventSet.get(1), 1);
        DataSet<Double> doubleDS = DataSetBuilder.BuildDoubleDataSet(eventSet.get(0), eventSet.get(1), 1);

        Assert.assertEquals(intDS.Length(), 9);
        Assert.assertEquals(doubleDS.Length(), 9);
    }

    @Test
    public void BuildFrequencySequenceTest_FrequencySequenceAreCorrect() {
        List<Event> events = GenerateEventCollection1ToFreqCalculation();
        Sequence freqs = DataSetBuilder.BuildFrequencySequence(events);
        for (int i = 0; i < freqs.Length(); i++) {
            Assert.assertTrue(freqs.get(i) == 1);
        }
    }

    private List<Event> GenerateEventCollection1ToFreqCalculation() {
        List<Event> events = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            Event e = new Event();
            e.SetEventDate(new Date(2000, i / 30 + 1, i % 30));
            events.add(e);
        }
        return events;
    }

    private List<List<Event>> GenerateEventCollectionPair(int count) {
        List<List<Event>> result = new ArrayList<>();
        List<Event> events1 = new ArrayList<>();
        List<Event> events2 = new ArrayList<>();
        for (int i = 1; i <= count; i += 2) {
            Event event = new Event();
            event.EditDate(new Date(2000, 1, i));
            event.EditScale((double)i * 100);
            events1.add(event);
        }
        for (int i = 2; i <= count; i += 2) {
            Event event = new Event();
            event.EditDate(new Date(2000, 1, i));
            event.EditScale((double)i * 100);
            events2.add(event);
        }
        result.add(events1);
        result.add(events2);
        return result;
    }
}
