package ru.lod_misis.ithappened.domain.statistics.facts.models.sequencework;

import java.util.ArrayList;
import java.util.List;

import EDU.oswego.cs.dl.util.concurrent.FJTask;
import cern.jet.stat.Probability;

import ru.lod_misis.ithappened.domain.statistics.facts.models.collections.Sequence;

public class SequenceAnalyzer {

    private static final double DELTA = 0.000005; // В интервале [0 - DELTA, 0 + DELTA] тренд незначим
    private static final double Alpha = 0.05; // Достигаемый уровень значимости значимости

    public static int DetectTrendChangingPoint(Sequence y) {
        Integer cpointInd = 0; // Индекс последней точки изменения тренда
        Double lastCoef = null; // Последний коэффициент (для сравнения с новым)
        Sequence X = Range(0, y.Length());
        for (int i = 1; i < y.Length(); i++) {

            // Начинаем с единицы, так как 0 - это дефолтная точка перегиба
            // Считаем коэффициент для среза с 0 по текущую точку
            Sequence Ysliced = y.Slice(0, i);
            Sequence Xsliced = X.Slice(0, i);
            Double coef = CalculateACoefficient(Ysliced, Xsliced);

            // Сперва нам надо найти первый значимый тренд в последовательности
            // В противном случае проверяем, интересует ли нас
            // этот коэффициент, а интересует он нас, если он
            // лежит по другую строну от 0, он значим
            // (отклоняется от 0 более чем на DELTA и проходит Т-тест)
            if (lastCoef != null) {
                if (IsCoefficientInAnotherZone(lastCoef, coef)
                        && IsTrendSignificant(coef)
                        && TSignificanceTest(coef, Ysliced, Xsliced)){
                    lastCoef = coef;
                    cpointInd = i;
                }
            }
            // Если первый значимый тренд еще не найден,
            // то проверяем на значимость текущий коэффициент и
            // берем коэффициент как lastCoef только в случе успеха проверки
            else {
                if (IsTrendSignificant(coef)
                        && TSignificanceTest(coef, Ysliced, Xsliced)) {
                    lastCoef = coef;
                    cpointInd = i;
                }
            }
        }
        // Возвращаем индекс переломной точки.
        // Если значимого тренда так и не встретилось, то вернем -1
        return cpointInd - 1;
    }
    // Метод поиска последней точки, в которой тренд изменился. На каждой
    // итерации мы просто проверяем, текущий коэффициент и если он значим
    // и находится в другой зоне от прошлого коэффициента (по другую сторону от 0),
    // то говорим, что этот коэффициент и есть последний перегиб графика.

    public static LinearRegression BuildLinearRegression(Sequence Y, int startX) {
        Sequence X = Range(startX, startX + Y.Length());
        double a = CalculateACoefficient(Y, X);
        double b = CalculateBCoefficient(Y, X, a);
        return new LinearRegression(a, b);
    }

    private static double CalculateACoefficient(Sequence Y, Sequence X) {
        // Формула для расчета коэффициента а линейной регрессии у = ax + b
        return X.DiffConst(X.Mean()).Mult(Y.DiffConst(Y.Mean())).Sum() / X.DiffConst(X.Mean()).Pow(2).Sum();
        //int n = X.Length();
        //return (n * X.Mult(Y).Sum() - X.Sum() * Y.Sum()) / (n * X.Pow(2).Sum() - Math.pow(X.Sum(), 2));
    }

    private static double CalculateBCoefficient(Sequence Y, Sequence X, Double A) {
        // Из уранения Y = AX + B следует, что
        // mean(Y) = mean(AX + B) = A * mean(X) + B => B = mean(Y) - A * mean(X)
        return Y.Mean() - A * X.Mean();
    }

    private static boolean IsCoefficientInAnotherZone(double first, double second) {
        // Если условие выполняется, то это значит, что один из
        // коэффициентов больше 0, а другой меньше или наоборот
        return first * second < 0;
    }

    private static boolean IsTrendSignificant(double aCoefficient) {
        return aCoefficient > 0.0 + DELTA || aCoefficient < 0.0 - DELTA;
    }

    private static boolean TSignificanceTest(Double coefficient, Sequence Y, Sequence X) {
        // Если количество событий будет менее 3,
        // то мы получим неположительное число
        // степеней свободы и будет брошено исключение
        if (Y.Length() < 3) return false;
        // Сначала расчитываем коэффициент B, для того,
        // чтобы восстановить полное уравнение регрессии
        Double B = CalculateBCoefficient(Y, X, coefficient);

        // Считаем остаточную дисперсию регрессии, то есть среднее
        // квадратичное отклонение прогнозов от реальных данных
        Double gammaReg = CalculateRegressionVarSqrt(coefficient, B, Y, X);

        // Считаем дисперсию Х, где Х - это массив числе от 0 до максимального индекса Y
        Double gammaX = CalculateVarSqrt(X);

        // На основании всего этого считаем статистику,
        // которая принадлежит распределению Стьюдента
        // с числом степеней свободы Y.length - 2
        Double T = Math.abs(coefficient) * (Math.sqrt(Y.Length() - 2) / gammaReg) * gammaX;

        // Считаем теоритическое значение статистики, то
        // биш 1 - Alpha / 2 квантиль распределения Стьюдента.
        Double Ttheoretical = Probability.studentTInverse(1 - Alpha / 2, Y.Length() - 2);

        // Если значение статичтики больше теоритического,
        // значит с вероятность 95% гипотеза H0 (Равенство
        // коэффициента регрессии 0) не верна.
        return T > Ttheoretical;
    }

    private static double ApplyRegressionFunction(Double A, Double B, Double x) {
        // Функция регрессии в общем виде с коэффициентами, принимаемыми на вход функции
        return A * x + B;
    }

    private static double CalculateRegressionVarSqrt(Double A, Double B, Sequence Y, Sequence X) {
        // Функция расчета корня от остаточной дисперсии,
        // то есть среднеквадратичного отклонения значений
        // функции регрессии от реальных данных
        // Сначала собираем результаты регрессии
        List<Double> y = new ArrayList<>();
        for (int i = 0; i < Y.Length(); i++) {
            y.add(ApplyRegressionFunction(A, B, X.get(i)));
        }
        // Считаем сумму квадратов отклонений
        Double sum = 0.;
        for (int i = 0; i < Y.Length(); i++) {
            sum += Math.pow(y.get(i) - Y.get(i), 2);
        }
        // Возвращаем корень из среднего отклонения
        return Math.sqrt(sum / Y.Length());
    }

    private static double CalculateVarSqrt(Sequence X) {
        // Функция во много аналогична функции выше,
        // однако она считает среднеквадратичное отклонение Х
        // Сначала считаем среднее по X
        Double mean = X.Mean();
        // Считаем сумму квадратов отклонений X от среднего
        Double deltaSum = 0.;
        for (int i = 0; i < X.Length(); i++) {
            deltaSum += Math.pow(mean - X.get(i), 2);
        }
        // Возвращаем среднеквадратичное отклонение
        return Math.sqrt(deltaSum / (X.Length() - 1));
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