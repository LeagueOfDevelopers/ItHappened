package ru.lod_misis.ithappened.Statistics.Facts.Models.Collections;

import java.util.ArrayList;
import java.util.List;

public class Sequence {

    private List<Double> Data;

    public Sequence(List<Double> data) {
        Data = data;
    }

    public double Sum() {
        double res = 0;
        for (Double d: Data) {
            res += d;
        }
        return res;
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

    public Sequence Add(Sequence sequence) {
        if (sequence.Length() != Length()) {
            throw new IllegalArgumentException("Second sequence must have same length.");
        }
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < Length(); i++) {
            result.add(get(i) + sequence.get(i));
        }
        return new Sequence(result);
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
}
