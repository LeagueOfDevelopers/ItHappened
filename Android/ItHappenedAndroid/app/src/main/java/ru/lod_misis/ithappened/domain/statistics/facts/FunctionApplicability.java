package ru.lod_misis.ithappened.domain.statistics.facts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.AllEventsCountFact;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.DayWithLargestEventCountFact;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.MostFrequentEventFact;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.WeekWithLargestEventCountFact;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.correlation.BinaryCorrelationFact;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.correlation.MultinomialCorrelationFact;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.correlation.ScaleCorrelationFact;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.ScalePredictionFact;
import ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics.scalePrediction.predictors.PredictorEnum;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.AvrgRatingFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.AvrgScaleFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.BestEvent;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.CertainDayTimeFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.CertainWeekDaysFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.FrequencyTrendChangingFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.LongTimeAgoFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.LongestBreakFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.RatingTrendChangingFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.ScaleTrendChangingFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.SumScaleFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.TrackingEventsCountFact;
import ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs.WorstEvent;

public final class FunctionApplicability  {

    public FunctionApplicability(){}

    public static Fact mostFrequentEventApplicability(List<TrackingV1> trackingV1Collection)
    {
        List<TrackingV1> trckngs = new ArrayList<>();
        for (TrackingV1 trackingV1 : trackingV1Collection){
            if (!trackingV1.isDeleted()) trckngs.add(trackingV1);
        }

        trackingV1Collection = trckngs;
        int trackingCount = 0;
        if (trackingV1Collection.size() < 2) return null;
        for (TrackingV1 trackingV1 : trackingV1Collection) {
            int eventCount = 0;
            for (EventV1 eventV1 : trackingV1.getEventCollection()) {
                if (!eventV1.isDeleted()) eventCount++;
            }
            if (eventCount > 2) trackingCount++;
        }
        if (trackingCount < 2) return null;
        return new MostFrequentEventFact(trackingV1Collection);
    }

    public static ru.lod_misis.ithappened.domain.statistics.facts.Fact allEventsCountFactApplicability(List<TrackingV1> trackingV1Collection)
    {
        int count=0;
        for (TrackingV1 trackingV1 : trackingV1Collection) {
            if (!trackingV1.isDeleted()) {
                for (EventV1 eventV1 : trackingV1.getEventCollection()) {
                    if (!eventV1.isDeleted()) count++;
                }
            }
        }
        if (count <= 5) return null;
        return new AllEventsCountFact(trackingV1Collection);
    }

    public static Fact trackingEventsCountApplicability(TrackingV1 trackingV1)
    {
        int eventCount = 0;
        for (EventV1 eventV1 : trackingV1.getEventCollection()) {
            if (!eventV1.isDeleted()) eventCount++;
        }
        if (eventCount == 0) return null;
        return new TrackingEventsCountFact(trackingV1);
    }

    public static Fact avrgRatingApplicability(TrackingV1 trackingV1)
    {
        if (trackingV1.getRatingCustomization() == TrackingCustomization.None) return null;

        List<EventV1> eventV1Collection = removeDeletedEvents(trackingV1.getEventHistory());

        int eventsWithRating = 0;
        for (EventV1 eventV1 : eventV1Collection) {
            if (eventV1.getRating() != null) eventsWithRating++;
        }
        if (eventsWithRating <= 1) return null;

        return new AvrgRatingFact(trackingV1);
    }

    public static Fact avrgScaleApplicability(TrackingV1 trackingV1)
    {
        if (trackingV1.getScaleCustomization() == TrackingCustomization.None) return null;

        List<EventV1> eventV1Collection = removeDeletedEvents(trackingV1.getEventHistory());

        int eventsWithScale = 0;
        for (EventV1 eventV1 : eventV1Collection) {
            if (eventV1.getScale() != null) eventsWithScale++;
        }
        if (eventsWithScale <= 1) return null;

        return new AvrgScaleFact(trackingV1);
    }

    public static Fact sumScaleApplicability(TrackingV1 trackingV1)
    {
        if (trackingV1.getScaleCustomization() == TrackingCustomization.None) return null;

        List<EventV1> eventV1Collection = removeDeletedEvents(trackingV1.getEventHistory());

        int eventsWithScale = 0;
        for (EventV1 eventV1 : eventV1Collection) {
            if (eventV1.getScale() != null) eventsWithScale++;
        }
        if (eventsWithScale <= 1) return null;

        return new SumScaleFact(trackingV1);
    }

    public static Fact worstEventApplicability(TrackingV1 trackingV1)
    {
        if(trackingV1.getRatingCustomization() == TrackingCustomization.None) return null;

        int ratingCount =0;
        List<EventV1> eventV1Collection = removeDeletedEvents(trackingV1.getEventCollection());

        for (EventV1 eventV1 : eventV1Collection)
        {
            if (eventV1.getRating() != null) ratingCount ++;
        }
        if(ratingCount <= 10 ) return null;

        int monthDifferece = diffMonth(firstEventDate(eventV1Collection));
        if(monthDifferece < 3) return null;

        WorstEvent worstEventFact = new WorstEvent(trackingV1);
        worstEventFact.calculateData();
        EventV1 worstEventV1 = worstEventFact.getWorstEventV1();

        Date worstEventDate = worstEventV1.getEventDate();
        Date currentDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        long curDateTime = currentDate.getTime();
        long worstDateTime = worstEventDate.getTime();
//        int daysDiff = (int)(currentDate.getTime() - worstEventDate.getTime())/1000/60/60/24;
        double daysDiff = (curDateTime - worstDateTime)/1000/60/60/24;
        if(daysDiff <= 7) return null;

        return worstEventFact;
    }

    public static Fact bestEventApplicability(TrackingV1 trackingV1)
    {
        if(trackingV1.getRatingCustomization() == TrackingCustomization.None) return null;

        int ratingCount =0;
        List<EventV1> eventV1Collection = removeDeletedEvents(trackingV1.getEventCollection());

        for (EventV1 eventV1 : eventV1Collection)
        {
            if (eventV1.getRating() != null) ratingCount ++;
        }
        if(ratingCount <= 10 ) return null;

        int monthDifferece = diffMonth(firstEventDate(eventV1Collection));
        if(monthDifferece < 3) return null;

        BestEvent bestEventFact = new BestEvent(trackingV1);
        bestEventFact.calculateData();
        EventV1 bestEventV1 = bestEventFact.getBestEventV1();

        Date bestEventDate = bestEventV1.getEventDate();
        Date currentDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        long curDateTime = currentDate.getTime();
        long bestDateTime = bestEventDate.getTime();

//        int daysDiff = (int)(currentDate.getTime() - bestEventDate.getTime())/1000/60/60/24;
        double daysDiff = (double)(curDateTime - bestDateTime)/1000/60/60/24;
        if(daysDiff <= 7) return null;

        return bestEventFact;
    }

    public static Fact certainWeekDaysApplicability(TrackingV1 trackingV1)
    {
        List<EventV1> eventV1Collection = removeDeletedEvents(trackingV1.getEventHistory());
        if(eventV1Collection.size() <= 7) return null;

        CertainWeekDaysFact fact = new CertainWeekDaysFact(trackingV1);
        fact.calculateData();

        if(fact.getHighestPercentage().getPercetage() < 25.0) return null;

        return fact;
    }

    public static Fact certainDayTimeApplicability(TrackingV1 trackingV1)
    {
        List<EventV1> eventV1Collection = removeDeletedEvents(trackingV1.getEventHistory());
        if(eventV1Collection.size() <= 7) return null;

        CertainDayTimeFact fact = new CertainDayTimeFact(trackingV1);
        fact.calculateData();

        if(fact.getHighestPercentage().getPercetage() < 70.0) return null;

        return fact;
    }

    public static Fact longTimeAgoApplicability(TrackingV1 trackingV1){

        LongTimeAgoFact fact = new LongTimeAgoFact(trackingV1);
        fact.calculateData();

        List<EventV1> eventV1Collection = removeDeletedEvents(trackingV1.getEventHistory());
        if(eventV1Collection.size() <= 2) return null;

        Double daysSinceLastEvent = fact.getDaysSinceLastEvent();
        if(daysSinceLastEvent <= 7) return null;

        Date firstEventDate = eventV1Collection.get(0).getEventDate();
        Date lastEventDate = firstEventDate;

        for(EventV1 event : eventV1Collection){
            if (firstEventDate.after(event.getEventDate()))
                firstEventDate = event.getEventDate();
            if (lastEventDate.before(event.getEventDate()))
                lastEventDate = event.getEventDate();
        }

        long interval = lastEventDate.getTime() - firstEventDate.getTime();
        if (daysSinceLastEvent <= 3 * interval/1000/60/60/24)
            return null;

        return fact;
    }

    private static List<EventV1> removeDeletedEvents(List<EventV1> eventV1Collection)
    {
        List<EventV1> eventV1CollectionToReturn = new ArrayList<>();
        for(EventV1 eventV1 : eventV1Collection){
            if(!eventV1.isDeleted()){
                eventV1CollectionToReturn.add(eventV1);
            }
        }

        return eventV1CollectionToReturn;
    }

    private static Date firstEventDate(List<EventV1> eventV1Collection)
    {
        Date firstEventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        for(EventV1 eventV1 : eventV1Collection)
        {
            if(eventV1.getEventDate().before(firstEventDate))
            {
                firstEventDate = eventV1.getEventDate();
            }
        }

        return firstEventDate;
    }

    private static int diffMonth(Date firstEventDate)
    {
        Calendar eventDate = new GregorianCalendar();
        eventDate.setTime(firstEventDate);
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(Calendar.getInstance(TimeZone.getDefault()).getTime());

        int diffYear = currentDate.get(Calendar.YEAR) - eventDate.get(Calendar.YEAR);

        return diffYear * 12 + currentDate.get(Calendar.MONTH)
                - eventDate.get(Calendar.MONTH);
    }

    private static DataValidator validator = new DataValidator();
    // Минимальный размер валидного набора данных для расчета бинарной корреляции
    private static final int minBinaryCorrelationDataSetSize = 40;
    // Минимальный размер валидного набора данных для расчета корреляции между скалярными значениями
    private static final int minScaleCorrelationDataSetSize = 4;
    // Минимальный размер валидного набора данных для расчета корреляции между рейтингом
    private static final int minRatingCorrelationDataSetSize = 4;
    // Минимальный размер валидного набора данных для расчета факта изменения тренда шкалы
    private static final int minScaleTrendAnalysisDataSetSize = 4;
    // Минимальный размер валидного набора данных для расчета факта изменения тренда рейтинга
    private static final int minRatingTrendAnalysisDataSetSize = 4;
    // Минимальный размер валидного набора данных для расчета факта изменения тренда частоты
    private static final int minFrequencyTrendAnalysisDataSetSize = 4;
    // Минимальный размер валидного набора данных для расчета факта самого долгого перерыва
    private static final int minLongestBreakApplicabilityFactCount = 10;
    // Точность предсказания значения шкалы регулирется размером окна
    // (размер измерятеся в стандартных отклонениях и 2 стандартных
    // отклонения соответствуют точности 95%, 3 отклонения - 99.5%, но чем шире окно, тем бесполезнее информация)
    private static final double nDeviationsForScalePrediction = 2;

    public static List<Fact> BinaryCorrelationFactApplicability(List<TrackingV1> trackings) {
        List<Fact> facts = new ArrayList<>();
        for (int i = 0; i < trackings.size() - 1; i++) {
            for (int j = i + 1; j < trackings.size(); j++) {
                boolean firstCondition = validator.CheckTrackingForBinaryData(trackings.get(i),
                        minBinaryCorrelationDataSetSize);
                boolean secondCondition = validator.CheckTrackingForBinaryData(trackings.get(j),
                        minBinaryCorrelationDataSetSize);
                if (firstCondition && secondCondition) {
                    BinaryCorrelationFact fact = new BinaryCorrelationFact(trackings.get(i), trackings.get(j));
                    fact.calculateData();
                    if (fact.IsBoolCorrSignificant()) facts.add(fact);
                }
            }
        }
        return facts;
    }

    public static List<Fact> ScaleCorrelationFactApplicability(List<TrackingV1> trackings) {
        List<Fact> facts = new ArrayList<>();
        for (int i = 0; i < trackings.size() - 1; i++) {
            for (int j = i + 1; j < trackings.size(); j++) {
                boolean firstCondition = validator.CheckTrackingForScaleData(trackings.get(i),
                        minScaleCorrelationDataSetSize);
                boolean secondCondition = validator.CheckTrackingForScaleData(trackings.get(j),
                        minScaleCorrelationDataSetSize);
                if (firstCondition && secondCondition) {
                    ScaleCorrelationFact fact = new ScaleCorrelationFact(trackings.get(i), trackings.get(j));
                    fact.calculateData();
                    if (fact.IsDoubleCorrSignificant()) facts.add(fact);
                }
            }
        }
        return facts;
    }

    public static List<Fact> MultinomialCorrelationApplicability(List<TrackingV1> trackings) {
        List<Fact> facts = new ArrayList<>();
        for (int i = 0; i < trackings.size() - 1; i++) {
            for (int j = i + 1; j < trackings.size(); j++) {
                boolean firstCondition = validator.CheckTrackingForRatingData(trackings.get(i),
                        minRatingCorrelationDataSetSize);
                boolean secondCondition = validator.CheckTrackingForRatingData(trackings.get(j),
                        minRatingCorrelationDataSetSize);
                if (firstCondition && secondCondition) {
                    MultinomialCorrelationFact fact = new MultinomialCorrelationFact(trackings.get(i), trackings.get(j));
                    fact.calculateData();
                    if (fact.IsMultinomialCorrSignificant()) facts.add(fact);
                }
            }
        }
        return facts;
    }

    public static Fact ScaleTrendChangingFactApplicability(TrackingV1 tracking) {
        Fact factToReturn = null;
        if (tracking.isDeleted()) return null;
        boolean firstCondition = tracking.getScaleCustomization() != TrackingCustomization.None;
        boolean secondCondition = validator.CheckScaleEventCollection(tracking.getEventHistory(),
                minScaleTrendAnalysisDataSetSize);
        if (firstCondition && secondCondition) {
            ScaleTrendChangingFact fact = new ScaleTrendChangingFact(tracking);
            fact.calculateData();
            if (fact.IsTrendDeltaSignificant()) factToReturn = fact;
        }
        return factToReturn;
    }

    public static Fact RatingTrendChangingFactApplicability(TrackingV1 tracking) {
        Fact factToReturn = null;
        if (tracking.isDeleted()) return null;
        boolean firstCondition = tracking.getRatingCustomization() != TrackingCustomization.None;
        boolean secondCondition = validator.CheckRatingEventCollection(tracking.getEventHistory(),
                minRatingTrendAnalysisDataSetSize);
        if (firstCondition && secondCondition) {
            RatingTrendChangingFact fact = new RatingTrendChangingFact(tracking);
            fact.calculateData();
            if (fact.IsTrendDeltaSignificant()) factToReturn = fact;
        }
        return factToReturn;
    }

    public static Fact FrequencyTrendChangingFactApplicability(TrackingV1 tracking) {
        Fact factToReturn = null;
        if (tracking.isDeleted()) return null;
        boolean condition = validator.CheckEventsForNotDeletedAndDate(tracking.getEventHistory(),
                minFrequencyTrendAnalysisDataSetSize);
        if (condition) {
            FrequencyTrendChangingFact fact = new FrequencyTrendChangingFact(tracking);
            fact.calculateData();
            if (fact.IsTrendDeltaSignificant()) factToReturn = fact;
        }
        return factToReturn;
    }

    public static Fact LongestBreakFactApplicability(TrackingV1 tracking) {
        Fact factToReturn = null;
        if (tracking.isDeleted()) return null;
        boolean condition = validator.CheckEventsForNotDeletedAndDate(tracking.getEventHistory(),
                minLongestBreakApplicabilityFactCount);
        if (condition) {
            LongestBreakFact fact = new LongestBreakFact(tracking);
            fact.calculateData();
            if (fact.getLongestBreak() != null) factToReturn = fact;
        }
        return factToReturn;
    }

    public static Fact DayWithLargestEventCountApplicability(List<TrackingV1> trackings) {
        DayWithLargestEventCountFact fact = new DayWithLargestEventCountFact(trackings);
        fact.calculateData();
        if (!fact.IsFactSignificant()) return null;
        return fact;
    }

    public static Fact WeekWithLargestEventCountApplicability(List<TrackingV1> trackings) {
        WeekWithLargestEventCountFact fact = new WeekWithLargestEventCountFact(trackings);
        fact.calculateData();
        if (!fact.IsFactSignificant()) return null;
        return fact;
    }

    public static Fact ScalePredictionFactApplicability(TrackingV1 trackingV1) {
        // Проверка трекинга и коллекции эвентов на то, что все эвенты произошли в прошлом и каждый эвент имеет значение шкалы
        if (!validator.CheckTrackingForScaleData(trackingV1, trackingV1.getEventCollection().size())) {
            return null;
        }
        ScalePredictionFact fact = new ScalePredictionFact(trackingV1, 1,
                PredictorEnum.LinearRegressionPredictor, nDeviationsForScalePrediction);
        if (fact.hasEnoughData()) {
            fact.calculateData();
            return fact;
        }
        else {
            return null;
        }
    }
}