package ru.lod_misis.ithappened.Statistics.Facts.Models.SequenceWork;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.Sequence;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingData;

public class SequenceAnalyzer {

    private static final double DELTA = 0.000005; // В интервале [0 - DELTA, 0 + DELTA] тренд незначим

    public static TrendChangingData DetectTrendChangingPoint(Sequence data) {
        Integer cpointInd = 0;
        Double lastCoef = null;
        Double pointCoef = 0.0;
        for (int i = 1; i < data.Length(); i++) {
            Double coef = CalculateACoefficient(data.Slice(0, i));
            if (lastCoef == null) {
                lastCoef = coef;
                continue;
            }
            if (IsCoefficientInAnotherZone(lastCoef, coef) && IsTrendSignificant(coef)) {
                cpointInd = i - 1;
                pointCoef = coef;
            }
            lastCoef = coef;
        }
        return new TrendChangingData(cpointInd, pointCoef);
    }
    // Метод поиска последней точки, в которой тренд изменился. На каждой
    // итерации мы просто проверяем, текущий коэффициент и если он значим
    // и находится в другой зоне от прошлого коэффициента (по другую сторону от 0),
    // то говорим, что этот коэффициент и есть последний перегиб графика.

    private static double CalculateACoefficient(Sequence Y) {
        int n = Y.Length();
        Sequence X = Range(0, n);
        return (n * X.Mult(Y).Sum() - X.Sum() * Y.Sum()) / (n * X.Pow(2).Sum() - Math.pow(X.Sum(), 2));
    }

    private static boolean IsCoefficientInAnotherZone(double first, double second) {
        return (IsTrendSignificant(first) && !IsTrendSignificant(second)) ||
                (IsTrendSignificant(second) && !IsTrendSignificant(first)) ||
                (second > 0.0 + DELTA && first < 0.0 - DELTA) ||
                (first > 0.0 + DELTA && second < 0.0 - DELTA);
    }

    private static boolean IsTrendSignificant(double aCoefficient) {
        return aCoefficient > 0.0 + DELTA || aCoefficient < 0.0 - DELTA;
    }

    private static Sequence Range(int start, int stop) {
        List<Double> result = new ArrayList<>();
        for (int i = start; i < stop; i++) {
            result.add((double)i);
        }
        return new Sequence(result);
    }
    // Генерирует массив чисел от 0 до length. Нужно для создания подобия графика
}