package ru.lod_misis.ithappened;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    /*@Test
    public void DetectTrendChangingPointStressTest() {
        Random rand = new Random();
        while (true) {
            int size = Math.abs(rand.nextInt());
            List<Double> seq = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                seq.add((double)rand.nextInt());
            }
            Sequence sequence = new Sequence(seq);
            DateTime stime = DateTime.now();
            SequenceAnalyzer.DetectTrendChangingPoint(sequence);
            DateTime etime = DateTime.now();
            System.out.print("Completed in time " +
                    (new Interval(etime.toDate().getTime() - stime.toDate().getTime())) +
                    " || Size " + size);
        }
    }*/

    private void InitializeRatingSeq() {
        List<Double> list = new ArrayList<>();
        String dataset = "3, 5, 4, 6, 7, 5, 7, 5, 6, 8, 7, 10, 1, 2, 3, 3, 2, 1";
        for (String s: Arrays.asList(dataset.split(", "))) {
            list.add(Double.parseDouble(s));
        }
        sequence = new Sequence(list);
    }
}
