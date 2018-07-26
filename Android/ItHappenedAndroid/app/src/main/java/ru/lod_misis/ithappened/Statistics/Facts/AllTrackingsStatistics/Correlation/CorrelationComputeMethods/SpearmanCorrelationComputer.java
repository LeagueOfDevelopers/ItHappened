package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods;

import java.util.List;

import cern.jet.stat.Probability;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSetType;

public class SpearmanCorrelationComputer extends CorrelationComputer {

    public SpearmanCorrelationComputer(Double alpha) {
        Alpha = alpha;
    }

    @Override
    public Double ComputeCorrelation(DataSet data) {
        DataSize = data.Length();
        if (data.getType() != DataSetType.Double) {
            Correlation = null;
        }
        else {
            Correlation = CalculateSpearmanCorrelation(data);
        }
        return Correlation;
    }

    @Override
    public boolean IsCorrelationSignificant() {
        if (Correlation == null) return false;
        if (DataSize <= 1) return false;
        double T = Math.sqrt((1 - Math.pow(Correlation, 2)) / (DataSize - 2));// Статистика
        double p = Probability.studentTInverse(Alpha, DataSize - 2);
        return T < p; // вероятность получить значение статистики меньше Alpha
    }
    // Данная функция проверяет значимость корреляции между непрерывными признаками
    // Сначала вычисляем значение специальным образом нормированной статистики (T) и
    // затем сравниваем ее со значением точки (p), которая больше случайно взятого
    // значения с вероятностью в 1 - Alpha / 2. Если значение статистики выходит за эту точку,
    // значит получить такое же значение статистики при справедливости нулевой
    // гипотезы маловероятно и ее можно отвергнуть.
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
}