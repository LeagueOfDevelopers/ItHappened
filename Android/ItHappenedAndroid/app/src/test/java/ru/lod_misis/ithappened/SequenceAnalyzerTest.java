package ru.lod_misis.ithappened;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.Sequence;
import ru.lod_misis.ithappened.Statistics.Facts.Models.SequenceWork.SequenceAnalyzer;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingData;

public class SequenceAnalyzerTest {

    private Sequence sequence;

    @Test
    public void DetectTrendChangingPointTest_GotCorrectTrendChangingPoint() {
        InitializeRatingSeq();
        TrendChangingData data1 = SequenceAnalyzer.DetectTrendChangingPoint(sequence);
        TrendChangingData data2 = SequenceAnalyzer.DetectTrendChangingPoint(sequence.Slice(0, 12));

        Assert.assertTrue(data1.getEventInCollectionId() == 15);
        Assert.assertTrue(data2.getEventInCollectionId() == 1);
    }

    private void InitializeRatingSeq() {
        List<Double> list = new ArrayList<>();
        String dataset = "3, 5, 4, 6, 7, 5, 7, 5, 6, 8, 7, 10, 1, 2, 3, 3, 2, 1";
        for (String s: Arrays.asList(dataset.split(", "))) {
            list.add(Double.parseDouble(s));
        }
        sequence = new Sequence(list);
    }
}
