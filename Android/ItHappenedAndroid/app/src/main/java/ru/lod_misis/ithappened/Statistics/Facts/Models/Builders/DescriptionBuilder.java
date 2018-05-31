package ru.lod_misis.ithappened.Statistics.Facts.Models.Builders;

import org.joda.time.Interval;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.lod_misis.ithappened.Statistics.Facts.Models.CorrelationModels.CorrelationData;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendDelta;

public class DescriptionBuilder {

    private static final String DateFormatPattern = "EEE, dd MMM yyyy HH:mm:ss";
    private static final Locale DateFormatLocalization = new Locale("ru");
    private static final SimpleDateFormat DateFormatter = new SimpleDateFormat(DateFormatPattern, DateFormatLocalization);

    public static String BuildCorrelationReport(CorrelationData Correlation,
                                         String FirstTrackingName,
                                         String FirstTrackingScaleName,
                                         String SecondTrackingName,
                                         String SecondTrackingScaleName) {
        String scaleTrackingRelation = String.format("значениями %s событий %s и значениями %s событий %s",
                FirstTrackingScaleName, FirstTrackingName, SecondTrackingScaleName, SecondTrackingName);
        String binaryTrackingRelation = String.format("фактами возникновения события %s и " +
                                "фактами возникновения события %s",
                                FirstTrackingName, SecondTrackingName);
        String ratingTrackingRelation = String.format("рейтингом событий %s и " +
                                "рейтингом событий %s",
                                FirstTrackingName, SecondTrackingName);
        String result = "";
        String orientation = "";
        if (Correlation.getScaleCorrelation() != null) {
            if (Correlation.getScaleCorrelation() > 0)
                orientation = "положительная";
            if (Correlation.getScaleCorrelation() < 0)
                orientation = "отрицательная";
            if (Correlation.getScaleCorrelation() == 0)
                result += "Между " + scaleTrackingRelation + " взаимосвязи не выявлено. ";
            if (Math.abs(Correlation.getScaleCorrelation()) > 0 &&
                    Math.abs(Correlation.getScaleCorrelation()) <= 0.3) {
                result += "Между " + scaleTrackingRelation + " выявлена очень слабая " + orientation + " взаимосвязь. ";
            }
            if (Math.abs(Correlation.getScaleCorrelation()) > 0.3 &&
                    Math.abs(Correlation.getScaleCorrelation()) <= 0.5) {
                result += "Между " + scaleTrackingRelation + " выявлена слабая " + orientation + " взаимосвязь. ";
            }
            if (Math.abs(Correlation.getScaleCorrelation()) > 0.5 &&
                    Math.abs(Correlation.getScaleCorrelation()) <= 0.7) {
                result += "Между " + scaleTrackingRelation + " выявлена средняя " + orientation + " взаимосвязь. ";
            }
            if (Math.abs(Correlation.getScaleCorrelation()) > 0.7 &&
                    Math.abs(Correlation.getScaleCorrelation()) <= 0.9) {
                result += "Между " + scaleTrackingRelation + " сильная " + orientation + " взаимосвязь. ";
            }
            if (Math.abs(Correlation.getScaleCorrelation()) > 0.9 &&
                    Math.abs(Correlation.getScaleCorrelation()) <= 1) {
                result += "Между " + scaleTrackingRelation + " выявлена очень сильная " + orientation + " взаимосвязь. ";
            }
        }

        if (Correlation.getBinaryCorrelation() != null) {
            if (Correlation.getBinaryCorrelation() > 0)
                orientation = "положительная";
            if (Correlation.getBinaryCorrelation() < 0)
                orientation = "отрицательная";
            if (Correlation.getBinaryCorrelation() == 0)
                result += "Между " + binaryTrackingRelation + " взаимосвязи не выявлено. ";
            if (Math.abs(Correlation.getBinaryCorrelation()) > 0 &&
                    Math.abs(Correlation.getBinaryCorrelation()) <= 0.3) {
                result += "Между " + binaryTrackingRelation + " выявлена очень слабая " + orientation + " взаимосвязь. ";
            }
            if (Math.abs(Correlation.getBinaryCorrelation()) > 0.3 &&
                    Math.abs(Correlation.getBinaryCorrelation()) <= 0.5) {
                result += "Между " + binaryTrackingRelation + " выявлена слабая " + orientation + " взаимосвязь. ";
            }
            if (Math.abs(Correlation.getBinaryCorrelation()) > 0.5 &&
                    Math.abs(Correlation.getBinaryCorrelation()) <= 0.7) {
                result += "Между " + binaryTrackingRelation + " выявлена средняя " + orientation + " взаимосвязь. ";
            }
            if (Math.abs(Correlation.getBinaryCorrelation()) > 0.7 &&
                    Math.abs(Correlation.getBinaryCorrelation()) <= 0.9) {
                result += "Между " + binaryTrackingRelation + " выявлена сильная " + orientation + " взаимосвязь. ";
            }
            if (Math.abs(Correlation.getBinaryCorrelation()) > 0.9 &&
                    Math.abs(Correlation.getBinaryCorrelation()) <= 1) {
                result += "Между " + binaryTrackingRelation + " выявлена очень сильная " + orientation + " взаимосвязь. ";
            }
        }

        if (Correlation.getMultinomialCorrelation() != null) {
            if (Correlation.getMultinomialCorrelation() > 0)
                orientation = "положительная";
            if (Correlation.getMultinomialCorrelation() < 0)
                orientation = "отрицательная";
            if (Correlation.getMultinomialCorrelation() == 0)
                result += "Между " + ratingTrackingRelation + " взаимосвязи не выявлено.";
            if (Math.abs(Correlation.getMultinomialCorrelation()) > 0 &&
                    Math.abs(Correlation.getMultinomialCorrelation()) <= 0.3) {
                result += "Между " + ratingTrackingRelation + " выявлена очень слабая " + orientation + " взаимосвязь.";
            }
            if (Math.abs(Correlation.getMultinomialCorrelation()) > 0.3 &&
                    Math.abs(Correlation.getMultinomialCorrelation()) <= 0.5) {
                result += "Между " + ratingTrackingRelation + " выявлена слабая " + orientation + " взаимосвязь.";
            }
            if (Math.abs(Correlation.getMultinomialCorrelation()) > 0.5 &&
                    Math.abs(Correlation.getMultinomialCorrelation()) <= 0.7) {
                result += "Между " + ratingTrackingRelation + " выявлена средняя " + orientation + " взаимосвязь.";
            }
            if (Math.abs(Correlation.getMultinomialCorrelation()) > 0.7 &&
                    Math.abs(Correlation.getMultinomialCorrelation()) <= 0.9) {
                result += "Между " + ratingTrackingRelation + " выявлена сильная " + orientation + " взаимосвязь.";
            }
            if (Math.abs(Correlation.getMultinomialCorrelation()) > 0.9 &&
                    Math.abs(Correlation.getMultinomialCorrelation()) <= 1) {
                result += "Между " + ratingTrackingRelation + " выявлена очень сильная " + orientation + " взаимосвязь.";
            }
        }
        return result;
    }
    // Рамки для текстового описания корреляции (сильная слабая и тд) взяты по шкале Чеддока.

    public static String BuildScaleTrendReport(TrendDelta delta, String trackingName, String scaleName) {
        String deltaDescription = delta.getAverangeDelta() > 0 ? "увеличилось" : "уменьшилось";
        String orientation = "";
        if (delta.getPoint().getAlphaCoefficient() > 0) {
            orientation = "положительный";
        }
        if (delta.getPoint().getAlphaCoefficient() < 0) {
            orientation = "отрицательный";
        }
        return "В значениях шкалы " + scaleName + " отслеживания " + trackingName + " выявлен "
                + orientation + " тренд. С момента " + DateFormatter.format(delta.getPoint().getPointEventDate())
                + " среднее значение шкалы " + deltaDescription + " на " + Math.abs(delta.getAverangeDelta());
    }

    public static String BuildRatingTrendReport(TrendDelta delta, String trackingName) {
        String deltaDescription = delta.getAverangeDelta() > 0 ? "увеличилось" : "уменьшилось";
        String orientation = "";
        if (delta.getPoint().getAlphaCoefficient() > 0) {
            orientation = "положительный";
        }
        if (delta.getPoint().getAlphaCoefficient() < 0) {
            orientation = "отрицательный";
        }
        return "В значениях рейтинга отслеживания " + trackingName + " выявлен "
                + orientation + " тренд. С " + DateFormatter.format(delta.getPoint().getPointEventDate())
                + " среднее значение шкалы " + deltaDescription + " на " + Math.abs(delta.getAverangeDelta());
    }

    public static String BuildLongestBreakDEscription(String trackingName,
                                                      Date firstEventDate,
                                                      Date secondEventDate) {
        return "Самый большой перерыв в " + trackingName + " произошёл с "
                + DateFormatter.format(firstEventDate) +
                " до " + DateFormatter.format(secondEventDate) + ". Длина перерыва в днях: " +
                (secondEventDate.getTime() - firstEventDate.getTime()) / (1000 * 60 * 60 * 24);
    }
}
