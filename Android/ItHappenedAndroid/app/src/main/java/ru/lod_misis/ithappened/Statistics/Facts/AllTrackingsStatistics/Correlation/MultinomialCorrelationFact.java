package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation;

import java.util.Comparator;
import java.util.List;

import cern.jet.stat.Probability;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;

public class MultinomialCorrelationFact extends Fact {

    private static final double Alpha = 0.05; // Этим параметром можно регулировать строгость оценки
    // значимости корреляции (чем он выше, тем ниже порог
    // для признания корреляции значимой)
    private DataSet<Integer> MultinomialData;
    private String FirstTrackingName;
    private String SecondTrackingName;
    private static final int DaysToTrack = 1;
    private Double Correlation;

    public MultinomialCorrelationFact(Tracking tracking1, Tracking tracking2) {
        FirstTrackingName = tracking1.GetTrackingName();
        SecondTrackingName = tracking2.GetTrackingName();
        MultinomialData = DataSetBuilder.BuildMultinomialDataset(tracking1.GetEventCollection(),
                tracking2.GetEventCollection(), DaysToTrack);
    }

    public MultinomialCorrelationFact(DataSet<Integer> mult) {
        MultinomialData = mult;
    }

    @Override
    public void calculateData() {
        Correlation = CalculateKendallCorrelation(MultinomialData);
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
        return DescriptionBuilder.BuildMultinomialCorrelationReport(Correlation,
                FirstTrackingName, SecondTrackingName);
    }

    public Double getCorrelation() {
        return Correlation;
    }

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
        if (Correlation == null || MultinomialData.Length() < 10) return false;
        double D = (double)(4 * MultinomialData.Length() + 10) / (9 * MultinomialData.Length() * (MultinomialData.Length() - 1));
        double stat = Math.abs(Correlation / Math.sqrt(D));
        double p = Probability.normalInverse(1 - Alpha / 2); // Вычисление квантили стандартного нормального распределения
        return p <= stat;
    }
    // В данном методе происходит сравнение статистики с квантилью стандартного нормального распределения.
    // Говоря простым языком, если значение статистики больше, чем 1 - alpha / 2 квантиль, то вероятность
    // получить такое же значение статистики при справедливости гипотезы о том, что переменные
    // независимы меньше чем alpha и мы можем отвергнуть эту гипотезу (привет от Сабуровой).
}
