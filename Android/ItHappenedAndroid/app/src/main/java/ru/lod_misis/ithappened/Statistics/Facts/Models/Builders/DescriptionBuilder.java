package ru.lod_misis.ithappened.Statistics.Facts.Models.Builders;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.lod_misis.ithappened.Statistics.Facts.Models.TimeSpanEventData;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingPoint;

public class DescriptionBuilder {

    private static final Locale DateFormatLocalization = new Locale("ru");

    private static final String BoolCorrReportFormat =
            "C <b>%s</b> вероятностью при увеличении числа событий <b>%s</b> происходит <b>%s</b> числа событий <b>%s</b>. ";

    private static final String ScaleCorrReportFormat =
            "C <b>%s</b> вероятностью при увеличении количества <b>%s</b> в событии <b>%s</b> происходит <b>%s</b> количества <b>%s</b> в событии <b>%s</b>. ";

    private static final String RatingCorrReportFormat =
            "C <b>%s</b> вероятностью при увеличении рейтинга события <b>%s</b> происходит <b>%s</b> рейтинга события <b>%s</b>. ";

    private static final String ScaleTrendReportFormat =
            "С момента <b>%s</b> среднее значение шкалы <b>%s</b> cобытия <b>%s</b> <b>%s</b> на <b>%s</b>.";

    private static final String RatingTrendReportFormat =
            "С момента <b>%s</b> среднее значение рейтинга события <b>%s</b> <b>%s</b> на <b>%s</b>.";

    private static final String FreqTrendReportFormat =
            "Событие <b>%s</b> происходит <b>%s</b>: за последние <b>%s</b> - <b>%s</b> ";

    private static final String LongestBreakReportFormat =
            "Самый большой перерыв в <b>%s</b> произошёл с <b>%s</b> до <b>%s</b>. Длина перерыва в днях: <b>%d</b>.";

    private static final String LargestEventCountReportFormat =
            "Самый насыщенный событиями день был <b>%s</b>. Тогда произошло <b>%s</b> ";

    private static final String LargestEventCountWeekReportFormat =
            "Самая насыщенная событиями неделя была с <b>%s</b> до <b>%s</b>. В течении этой недели произошло <b>%s</b> ";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat
            ("dd.MM.yyyy HH:mm", DateFormatLocalization);

    private static DecimalFormat format = new DecimalFormat("#.##");

    public static String BuildBinaryCorrelationReport(Double corr,
                                                      String firstTrackingName,
                                                      String secondTrackingName) {

        if (corr != null) {
            String orientation = "";
            if (corr > 0)
                orientation = "увеличение";
            if (corr < 0)
                orientation = "уменьшение";
            if (corr == 0)
                return String.format("Между %s и %s взаимосвязи не выявлено. ",
                        firstTrackingName, secondTrackingName);
            String probDesc = "";
            if (Math.abs(corr) > 0 &&
                    Math.abs(corr) <= 0.3) {
                probDesc = "очень маленькой";
            }
            if (Math.abs(corr) > 0.3 &&
                    Math.abs(corr) <= 0.5) {
                probDesc = "маленькой";
            }
            if (Math.abs(corr) > 0.5 &&
                    Math.abs(corr) <= 0.7) {
                probDesc = "средней";
            }
            if (Math.abs(corr) > 0.7 &&
                    Math.abs(corr) <= 0.9) {
                probDesc = "большой";
            }
            if (Math.abs(corr) > 0.9 &&
                    Math.abs(corr) <= 1) {
                probDesc = "очень большой";
            }
            return String.format(BoolCorrReportFormat, probDesc,
                    firstTrackingName, orientation, secondTrackingName).trim();
        }
        return "";
    }
    // Рамки для текстового описания корреляции (сильная слабая и тд) взяты по шкале Чеддока.

    public static String BuildScaleCorrelationReport(Double corr,
                                                     String firstTrackingName,
                                                     String firstTrackingScaleName,
                                                     String secondTrackingName,
                                                     String secondTrackingScaleName) {

        if (corr != null) {
            String orientation = "";
            if (corr > 0)
                orientation = "увеличение";
            if (corr < 0)
                orientation = "уменьшение";
            if (corr == 0)
                return String.format("Между событиями %s и %s взаимосвязи не выявлено. ",
                        firstTrackingName, secondTrackingName);
            String probDesc = "";
            if (Math.abs(corr) > 0 &&
                    Math.abs(corr) <= 0.3) {
                probDesc = "очень маленькой";
            }
            if (Math.abs(corr) > 0.3 &&
                    Math.abs(corr) <= 0.5) {
                probDesc = "маленькой";
            }
            if (Math.abs(corr) > 0.5 &&
                    Math.abs(corr) <= 0.7) {
                probDesc = "средней";
            }
            if (Math.abs(corr) > 0.7 &&
                    Math.abs(corr) <= 0.9) {
                probDesc = "большой";
            }
            if (Math.abs(corr) > 0.9 &&
                    Math.abs(corr) <= 1) {
                probDesc = "очень большой";
            }
            return String.format(ScaleCorrReportFormat, probDesc,
                    firstTrackingScaleName, firstTrackingName, orientation,
                    secondTrackingScaleName, secondTrackingName).trim();
        }
        return "";
    }
    // Рамки для текстового описания корреляции (сильная слабая и тд) взяты по шкале Чеддока.

    public static String BuildMultinomialCorrelationReport(Double corr,
                                                           String firstTrackingName,
                                                           String secondTrackingName) {

        if (corr != null) {
            String orientation = "";
            if (corr > 0)
                orientation = "увеличение";
            if (corr < 0)
                orientation = "уменьшение";
            if (corr == 0)
                return String.format("Между %s и %s взаимосвязи не выявлено.",
                        firstTrackingName, secondTrackingName);
            String probDesc = "";
            if (Math.abs(corr) > 0 &&
                    Math.abs(corr) <= 0.3) {
                probDesc = "очень маленькой";
            }
            if (Math.abs(corr) > 0.3 &&
                    Math.abs(corr) <= 0.5) {
                probDesc = "маленькой";
            }
            if (Math.abs(corr) > 0.5 &&
                    Math.abs(corr) <= 0.7) {
                probDesc = "средней";
            }
            if (Math.abs(corr) > 0.7 &&
                    Math.abs(corr) <= 0.9) {
                probDesc = "большой";
            }
            if (Math.abs(corr) > 0.9 &&
                    Math.abs(corr) <= 1) {
                probDesc = "очень большой";
            }
            return String.format(RatingCorrReportFormat, probDesc,
                    firstTrackingName, orientation, secondTrackingName).trim();
        }
        return "";
    }
    // Рамки для текстового описания корреляции (сильная слабая и тд) взяты по шкале Чеддока.

    public static String BuildScaleTrendReport(TrendChangingPoint delta, Double newAverange, String trackingName, String scaleName) {
        String deltaDescription = newAverange - delta.getAverageValue() > 0 ? "увеличилось" : "уменьшилось";
        DateTime date = new DateTime(delta.getPointEventDate());
        return String.format(ScaleTrendReportFormat,
                DateDescription(date), scaleName, trackingName,
                deltaDescription, format.format(Math.abs(delta.getAverageValue() - newAverange)));
    }

    public static String BuildRatingTrendReport(TrendChangingPoint delta, Double newAverange, String trackingName) {
        String deltaDescription = newAverange - delta.getAverageValue() > 0 ? "увеличилось" : "уменьшилось";
        DateTime date = new DateTime(delta.getPointEventDate());
        return String.format(RatingTrendReportFormat,
                DateDescription(date), trackingName,
                deltaDescription, format.format(Math.abs(delta.getAverageValue() - newAverange)));
    }

    public static String BuildFrequencyTrendReport(TrendChangingPoint delta, Double newAverage, String trackingName, Interval period, int count) {
        String orientation = newAverage - delta.getAverageValue() > 0 ? "чаще" : "реже";
        String duration = IntervalDescription(period);
        return String.format(FreqTrendReportFormat + TimesCountDescription(count), trackingName, orientation, duration, count);
    }

    public static String BuildLongestBreakDescription(String trackingName,
                                                      Date firstEventDate,
                                                      Date secondEventDate) {
        DateTime begin = new DateTime(firstEventDate);
        DateTime end = new DateTime(secondEventDate);
        return String.format(DateFormatLocalization, LongestBreakReportFormat,
                trackingName,
                dateFormat.format(begin),
                dateFormat.format(end),
                (secondEventDate.getTime() - firstEventDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String LargestEventCountDayDescription(TimeSpanEventData data) {
        String eventCountDescr = EventCountDescription(data.getEventCount());
        String pattern = LargestEventCountReportFormat + eventCountDescr;
        return String.format(pattern, dateFormat.format(data.getDate()), data.getEventCount()).trim();
    }

    public static String LargestEventCountWeekDescription(TimeSpanEventData data) {
        String eventCountDescr = EventCountDescription(data.getEventCount());
        String pattern = LargestEventCountWeekReportFormat + eventCountDescr;
        DateTime leftWeekBorder = data.getLeftWeekBorder();
        DateTime rightWeekBorder = data.getRightWeekBorder();
        String leftBorderDescription = dateFormat.format(leftWeekBorder);
        String rightBorderDescription = dateFormat.format(rightWeekBorder);
        return String.format(pattern, leftBorderDescription,
                rightBorderDescription, data.getEventCount()).trim();
    }

    private static String TimesCountDescription(int times) {
        String timesDescr = "";
        int lastEventCountDigit = times % 10;
        if (lastEventCountDigit > 1 && lastEventCountDigit < 5) {
            timesDescr = "раза.";
        }
        boolean additionalCondition = times % 100 > 10 && times % 100 < 20;
        if (lastEventCountDigit > 4 || lastEventCountDigit <= 1 || additionalCondition) {
            timesDescr = "раз.";
        }
        return timesDescr;
    }

    private static String EventCountDescription(int eventCount) {
        String eventCountDescr = "";
        int lastEventCountDigit = eventCount % 10;
        if (lastEventCountDigit == 1) {
            eventCountDescr = "событие.";
        }
        if (lastEventCountDigit > 1 && lastEventCountDigit < 5) {
            eventCountDescr = "события.";
        }
        boolean additionalCondition = eventCount % 100 > 10 && eventCount % 100 < 20;
        if (lastEventCountDigit > 4 || lastEventCountDigit == 0 || additionalCondition) {
            eventCountDescr = "событий.";
        }
        return eventCountDescr;
    }

    private static String IntervalDescription(Interval interval) {
        String duration = "";
        int days = interval.toDuration().toStandardDays().getDays();
        int hours = interval.toDuration().toStandardHours().getHours() - days * 24;
        int minutes = interval.toDuration().toStandardMinutes().getMinutes() - days * 24 * 60 - hours * 60;
        if (days > 0) {
            int lastDigitD = days % 10;
            // Данное условие отвечает за описание чисел с 10 до 20: например 11 дней но 21 день
            boolean condition = days % 100 > 10 && days % 100 < 20;
            if (lastDigitD > 4 || lastDigitD == 0 || condition) {
                duration += String.format("%s дней ", days);
            }
            if (lastDigitD > 1 && lastDigitD <= 4 && !condition) {
                duration += String.format("%s дня ", days);
            }
            if (lastDigitD == 1 && !condition) {
                duration += String.format("%s день ", days);
            }
        }
        if (hours > 0) {
            int lastDigitH = hours % 10;
            // Данное условие отвечает за описание числе с 10 до 20: например 11 дней но 21 день
            boolean condition = hours % 100 > 10 && hours % 100 < 20;
            if (lastDigitH > 4 || lastDigitH == 0 || condition) {
                duration += String.format("%s часов ", hours);
            }
            if (lastDigitH > 1 && lastDigitH <= 4 && !condition) {
                duration += String.format("%s часа ", hours);
            }
            if (lastDigitH == 1 && !condition) {
                duration += String.format("%s час ", hours);
            }
        }
        if (minutes > 0) {
            int lastDigitM = minutes % 10;
            // Данное условие отвечает за описание числе с 10 до 20: например 11 дней но 21 день
            boolean condition = minutes % 100 > 10 && minutes % 100 < 20;
            if (lastDigitM > 4 || lastDigitM == 0 || condition) {
                duration += String.format("%s минут ", minutes);
            }
            if (lastDigitM > 1 && lastDigitM <= 4 && !condition) {
                duration += String.format("%s минуты ", minutes);
            }
            if (lastDigitM == 1 && !condition) {
                duration += String.format("%s минуту ", minutes);
            }
        }
        return duration.trim();
    }

    private static String DateDescription(DateTime date) {
        int year = date.getYear();
        int month = date.getMonthOfYear();
        int day = date.getDayOfMonth();
        String monthDescr = "";
        switch (month) {
            case 1:
                monthDescr = "января";
                break;
            case 2:
                monthDescr = "февраля";
                break;
            case 3:
                monthDescr = "марта";
                break;
            case 4:
                monthDescr = "апреля";
                break;
            case 5:
                monthDescr = "мая";
                break;
            case 6:
                monthDescr = "июня";
                break;
            case 7:
                monthDescr = "июля";
                break;
            case 8:
                monthDescr = "августа";
                break;
            case 9:
                monthDescr = "сентября";
                break;
            case 10:
                monthDescr = "октября";
                break;
            case 11:
                monthDescr = "ноября";
                break;
            case 12:
                monthDescr = "декабря";
        }
        return String.format("%s %s %s года", day, monthDescr, year);
    }
}
