package ru.lod_misis.ithappened.domain.statistics.facts.models.collections;

import java.util.ArrayList;
import java.util.List;

public class Sequence {

    private List<Double> Data;
    private double Sum;
    private double Min;
    private double Max;

    public Sequence(List<Double> data) {
        Data = data;
        Min = data.get(0);
        Max = data.get(0);
        for (Double d: data) {
            Sum += d;
            if (d < Min) {
                Min = d;
            }
            if (d > Max) {
                Max = d;
            }
        }
    }

    public double Sum() {
        return Sum;
    }

    public double Mean() {
        return Sum() / Data.size();
    }

    public int Length() {
        return Data.size();
    }

    public Double get(int i) {
        return Data.get(i);
    }

    public Sequence Mult(Sequence sequence) {
        if (sequence.Length() != Length()) {
            throw new IllegalArgumentException("Second sequence must have same length.");
        }
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < Length(); i++) {
            result.add(get(i) * sequence.get(i));
        }
        return new Sequence(result);
    }

    public Sequence Pow(int degree) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < Length(); i++) {
            result.add(Math.pow(get(i), degree));
        }
        return new Sequence(result);
    }

    public Sequence Slice(int start, int stop) {
        List<Double> result = new ArrayList<>();
        for (int i = start; i < stop; i++) {
            result.add(get(i));
        }
        return new Sequence(result);
    }

    public Sequence DiffConst(double constant) {
        List<Double> result = new ArrayList<>();
        for (double i: Data) {
            result.add(i - constant);
        }
        return new Sequence(result);
    }

    public double Var() {
        return this.DiffConst(this.Mean()).Pow(2).Sum() / (this.Length() - 1);
    }

    public double Max() {
        return Max;
    }

    public double Min() {
        return Min;
    }
}
