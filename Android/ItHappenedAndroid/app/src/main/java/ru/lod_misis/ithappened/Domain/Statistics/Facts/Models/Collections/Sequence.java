package ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Collections;

import java.util.ArrayList;
import java.util.List;

public class Sequence {

    private List<Double> Data;
    private double Sum;

    public Sequence(List<Double> data) {
        Data = data;
        for (Double d: data) {
            Sum += d;
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

    public List<Double> ToList() {
        return Data;
    }
}
