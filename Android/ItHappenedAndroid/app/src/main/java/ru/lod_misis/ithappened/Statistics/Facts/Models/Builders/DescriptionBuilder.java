package ru.lod_misis.ithappened.Statistics.Facts.Models.Builders;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.Minutes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.lod_misis.ithappened.Statistics.Facts.Models.TimeSpanEventData;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Trends.TrendChangingPoint;

public class DescriptionBuilder {

    private static final String DateFormatPattern = "EEE, dd MMM yyyy HH:mm:ss";
    private static final Locale DateFormatLocalization = new Locale("ru");
    private static final SimpleDateFormat DateFormatter = new SimpleDateFormat(DateFormatPattern, DateFormatLocalization);

    private static final String BoolCorrReportFormat =
            "C %s вероятностью при увеличении числа событий %s происходит %s числа событий %s. ";

    private static final String ScaleCorrReportFormat =
            "C %s вероятностью при увеличении количества %s в событии %s происходит %s количества %s в событии %s. ";

    private static final String RatingCorrReportFormat =
            "C %s вероятностью при увеличении рейтинга события %s происходит %s рейтинга события %s. ";

    private static final String ScaleTrendReportFormat =
            "С момента %s среднее значение шкалы '%s' cобытия '%s' %s на %s.";

    private static final String RatingTrendReportFormat =
            "С момента %s среднее значение рейтинга события '%s' %s на %s.";

    private static final String FreqTrendReportFormat =
            "Событие %s происходит %s: за последние %s - %s раз.";

    private static final String LongestBreakReportFormat =
            "Самый большой перерыв в %s произошёл с %s до %s. Длина перерыва в днях: %d.";

    private static final String LargestEventCountReportFormat =
            "Самый насыщенный событиями день был %s. Тогда произошло %s ";

    private static final String LargestEventCountWeekReportFormat =
            "Самая насыщенная событиями неделя была с %s до %s. В течении этой недели произошло %s ";

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
        String deltaDescription = newAverange - delta.getAverangeValue() > 0 ? "увеличилось" : "уменьшилось";
        return String.format(ScaleTrendReportFormat,
                DateFormatter.format(delta.getPointEventDate()), scaleName, trackingName,
                deltaDescription, Math.abs(delta.getAverangeValue() - newAverange));
    }

    public static String BuildRatingTrendReport(TrendChangingPoint delta, Double newAverange, String trackingName) {
        String deltaDescription = newAverange - delta.getAverangeValue() > 0 ? "увеличилось" : "уменьшилось";
        return String.format(RatingTrendReportFormat,
                DateFormatter.format(delta.getPointEventDate()), trackingName,
                deltaDescription, Math.abs(delta.getAverangeValue() - newAverange));
    }

    public static String BuildFrequencyTrendReport(TrendChangingPoint delta, Double newAverange, String trackingName, Interval period, int count) {
        String orientation = newAverange - delta.getAverangeValue() > 0 ? "чаще" : "реже";
        String duration = IntervalDescription(period);
        return String.format(FreqTrendReportFormat, trackingName, orientation, duration, count);
    }

    public static String BuildLongestBreakDEscription(String trackingName,
                                                      Date firstEventDate,
                                                      Date secondEventDate) {
        return String.format(new Locale("ru"), LongestBreakReportFormat,
                trackingName, DateFormatter.format(firstEventDate), DateFormatter.format(secondEventDate),
                (secondEventDate.getTime() - firstEventDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String LargestEventCountDayDescription(TimeSpanEventData data) {
        String eventCountDescr = "";
        if (data.getEventCount() % 10 == 1) {
            eventCountDescr = "событие.";
        }
        if (data.getEventCount() % 10 > 1 && data.getEventCount() < 5) {
            eventCountDescr = "события.";
        }
        if (data.getEventCount() % 10 > 4) {
            eventCountDescr = "событий.";
        }
        String pattern = LargestEventCountReportFormat + eventCountDescr;
        return String.format(pattern, DateDescription(data.getYear(),
                data.getMonth(), data.getDay()), data.getEventCount()).trim();
    }

    public static String LargestEventCountWeekDescription(TimeSpanEventData data) {
        String eventCountDescr = "";
        if (data.getEventCount() % 10 == 1) {
            eventCountDescr = "событие.";
        }
        if (data.getEventCount() % 10 > 1 && data.getEventCount() < 5) {
            eventCountDescr = "события.";
        }
        if (data.getEventCount() % 10 > 4) {
            eventCountDescr = "событий.";
        }
        String pattern = LargestEventCountWeekReportFormat + eventCountDescr;
        List<DateTime> borders = data.getWeekBorders();
        int month = borders.get(0).getMonthOfYear();
        String leftBorderDescription = DateDescription(borders.get(0).getYear(),
                borders.get(0).getMonthOfYear(), borders.get(0).getDayOfMonth());
        String rightBorderDescription = DateDescription(borders.get(1).getYear(),
                borders.get(1).getMonthOfYear(), borders.get(1).getDayOfMonth());
        return String.format(pattern, leftBorderDescription,
                rightBorderDescription, data.getEventCount()).trim();
    }

    private static String IntervalDescription(Interval interval) {
        String duration = "";
        Days days = interval.toDuration().toStandardDays();
        Hours hours = interval.toDuration().toStandardHours();
        Minutes minutes = interval.toDuration().toStandardMinutes();
        if (days.getDays() > 0) {
            int lastDigitD = days.getDays() % 10;
            if (lastDigitD > 4) {
                duration += String.format("%s дней ", days.getDays());
            }
            if (lastDigitD > 1 && lastDigitD <=4) {
                duration += String.format("%s дня ", days.getDays());
            }
            if (lastDigitD == 1) {
                duration += String.format("%s день ", days.getDays());
            }
        }
        if (hours.getHours() > 0) {
            int lastDigitH = hours.getHours() % 10;
            if (lastDigitH > 4 || lastDigitH == 0) {
                duration += String.format("%s часов ", days.getDays());
            }
            if (lastDigitH > 1 && lastDigitH <=4) {
                duration += String.format("%s часа ", days.getDays());
            }
            if (lastDigitH == 1) {
                duration += String.format("%s час ", days.getDays());
            }
        }
        if (minutes.getMinutes() > 0) {
            int lastDigitM = minutes.getMinutes() % 10;
            if (lastDigitM > 4) {
                duration += String.format("%s часов ", days.getDays());
            }
            if (lastDigitM > 1 && lastDigitM <=4) {
                duration += String.format("%s часа ", days.getDays());
            }
            if (lastDigitM == 1) {
                duration += String.format("%s час ", days.getDays());
            }
        }
        return duration.trim();
    }

    private static String DateDescription(int year, int month, int day) {
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
