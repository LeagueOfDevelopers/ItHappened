package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation;

import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods.CorrelationComputer;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods.MatthewsCorrelationComputer;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods.SpearmanCorrelationComputer;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;

public class BinaryCorrelationFact extends Fact {

    private static final double Alpha = 0.05; // Этим параметром можно регулировать строгость оценки
    // значимости корреляции (чем он выше, тем ниже порог
    // для признания корреляции значимой)
    private DataSet<Integer> BinaryData;
    private String FirstTrackingName;
    private String SecondTrackingName;
    private Double Correlation;
    private CorrelationComputer computer = new MatthewsCorrelationComputer(Alpha);

    public BinaryCorrelationFact(TrackingV1 tracking1, TrackingV1 tracking2) {
        FirstTrackingName = tracking1.GetTrackingName();
        SecondTrackingName = tracking2.GetTrackingName();
        BinaryData = DataSetBuilder.BuildBooleanDataSet(tracking1.getEventV1Collection(),
                tracking2.getEventV1Collection());
    }

    public BinaryCorrelationFact(DataSet<Integer> binaryData) {
        BinaryData = binaryData;
    }

    @Override
    public void calculateData() {
        Correlation = computer.ComputeCorrelation(BinaryData);
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
        return DescriptionBuilder.BuildBinaryCorrelationReport(Correlation,
                FirstTrackingName, SecondTrackingName);
    }

    public Double getCorrelation() {
        return Correlation;
    }

    public boolean IsBoolCorrSignificant() {
        return computer.IsCorrelationSignificant();
    }
}
