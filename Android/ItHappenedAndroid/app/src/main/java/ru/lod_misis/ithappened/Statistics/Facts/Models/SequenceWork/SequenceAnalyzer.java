package ru.lod_misis.ithappened.Statistics.Facts.Models.SequenceWork;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.Sequence;

public class SequenceAnalyzer {

    private static final double DELTA = 0.000005; // В интервале [0 - DELTA, 0 + DELTA] тренд незначим

    public static int DetectTrendChangingPoint(Sequence y) {
        Integer cpointInd = 0; // Индекс последней точки изменения тренда
        Double lastCoef = null; // Последний коэффициент (для сравнения с новым)
        for (int i = 1; i < y.Length(); i++) {

            // Начинаем с единицы, так как 0 - это дефолтная точка перегиба
            // Считаем коэффициент для среза с 0 по текущую точку
            Double coef = CalculateACoefficient(y.Slice(0, i));

            // Если это первая итерация, просто запоминаем коэффициент и идем дальше
            // В противном случае проверяем, интересует ли нас
            // этот коэффициент, а интересует он нас, если он
            // лежит по другую строну от 0 и он значим (отклоняется от 0 более чем на DELTA)
            if (lastCoef != null && IsCoefficientInAnotherZone(lastCoef, coef) && IsTrendSignificant(coef)) {
                cpointInd = i - 1;
            }
            lastCoef = coef;
        }
        // Возвращаем индекс переломной точки
        return cpointInd;
    }
    // Метод поиска последней точки, в которой тренд изменился. На каждой
    // итерации мы просто проверяем, текущий коэффициент и если он значим
    // и находится в другой зоне от прошлого коэффициента (по другую сторону от 0),
    // то говорим, что этот коэффициент и есть последний перегиб графика.

    private static double CalculateACoefficient(Sequence Y) {
        int n = Y.Length();
        Sequence X = Range(0, n);
        // Формула для расчета коэффициента а линейной регрессии у = ax + b
        return (n * X.Mult(Y).Sum() - X.Sum() * Y.Sum()) / (n * X.Pow(2).Sum() - Math.pow(X.Sum(), 2));
    }

    private static boolean IsCoefficientInAnotherZone(double first, double second) {
        return first * second < 0;
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