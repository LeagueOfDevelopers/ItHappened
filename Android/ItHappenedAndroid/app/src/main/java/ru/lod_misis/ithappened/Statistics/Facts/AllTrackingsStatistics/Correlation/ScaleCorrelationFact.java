package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation;

import java.util.List;

import cern.jet.stat.Probability;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods.CorrelationComputer;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods.SpearmanCorrelationComputer;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;

public class ScaleCorrelationFact extends Fact {

    private static final double Alpha = 0.05; // Этим параметром можно регулировать строгость оценки
                                 // значимости корреляции (чем он выше, тем ниже порог
                                 // для признания корреляции значимой)
    private DataSet<Double> ScaleData;
    private String FirstTrackingName;
    private String FirstTrackingScaleName;
    private String SecondTrackingName;
    private String SecondTrackingScaleName;
    private Double Correlation;
    private static final int DaysToTrack = 1;
    private CorrelationComputer computer = new SpearmanCorrelationComputer(Alpha);

    public ScaleCorrelationFact(TrackingV1 tracking1, TrackingV1 tracking2) {
        FirstTrackingName = tracking1.GetTrackingName();
        FirstTrackingScaleName = tracking1.getScaleName();
        SecondTrackingName = tracking2.GetTrackingName();
        SecondTrackingScaleName = tracking2.getScaleName();
        ScaleData = DataSetBuilder.BuildDoubleDataSet(tracking1.getEventV1Collection(),
                tracking2.getEventV1Collection(), DaysToTrack);
    }

    public ScaleCorrelationFact(DataSet<Double> scale) {
        ScaleData = scale;
    }

    @Override
    public void calculateData() {
        Correlation = computer.ComputeCorrelation(ScaleData);
        calculatePriority();
    }

    @Override
    protected void calculatePriority() {
        if (Correlation != null)
            priority = 40.0 * Math.abs(Correlation);
        else
            priority = 0.0;
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildScaleCorrelationReport(Correlation,
                FirstTrackingName, FirstTrackingScaleName, SecondTrackingName, SecondTrackingScaleName);
    }

    public boolean IsDoubleCorrSignificant() {
        return computer.IsCorrelationSignificant();
    }

    public Double getCorrelation() {
        return Correlation;
    }
}
