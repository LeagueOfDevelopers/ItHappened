package ru.lod_misis.ithappened.Statistics.Facts.Models.Builders;

import ru.lod_misis.ithappened.Statistics.Facts.Models.CorrelationModels.CorrelationData;

public class DescriptionBuilder {

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
}
