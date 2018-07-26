package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cern.jet.stat.Probability;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSetType;

public class TKendallCorrelationComputer extends CorrelationComputer {

    public TKendallCorrelationComputer(double alpha) {
        Alpha = alpha;
    }

    @Override
    public Double ComputeCorrelation(DataSet data) {
        DataSize = data.Length();
        if (DataSize < 10) return null;
        if (data.getType() != DataSetType.Integer) {
            Correlation = null;
        }
        else {
            Correlation = CalculateKendallCorrelation(data);
        }
        return Correlation;
    }

    @Override
    public boolean IsCorrelationSignificant() {
        if (Correlation == null || DataSize < 10) return false;
        double D = (double)(4 * DataSize + 10) / (9 * DataSize * (DataSize - 1));
        double stat = Math.abs(Correlation / Math.sqrt(D));
        double p = Probability.normalInverse(1 - Alpha / 2); // Вычисление квантили стандартного нормального распределения
        return p <= stat;
    }
    // В данном методе происходит сравнение статистики с квантилью стандартного нормального распределения.
    // Говоря простым (относительно) языком, если значение статистики больше, чем 1 - alpha / 2 квантиль, то вероятность
    // получить такое же значение статистики при справедливости гипотезы о том, что переменные
    // независимы меньше чем alpha и мы можем отвергнуть эту гипотезу (привет от Сабуровой).

    private Double CalculateKendallCorrelation(DataSet<Integer> ds) {
        if (ds.Length() <= 4) return null;
        ds = OrderDataSetByFirstColumn(ds);
        List<Integer> X = ds.GetColumn(0);
        List<Integer> Y = ds.GetColumn(1);
        double C = 0; // concordant pars;
        double D = 0; // discordant pars;
        double n1 = 0;
        double n2 = 0;
        for (int i = 0; i < ds.Length() - 1; i++) {
            for (int j = i + 1; j < ds.Length(); j++) {
                // Датасет отсортирован по столбцу Х, то есть предполагается, что Х(i) <= X(j)
                if (Y.get(i) < Y.get(j) && X.get(i) < X.get(j)) C++;
                if (Y.get(i) > Y.get(j) && X.get(i) < X.get(j)) D++;
            }
        }
        // Считаем пары повторяющихся рангов для первого столбца
        for (int i = 0; i < ds.Length() - 1; i++) {
            // Длина группы повторяющихся рангов значально равна
            // единице, потому что каждый ранг эквивалентен самому себе
            int XgroupLength = 1;
            for (int j = i + 1; j < ds.Length(); j++) {
                // Если следующий элемент последовательности
                // эквивалентен предыдущему, увеличиваем длину
                // группы повторяющихся рангов
                if (X.get(i).equals(X.get(j))) XgroupLength++;
                // Так как массив отсортирован по неубыванию,
                // то все одинаковые элементы расположены в кучке.
                // Это значит, что если следующий элемент не равен
                // предыдущему, то и все, что идут за ним ему не равны.
                // Следовательно можно отказаться от их рассмотрения.
                if (!X.get(i).equals(X.get(j)) && XgroupLength > 1) {
                    // Считаем количество всех пар в последовательности длины XgroupLength
                    n1 += XgroupLength * (XgroupLength - 1) / 2;
                    // Перетаскиваем индекс j и прерываем цикл
                    i = j - 1;
                    break;
                }
            }
        }
        // Расчет этого показателя для второго столбца
        // в целом аналогичен таковому для первого, за
        // исключением того, что мы не можем прерывать
        // цикл, так как по второму столбцу датасет не
        // отсортирован. Идея для улучшения: отсортировать Y
        for (int i = 0; i < ds.Length() - 1; i++) {
            int YgroupLength = 1;
            for (int j = i + 1; j < ds.Length(); j++) {
                if (Y.get(i).equals(Y.get(j))) YgroupLength++;
                /*if (!Y.get(i).equals(Y.get(j)) && YgroupLength > 1) {
                    n2 += YgroupLength * (YgroupLength - 1) / 2;
                    i = j - 1;
                    break;
                }*/
            }
            n2 += YgroupLength * (YgroupLength - 1) / 2;
        }
        double n0 = ds.Length() * (ds.Length() - 1) / 2; // Общее количество пар
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

    private DataSet<Integer> OrderDataSetByFirstColumn(final DataSet<Integer> ds) {
        DataSet<Integer> newDataSet = new DataSet<>(DataSetType.Integer);
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < ds.Length(); i++) {
            indexes.add(i);
        }
        Collections.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return ds.GetColumn(0).get(integer).compareTo(ds.GetColumn(0).get(t1));
            }
        });
        for (int i: indexes) {
            newDataSet.AddRow(ds.GetRow(i).get(0), ds.GetRow(i).get(1));
        }
        return newDataSet;
    }
}
