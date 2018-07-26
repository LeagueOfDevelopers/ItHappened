package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation;

import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods.CorrelationComputer;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods.TKendallCorrelationComputer;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;

public class MultinomialCorrelationFact extends Fact {

    private static final double Alpha = 0.05; // Этим параметром можно регулировать строгость оценки
    // значимости корреляции (чем он выше, тем ниже порог
    // для признания корреляции значимой)
    private DataSet<Integer> MultinomialData;
    private String FirstTrackingName;
    private String SecondTrackingName;
    private static final int DaysToTrack = 1;
    private Double Correlation;
    private CorrelationComputer computer = new TKendallCorrelationComputer(Alpha);

    public MultinomialCorrelationFact(TrackingV1 tracking1, TrackingV1 tracking2) {
        FirstTrackingName = tracking1.GetTrackingName();
        SecondTrackingName = tracking2.GetTrackingName();
        MultinomialData = DataSetBuilder.BuildMultinomialDataSet(tracking1.getEventV1Collection(),
                tracking2.getEventV1Collection(), DaysToTrack);
    }

    public MultinomialCorrelationFact(DataSet<Integer> mult) {
        MultinomialData = mult;
    }

    @Override
    public void calculateData() {
        Correlation = computer.ComputeCorrelation(MultinomialData);
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
        return DescriptionBuilder.BuildMultinomialCorrelationReport(Correlation,
                FirstTrackingName, SecondTrackingName);
    }

    public Double getCorrelation() {
        return Correlation;
    }

    public boolean IsMultinomialCorrSignificant() {
        return computer.IsCorrelationSignificant();
    }
}
