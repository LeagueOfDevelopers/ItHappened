package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods;

import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;

public abstract class CorrelationComputer {

    double Alpha;
    int DataSize;
    protected Double Correlation;
    public abstract Double ComputeCorrelation(DataSet data);
    public abstract boolean IsCorrelationSignificant();
}
