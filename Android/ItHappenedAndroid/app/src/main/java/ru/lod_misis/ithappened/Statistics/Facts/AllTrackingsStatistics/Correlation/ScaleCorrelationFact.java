package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation;

import java.util.List;

import cern.jet.stat.Probability;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;

public class ScaleCorrelationFact extends Fact {

    private static final double Alpha = 0.05; // Этим параметром можно регулировать строгость оценки
                                 // значимости корреляции (чем он выше, тем ниже порог
                                 // для признания корреляции значимой)
    private DataSet<Double> ScaleData;
    private String FirstTrackingName;
    private String FirstTrackingScaleName;
    private String SecondTrackingName;
    private String SecondTrackingScaleName;
    private Double Correlation;
    private static final int DaysToTrack = 1;

    public ScaleCorrelationFact(Tracking tracking1, Tracking tracking2) {
        FirstTrackingName = tracking1.GetTrackingName();
        FirstTrackingScaleName = tracking1.getScaleName();
        SecondTrackingName = tracking2.GetTrackingName();
        SecondTrackingScaleName = tracking2.getScaleName();
        ScaleData = DataSetBuilder.BuildDoubleDataSet(tracking1.GetEventCollection(),
                tracking2.GetEventCollection(), DaysToTrack);
    }

    public ScaleCorrelationFact(DataSet<Double> scale) {
        ScaleData = scale;
    }

    @Override
    public void calculateData() {
        Correlation = CalculateSpearmanCorrelation(ScaleData);
        calculatePriority();
    }

    @Override
    protected void calculatePriority() {
        if (Correlation != null)
            priority = 40.0 * Math.abs(Correlation);
        else
            priority = 0.0;
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildScaleCorrelationReport(Correlation,
                FirstTrackingName, FirstTrackingScaleName, SecondTrackingName, SecondTrackingScaleName);
    }

    public Double getCorrelation() {
        return Correlation;
    }

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
        if (Correlation == null) return false;
        if (ScaleData.Length() <= 1) return false;
        double T = Math.sqrt((1 - Math.pow(Correlation, 2)) / (ScaleData.Length() - 2));// Статистика
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
}
