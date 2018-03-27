package ru.lod_misis.ithappened.Statistics.Facts;

import java.lang.annotation.Target;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.AllEventsCountFact;
import ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics.MostFrequentEventFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.AvrgRatingFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.AvrgScaleFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.BestEvent;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.SumScaleFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.TrackingEventsCountFact;
import ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs.WorstEvent;

public final class FunctionApplicability  {
    public FunctionApplicability(){}

    public static Fact mostFrequentEventApplicability(List<Tracking> trackingCollection)
    {
        List<Tracking> trckngs = new ArrayList<>();
        for (Tracking tracking: trackingCollection){
            if (!tracking.isDeleted()) trckngs.add(tracking);
        }
        trackingCollection = trckngs;

        if (trackingCollection.size() < 2) return null;
        List<Tracking> processingTracking = new ArrayList<>();
        for (Tracking tracking: trackingCollection) {
            int eventCount = 0;
            for (Event event: tracking.getEventCollection()) {
                if (!event.isDeleted()) eventCount++;
            }
            if (eventCount > 2) processingTracking.add(tracking);
        }
        if (processingTracking.size() < 2) return null;
        return new MostFrequentEventFact(processingTracking);
    }

    public static Fact allEventsCountFactApplicability(List<Tracking> trackingCollection)
    {
        int count=0;
        for (Tracking tracking: trackingCollection) {
            for (Event event: tracking.getEventCollection()) {
                if (!event.isDeleted()) count++;
            }
        }
        if (count <= 5) return null;
        return new AllEventsCountFact(trackingCollection);
    }

    public static Fact trackingEventsCountApplicability(Tracking tracking)
    {
        int eventCount = 0;
        for (Event event: tracking.getEventCollection()) {
            if (!event.isDeleted()) eventCount++;
        }
        if (eventCount == 0) return null;
        return new TrackingEventsCountFact(tracking);
    }

    public static Fact avrgRatingApplicability(Tracking tracking)
    {
        if (tracking.GetRatingCustomization() == TrackingCustomization.None) return null;

        int eventsWithRating = 0;
        for (Event event: tracking.getEventCollection()) {
            if (event.getRating() != null) eventsWithRating++;
        }
        if (eventsWithRating <= 1) return null;

        return new AvrgRatingFact(tracking);
    }

    public static Fact avrgScaleApplicability(Tracking tracking)
    {
        if (tracking.GetScaleCustomization() == TrackingCustomization.None) return null;

        int eventsWithScale = 0;
        for (Event event: tracking.getEventCollection()) {
            if (event.getScale() != null) eventsWithScale++;
        }
        if (eventsWithScale <= 1) return null;

        return new AvrgScaleFact(tracking);
    }

    public static Fact sumScaleApplicability(Tracking tracking)
    {
        if (tracking.GetScaleCustomization() == TrackingCustomization.None) return null;

        int eventsWithScale = 0;
        for (Event event: tracking.getEventCollection()) {
            if (event.getScale() != null) eventsWithScale++;
        }
        if (eventsWithScale <= 1) return null;

        return new SumScaleFact(tracking);
    }

    public static Fact worstEventApplicability(Tracking tracking)
    {
        if(tracking.GetRatingCustomization() == TrackingCustomization.None) return null;

        int ratingCount =0;
        List<Event> eventCollection = removeDeletedEvents(tracking.getEventCollection());

        for (Event event: eventCollection)
        {
            if (event.getRating() != null) ratingCount ++;
        }
        if(ratingCount <= 10 ) return null;

        int monthDifferece = diffMonth(firstEventDate(eventCollection));
        if(monthDifferece <= 3) return null;

        WorstEvent worstEventFact = new WorstEvent(tracking);
        worstEventFact.calculateData();
        Event worstEvent = worstEventFact.getWorstEvent();

        Date worstEventDate = worstEvent.GetEventDate();
        Date currentDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        int daysDiff = (int)(currentDate.getTime() - worstEventDate.getTime())/1000/60/60/24;
        if(daysDiff <= 7) return null;

        return worstEventFact;
    }

    public static Fact bestEventApplicability(Tracking tracking)
    {
        if(tracking.GetRatingCustomization() == TrackingCustomization.None) return null;

        int ratingCount =0;
        List<Event> eventCollection = removeDeletedEvents(tracking.getEventCollection());

        for (Event event: eventCollection)
        {
            if (event.getRating() != null) ratingCount ++;
        }
        if(ratingCount <= 10 ) return null;

        int monthDifferece = diffMonth(firstEventDate(eventCollection));
        if(monthDifferece <= 3) return null;

        BestEvent bestEventFact = new BestEvent(tracking);
        bestEventFact.calculateData();
        Event bestEvent = bestEventFact.getBestEvent();

        Date bestEventDate = bestEvent.GetEventDate();
        Date currentDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        int daysDiff = (int)(currentDate.getTime() - bestEventDate.getTime())/1000/60/60/24;
        if(daysDiff <= 7) return null;

        return bestEventFact;
    }

    private static List<Event> removeDeletedEvents(List<Event> eventCollection)
    {
        List<Event> eventCollectionToReturn = new ArrayList<>();
        for(Event event : eventCollection){
            if(!event.GetStatus()){
                eventCollectionToReturn.add(event);
            }
        }

        return eventCollectionToReturn;
    }

    private static Date firstEventDate(List<Event> eventCollection)
    {
        Date firstEventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        for(Event event: eventCollection)
        {
            if(event.GetEventDate().before(firstEventDate))
            {
                firstEventDate = event.GetEventDate();
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
        int diffMonth = diffYear * 12 + currentDate.get(Calendar.MONTH) - eventDate.get(Calendar.MONTH);

        return diffMonth;
    }

}