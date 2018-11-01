package ru.lod_misis.ithappened.Domain.Statistics.Facts.AllTrackingsStatistics.Correlation;

import java.util.List;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.stat.Probability;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Builders.DataSetBuilder;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Collections.DataSet;

public class BinaryCorrelationFact extends Fact {

    private static final double Alpha = 0.05; // Этим параметром можно регулировать строгость оценки
    // значимости корреляции (чем он выше, тем ниже порог
    // для признания корреляции значимой)
    private DataSet<Integer> BinaryData;
    private String FirstTrackingName;
    private String SecondTrackingName;
    private Double Correlation;
    private static final int DaysToTrack = 1;

    public BinaryCorrelationFact(TrackingV1 tracking1, TrackingV1 tracking2) {
        FirstTrackingName = tracking1.GetTrackingName();
        SecondTrackingName = tracking2.GetTrackingName();
        BinaryData = DataSetBuilder.BuildBooleanDataset(tracking1.getEventV1Collection(),
                tracking2.getEventV1Collection(), DaysToTrack);
    }

    public BinaryCorrelationFact(DataSet<Integer> binaryData) {
        BinaryData = binaryData;
    }

    @Override
    public void calculateData() {
        Correlation = CalculateMatthewsCorrelation(BinaryData);
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
        return DescriptionBuilder.BuildBinaryCorrelationReport(Correlation,
                FirstTrackingName, SecondTrackingName);
    }

    public Double getCorrelation() {
        return Correlation;
    }

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

    public boolean IsBoolCorrSignificant() {
        if (Correlation == null) return false;
        if (BinaryData.Length() < 40) return false;
        double X = BinaryData.Length() * Math.pow(Correlation, 2);
        double p = Probability.chiSquareComplemented(1, X); // интеграл от X до плюс бесконечности
        return p < Alpha;
    }
    // Данная функция проверяет значимость корреляции между бинарными признаками
    // (alpha - порог уровня значимости р. Если р > alpha, то корреляция незначима)
    // Метод выведен в паблик для использования в функции применимости.

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
