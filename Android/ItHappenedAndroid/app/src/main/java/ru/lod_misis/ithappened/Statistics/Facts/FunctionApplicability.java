package ru.lod_misis.ithappened.Statistics.Facts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.AllEventsCountFact;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.MostFrequentEventFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.AvrgRatingFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.AvrgScaleFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.BestEvent;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.CertainDayTimeFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.CertainWeekDaysFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.LongTimeAgoFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.SumScaleFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.TrackingEventsCountFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.WorstEvent;

public final class FunctionApplicability  {
    public FunctionApplicability(){}

    public static Fact mostFrequentEventApplicability(List<NewTracking> newTrackingCollection)
    {
        List<NewTracking> trckngs = new ArrayList<>();
        for (NewTracking newTracking : newTrackingCollection){
            if (!newTracking.isDeleted()) trckngs.add(newTracking);
        }
        newTrackingCollection = trckngs;
        int trackingCount = 0;
        if (newTrackingCollection.size() < 2) return null;
        for (NewTracking newTracking : newTrackingCollection) {
            int eventCount = 0;
            for (NewEvent newEvent : newTracking.getNewEventCollection()) {
                if (!newEvent.isDeleted()) eventCount++;
            }
            if (eventCount > 2) trackingCount++;
        }
        if (trackingCount < 2) return null;
        return new MostFrequentEventFact(newTrackingCollection);
    }

    public static Fact allEventsCountFactApplicability(List<NewTracking> newTrackingCollection)
    {
        int count=0;
        for (NewTracking newTracking : newTrackingCollection) {
            if (!newTracking.isDeleted()) {
                for (NewEvent newEvent : newTracking.getNewEventCollection()) {
                    if (!newEvent.isDeleted()) count++;
                }
            }
        }
        if (count <= 5) return null;
        return new AllEventsCountFact(newTrackingCollection);
    }

    public static Fact trackingEventsCountApplicability(NewTracking newTracking)
    {
        int eventCount = 0;
        for (NewEvent newEvent : newTracking.getNewEventCollection()) {
            if (!newEvent.isDeleted()) eventCount++;
        }
        if (eventCount == 0) return null;
        return new TrackingEventsCountFact(newTracking);
    }

    public static Fact avrgRatingApplicability(NewTracking newTracking)
    {
        if (newTracking.GetRatingCustomization() == TrackingCustomization.None) return null;

        List<NewEvent> newEventCollection = removeDeletedEvents(newTracking.GetEventCollection());

        int eventsWithRating = 0;
        for (NewEvent newEvent : newEventCollection) {
            if (newEvent.getRating() != null) eventsWithRating++;
        }
        if (eventsWithRating <= 1) return null;

        return new AvrgRatingFact(newTracking);
    }

    public static Fact avrgScaleApplicability(NewTracking newTracking)
    {
        if (newTracking.GetScaleCustomization() == TrackingCustomization.None) return null;

        List<NewEvent> newEventCollection = removeDeletedEvents(newTracking.GetEventCollection());

        int eventsWithScale = 0;
        for (NewEvent newEvent : newEventCollection) {
            if (newEvent.getScale() != null) eventsWithScale++;
        }
        if (eventsWithScale <= 1) return null;

        return new AvrgScaleFact(newTracking);
    }

    public static Fact sumScaleApplicability(NewTracking newTracking)
    {
        if (newTracking.GetScaleCustomization() == TrackingCustomization.None) return null;

        List<NewEvent> newEventCollection = removeDeletedEvents(newTracking.GetEventCollection());

        int eventsWithScale = 0;
        for (NewEvent newEvent : newEventCollection) {
            if (newEvent.getScale() != null) eventsWithScale++;
        }
        if (eventsWithScale <= 1) return null;

        return new SumScaleFact(newTracking);
    }

    public static Fact worstEventApplicability(NewTracking newTracking)
    {
        if(newTracking.GetRatingCustomization() == TrackingCustomization.None) return null;

        int ratingCount =0;
        List<NewEvent> newEventCollection = removeDeletedEvents(newTracking.getNewEventCollection());

        for (NewEvent newEvent : newEventCollection)
        {
            if (newEvent.getRating() != null) ratingCount ++;
        }
        if(ratingCount <= 10 ) return null;

        int monthDifferece = diffMonth(firstEventDate(newEventCollection));
        if(monthDifferece < 3) return null;

        WorstEvent worstEventFact = new WorstEvent(newTracking);
        worstEventFact.calculateData();
        NewEvent worstNewEvent = worstEventFact.getWorstNewEvent();

        Date worstEventDate = worstNewEvent.GetEventDate();
        Date currentDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        long curDateTime = currentDate.getTime();
        long worstDateTime = worstEventDate.getTime();
//        int daysDiff = (int)(currentDate.getTime() - worstEventDate.getTime())/1000/60/60/24;
        double daysDiff = (curDateTime - worstDateTime)/1000/60/60/24;
        if(daysDiff <= 7) return null;

        return worstEventFact;
    }

    public static Fact bestEventApplicability(NewTracking newTracking)
    {
        if(newTracking.GetRatingCustomization() == TrackingCustomization.None) return null;

        int ratingCount =0;
        List<NewEvent> newEventCollection = removeDeletedEvents(newTracking.getNewEventCollection());

        for (NewEvent newEvent : newEventCollection)
        {
            if (newEvent.getRating() != null) ratingCount ++;
        }
        if(ratingCount <= 10 ) return null;

        int monthDifferece = diffMonth(firstEventDate(newEventCollection));
        if(monthDifferece < 3) return null;

        BestEvent bestEventFact = new BestEvent(newTracking);
        bestEventFact.calculateData();
        NewEvent bestNewEvent = bestEventFact.getBestNewEvent();

        Date bestEventDate = bestNewEvent.GetEventDate();
        Date currentDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        long curDateTime = currentDate.getTime();
        long bestDateTime = bestEventDate.getTime();

//        int daysDiff = (int)(currentDate.getTime() - bestEventDate.getTime())/1000/60/60/24;
        double daysDiff = (double)(curDateTime - bestDateTime)/1000/60/60/24;
        if(daysDiff <= 7) return null;

        return bestEventFact;
    }

    public static Fact certainWeekDaysApplicability(NewTracking newTracking)
    {
        List<NewEvent> newEventCollection = removeDeletedEvents(newTracking.GetEventCollection());
        if(newEventCollection.size() <= 7) return null;

        CertainWeekDaysFact fact = new CertainWeekDaysFact(newTracking);
        fact.calculateData();

        if(fact.getHighestPercentage().getPercetage() < 25.0) return null;

        return fact;
    }

    public static Fact certainDayTimeApplicability(NewTracking newTracking)
    {
        List<NewEvent> newEventCollection = removeDeletedEvents(newTracking.GetEventCollection());
        if(newEventCollection.size() <= 7) return null;

        CertainDayTimeFact fact = new CertainDayTimeFact(newTracking);
        fact.calculateData();

        if(fact.getHighestPercentage().getPercetage() < 70.0) return null;

        return fact;
    }

    public static Fact longTimeAgoApplicability(NewTracking newTracking){

        LongTimeAgoFact fact = new LongTimeAgoFact(newTracking);
        fact.calculateData();

        Double daysSinceLastEvent = fact.getDaysSinceLastEvent();
        if(daysSinceLastEvent <= 7) return null;

        List<NewEvent> newEventCollection = removeDeletedEvents(newTracking.GetEventCollection());
        if(newEventCollection.size() <= 2) return null;

        newEventCollection = sortEventCollectionByDate(newEventCollection);

        for(int i = 0; i < newEventCollection.size() - 1; i++){
            Date firstEventDate = newEventCollection.get(i).GetEventDate();
            Date secondEventDate = newEventCollection.get(i+1).GetEventDate();

            double interval = (double)(secondEventDate.getTime() - firstEventDate.getTime())/
                    1000/60/60/24;

            if(daysSinceLastEvent <= interval) return null;
        }

        return fact;
    }

    private static List<NewEvent> removeDeletedEvents(List<NewEvent> newEventCollection)
    {
        List<NewEvent> newEventCollectionToReturn = new ArrayList<>();
        for(NewEvent newEvent : newEventCollection){
            if(!newEvent.GetStatus()){
                newEventCollectionToReturn.add(newEvent);
            }
        }

        return newEventCollectionToReturn;
    }

    private static Date firstEventDate(List<NewEvent> newEventCollection)
    {
        Date firstEventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        for(NewEvent newEvent : newEventCollection)
        {
            if(newEvent.GetEventDate().before(firstEventDate))
            {
                firstEventDate = newEvent.GetEventDate();
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
        int diffMonth = diffYear * 12 + currentDate.get(Calendar.MONTH)
                - eventDate.get(Calendar.MONTH);

        return diffMonth;
    }

    public static List<NewEvent> sortEventCollectionByDate(List<NewEvent> newEventCollection) {

        Collections.sort(newEventCollection, new Comparator<NewEvent>() {
            @Override
            public int compare(NewEvent newEvent, NewEvent t1) {
                return newEvent.GetEventDate().compareTo(t1.GetEventDate());
            }
        });

        return newEventCollection;
    }
}