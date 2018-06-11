package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import java.util.Comparator;
import java.util.List;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.CorrelationModels.CorrelationData;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;
import cern.jet.stat.Probability;
import cern.colt.matrix.DoubleMatrix2D;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;

public class CorrelationFact extends Fact {

    private CorrelationData Correlation;
    private double Alpha = 0.05; // Этим параметром можно регулировать строгость оценки
                                 // значимости корреляции (чем он выше, тем ниже порог
                                 // для признания корреляции значимой)
    private DataSet<Integer> BinaryData;
    private DataSet<Double> ScaleData;
    private DataSet<Integer> MultinomialData;
    private String FirstTrackingName;
    private String FirstTrackingScaleName;
    private String SecondTrackingName;
    private String SecondTrackingScaleName;
    private final int DaysToTrack = 1;

    public CorrelationFact(Tracking tracking1, Tracking tracking2) {
        FirstTrackingName = tracking1.GetTrackingName();
        FirstTrackingScaleName = tracking1.getScaleName();
        SecondTrackingName = tracking2.GetTrackingName();
        SecondTrackingScaleName = tracking2.getScaleName();
        BinaryData = DataSetBuilder.BuildBooleanDataset(
                tracking1.GetEventCollection(), tracking2.GetEventCollection(), DaysToTrack);
        ScaleData = DataSetBuilder.BuildDoubleDataSet(
                tracking1.GetEventCollection(), tracking2.GetEventCollection(), DaysToTrack);
        MultinomialData = DataSetBuilder.BuildMultinomialDataset(
                tracking1.GetEventCollection(), tracking2.GetEventCollection(), DaysToTrack);
    }

    public CorrelationFact(DataSet<Integer> binaryData,
                           DataSet<Double> scaleData,
                           DataSet<Integer> multinomDataset) {
        BinaryData = binaryData;
        ScaleData = scaleData;
        MultinomialData = multinomDataset;
    }

    @Override
    public void calculateData() {
        Double binaryCorr = CalculateMatthewsCorrelation(BinaryData); // Корреляция между фактами возникновения
        Double scaleCorr = CalculateSpearmanCorrelation(ScaleData); // Корреляция между значениями шкалы
        Double multCorr = CalculateKendallCorrelation(MultinomialData); // Корреляция между рейтингом
        Correlation = new CorrelationData(binaryCorr, scaleCorr, multCorr);
    }

    @Override
    protected void calculatePriority() {
        priority = 40.0;
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildCorrelationReport(Correlation, FirstTrackingName,
                FirstTrackingScaleName, SecondTrackingName, SecondTrackingScaleName);
    }

    public CorrelationData getCorrelation() {
        return Correlation;
    }

    private Double CalculateMatthewsCorrelation(DataSet<Integer> ds) {
        if (ds.Length() < 40) return null;
        DoubleMatrix2D matrix = BuildMultinomialMatrix(ds, 2, 2);
        double a = matrix.get(1, 1);
        double b = matrix.get(0, 1);
        double c = matrix.get(1, 0);
        double d = matrix.get(0, 0);
        int N = ds.Length();

        if (a + b + c + d != N) { throw new VerifyError(); }

        return (a * d - b * c) / Math.sqrt((a + b) * (a + c) * (d + b) * (d + c));
    }
    // Корреляция между бинарными пременными. Бросает null, если хоть в одной выборке < 40 обьектов.
    // Считается так: мы строим таблицу 2 на 2, где на главной диагонали стоят числа обьектов
    // в которых оба собыия произошли или оба не произошли, а на побочной стоят количества записей,
    // в которых одного из событий не было, при наличии второго (не важно как они будут
    // распологаться В ПРЕДЕЛАХ СВОИХ ДИАГОНАЛЕЙ!!!!!!! (а и d или b и c можно менять местами, но не a и c))

    public boolean IsBoolCorrSignificant() {
        if (Correlation.getBinaryCorrelation() == null) return false;
        if (BinaryData.Length() < 40) return false;
        double X = BinaryData.Length() * Math.pow(Correlation.getBinaryCorrelation(), 2);
        double p = Probability.chiSquareComplemented(1, X); // интеграл от X до плюс бесконечности
        return p < Alpha;
    }
    // Данная функция проверяет значимость корреляции между бинарными признаками
    // (alpha - порог уровня значимости р. Если р > alpha, то корреляция незначима)
    // Метод выведен в паблик для использования в функции применимости.

    private Double CalculateSpearmanCorrelation(DataSet<Double> ds) {
        if (ds.Length() <= 4) return null;
        DataSet<Double> ranks = DataSetBuilder.ConvertToRanks(ds);
        List<Double> ranks1 = ranks.GetColumn(0);
        List<Double> ranks2 = ranks.GetColumn(1);
        double corrCoef = 0.0;
        int n = ranks1.size();
        for (int i = 0; i < n; i++) {
            corrCoef += (ranks1.get(i) - (n + 1) / 2.0) * (ranks2.get(i) - (n + 1) / 2.0);
        }
        return (corrCoef * 12) / (Math.pow(n, 3) - n);
    }
    // Данная функция считает корреляцию между непрерывными величинами.
    // Сначала величины преобразовываются в массив рангов. Ранг числа в
    // выборке - это его индекс в отсортированном массиве, НО если таких
    // чисел несколько, то ранг каждого из них это среднее арифметическое
    // от их индексов в отсортированном массиве. Затем по формуле
    // вычисляется коэффициент корреляции между рангами.
    // Используется коэффициент ранговой корреляции Спирмена

    public boolean IsDoubleCorrSignificant() {
        if (Correlation.getScaleCorrelation() == null) return false;
        if (ScaleData.Length() <= 1) return false;
        double T = Math.sqrt((1 - Math.pow(Correlation.getScaleCorrelation(), 2)) / (ScaleData.Length() - 2));
        // Статистика
        double p = Probability.studentTInverse(Alpha, ScaleData.Length() - 2);
        return T < p; // вероятность получить значение статистики меньше Alpha
    }
    // Данная функция проверяет значимость корреляции между непрерывными признаками
    // Сначала вычисляем значение специальным образом нормированной статистики (T) и
    // затем сравниваем ее со значением точки (p), которая больше случайно взятого
    // значения с вероятностью в 1 - Alpha / 2. Если значение статистики выходит за эту точку,
    // значит получить такое же значение статистики при справедливости нулевой
    // гипотезы маловероятно и ее можно отвергнуть.
    // Метод выведен в паблик для использования в функции применимости.

    private DoubleMatrix2D BuildMultinomialMatrix(DataSet<Integer> ds, int rowCount, int colCount) {
        if (!ds.IsMultimonial()) return null;
        DoubleMatrix2D matrix = new DenseDoubleMatrix2D(rowCount, colCount);
        for (int j = 0; j < ds.Length(); j++) {
            List<Integer> row = ds.GetRow(j);
            matrix.set(row.get(0), row.get(1), matrix.get(row.get(0), row.get(1)) + 1);
        }
        return matrix;
    }
    // Этот метод преобразует мультиномиальный датасет в матрицу,
    // по которой можно считать корреляцию. Для не мультиномиальных
    // датасетов возвращает null. (Бинарный - это тоже мультиномиальный)
    // ВНИМАНИЕ!!! МУЛЬТИНОМИАЛЬНОСТЬ НЕ КОНТРОЛИРУЕТСЯ НИЧЕМ КРОМЕ РАЗРАБОТЧИКА.

    private Double CalculateKendallCorrelation(DataSet<Integer> ds) {
        if (ds.Length() <= 4) return null;
        ds.SortBy(new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> integers, List<Integer> t1) {
                return integers.get(0).compareTo(t1.get(0));
            }
        });
        List<Integer> X = ds.GetColumn(0);
        List<Integer> Y = ds.GetColumn(1);
        double C = 0; // concordant pars;
        double D = 0; // discordant pars;
        double n0 = ds.Length() * (ds.Length() - 1) / 2;
        double n1 = 0;
        double n2 = 0;
        for (int i = 0; i < ds.Length() - 1; i++) {
            for (int j = i + 1; j < ds.Length(); j++) {
                // Датасет отсортирован по столбцу Х, то есть предполагается, что Х(i) <= X(j)
                if (Y.get(i) < Y.get(j) && X.get(i) < X.get(j)) C++;
                if (Y.get(i) > Y.get(j) && X.get(i) < X.get(j)) D++;
            }
        }
        for (int i = 0; i < ds.Length() - 1; i++) {
            int XgroupLength = 1;
            for (int j = i + 1; j < ds.Length(); j++) {
                if (X.get(i).equals(X.get(j))) XgroupLength++;
                if (!X.get(i).equals(X.get(j)) && XgroupLength > 1) {
                    n1 += XgroupLength * (XgroupLength - 1) / 2;
                    i = j - 1;
                    break;
                }
            }
        }
        for (int i = 0; i < ds.Length() - 1; i++) {
            int YgroupLength = 1;
            for (int j = i + 1; j < ds.Length(); j++) {
                if (Y.get(i).equals(Y.get(j))) YgroupLength++;
                if (!Y.get(i).equals(Y.get(j)) && YgroupLength > 1) {
                    n2 += YgroupLength * (YgroupLength - 1) / 2;
                    i = j - 1;
                    break;
                }
            }
        }

        return (C - D) / Math.sqrt((n0 - n1) * (n0 - n2));
    }
    // Применяется коэффициент корреляции tau-b Кендала. Сначала считается общее
    // колличество скоординированных пар C (пар (X[i], Y[i]) для которых выполняется
    // X[i] < X[i+1] && Y[i] < Y[i+1]. Датасет перед этим сортируется по первой колонке,
    // так что вариант X[i] > X[i+1] в проверке не нуждается в виду невозможности.)
    // и нескоординированных пар D (пар, для которых выполняется X[i] < X[i+1] && Y[i] > Y[i+1]).
    // Затем считаем величину (n1 и n2 в статье на википедии) и во втором столбце. Я это делал так:
    // сначала проходимся по первому столбцу, вычисляем длину каждой серии повторяющихся рангов ti и
    // прибавляем к n1 величину ti * (ti - 1) / 2. Затем то же самое со вторым столбцом, только прибавляем к n2
    // И наконец все это дело подставляется в формулу.
    // Wikipedia: https://en.wikipedia.org/wiki/Kendall_rank_correlation_coefficient
    // Example: https://stats.stackexchange.com/questions/261206/kendall-s-tau-b-version-calculation-steps-with-tied-ranks

    public boolean IsMultinomialCorrSignificant() {
        if (Correlation.getMultinomialCorrelation() == null || MultinomialData.Length() < 10) return false;
        double D = (double)(4 * MultinomialData.Length() + 10) / (9 * MultinomialData.Length() * (MultinomialData.Length() - 1));
        double stat = Math.abs(Correlation.getMultinomialCorrelation() / Math.sqrt(D));
        double p = Probability.normalInverse(1 - Alpha / 2); // Вычисление квантили стандартного нормального распределения
        return p <= stat;
    }
    // В данном методе происходит сравнение статистики с квантилью стандартного нормального распределения.
    // Говоря простым языком, если значение статистики больше, чем 1 - alpha / 2 квантиль, то вероятность
    // получить такое же значение статистики при справедливости гипотезы о том, что переменные
    // независимы меньше чем alpha и мы можем отвергнуть эту гипотезу (привет от Сабуровой).
}
