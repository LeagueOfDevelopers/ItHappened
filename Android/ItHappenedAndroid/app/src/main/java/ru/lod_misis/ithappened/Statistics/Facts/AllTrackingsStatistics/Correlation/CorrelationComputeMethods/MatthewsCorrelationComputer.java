package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.Correlation.CorrelationComputeMethods;

import java.util.List;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.stat.Probability;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSetType;

public class MatthewsCorrelationComputer extends CorrelationComputer {

    public MatthewsCorrelationComputer(double alpha) {
        Alpha = alpha;
    }

    @Override
    public Double ComputeCorrelation(DataSet data) {
        DataSize = data.Length();
        // Данный метод требует датасет с типа Integer с двумя значеними: 0 - false, 1 - true.
        // Датасет с таким типом дает метод BuildBooleanDataSet
        if (data.getType() != DataSetType.Boolean) {
            Correlation = null;
        }
        else {
            Correlation = CalculateMatthewsCorrelation(data);
        }
        return Correlation;
    }

    @Override
    public boolean IsCorrelationSignificant() {
        if (Correlation == null) return false;
        if (DataSize < 40) return false;
        double X = DataSize * Math.pow(Correlation, 2);
        double p = Probability.chiSquareComplemented(1, X); // интеграл от X до плюс бесконечности
        return p < Alpha;
    }
    // Данная функция проверяет значимость корреляции между бинарными признаками
    // (alpha - порог уровня значимости р. Если р > alpha, то корреляция незначима)
    // Метод выведен в паблик для использования в функции применимости.

    private Double CalculateMatthewsCorrelation(DataSet<Integer> ds) {
        if (ds.Length() < 40) return null;
        DoubleMatrix2D matrix = BuildMultinomialMatrix(ds);
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

    private DoubleMatrix2D BuildMultinomialMatrix(DataSet<Integer> ds) {
        if (!ds.IsMultimonial()) return null;
        DoubleMatrix2D matrix = new DenseDoubleMatrix2D(2, 2);
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
}
