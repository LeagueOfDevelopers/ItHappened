package com.example.ithappenedandroid.StatisticsHelpers;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class GraphStatisticsHelper {

    Tracking tracking;
    List<Event> eventsCollection;

    public GraphStatisticsHelper(Tracking tracking) {
        this.tracking = tracking;
        for(Event event : tracking.GetEventCollection()){
            if(!event.GetStatus()){
                eventsCollection.add(event);
            }
        }
    }

    public int allTimesGraphData(){
        return eventsCollection.size();
    }

    public int lastWeekGraphData(){
        int lastWeekCount = 0;

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        Date start = c.getTime();
        c.add(Calendar.DATE, 6);
        Date end = c.getTime();

        for(Event event : eventsCollection){
            if(event.GetEventDate().compareTo(start) >= 0 && event.GetEventDate().compareTo(end) <= 0){
                lastWeekCount++;
            }
        }

        return lastWeekCount;
    }

    public int lastMonthGraphData(){

        int lastMonthCount = 0;

        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DATE, -i);
        Date start = c.getTime();
        Date end = Calendar.getInstance(TimeZone.getDefault()).getTime();

        for(Event event : eventsCollection){
            if(event.GetEventDate().compareTo(start) >= 0 && event.GetEventDate().compareTo(end) <= 0){
                lastMonthCount++;
            }
        }

        return lastMonthCount;
    }

    public int lastThreeMonthGraphData(){

        int lastThreeMonthCount = 0;
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DATE, -i);
        int j = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DATE, -j);
        int k = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DATE, -k);
        Date start = c.getTime();
        Date end = Calendar.getInstance(TimeZone.getDefault()).getTime();

        for(Event event : eventsCollection){
            if(event.GetEventDate().compareTo(start) >= 0 && event.GetEventDate().compareTo(end) <= 0){
                lastThreeMonthCount++;
            }
        }
        return lastThreeMonthCount;

    }

    public int halfYearGraphData(){

        int halfYearCount = 0;

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -1/2);
        Date start = c.getTime();
        Date end = Calendar.getInstance(TimeZone.getDefault()).getTime();

        for(Event event : eventsCollection){
            if(event.GetEventDate().compareTo(start) >= 0 && event.GetEventDate().compareTo(end) <= 0){
                halfYearCount++;
            }
        }

        return halfYearCount;

    }

    public int lastYearGraphData(){

        int lastYearCount = 0;
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);
        Date start = c.getTime();
        Date end = Calendar.getInstance(TimeZone.getDefault()).getTime();

        for(Event event : eventsCollection){
            if(event.GetEventDate().compareTo(start) >= 0 && event.GetEventDate().compareTo(end) <= 0){
                lastYearCount++;
            }
        }

        return lastYearCount;
    }

    public int userPeriodGraphData(Date start, Date end){

        int userPeriodCount = 0;

        for(Event event : eventsCollection){
            if(event.GetEventDate().compareTo(start) >= 0 && event.GetEventDate().compareTo(end) <= 0){
                userPeriodCount++;
            }
        }

        return userPeriodCount;

    }
}
