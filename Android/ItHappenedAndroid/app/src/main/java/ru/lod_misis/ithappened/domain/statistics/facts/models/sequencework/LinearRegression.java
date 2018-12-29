package ru.lod_misis.ithappened.domain.statistics.facts.models.sequencework;

public class LinearRegression {

    private double aCoef;
    private double bCoef;

    public LinearRegression(double a, double b) {
        aCoef = a;
        bCoef = b;
    }

    public double predictionInPoint(double x) {
        return aCoef * x + bCoef;
    }
}
