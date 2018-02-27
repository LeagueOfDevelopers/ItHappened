package com.example.ithappenedandroid.StatisticsHelpers;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

public class GraphStatisticsHelper {

    Tracking tracking;
    List<Event> eventsCollection = new ArrayList<>();

    public GraphStatisticsHelper(Tracking tracking) {
        this.tracking = tracking;
        for (Event event : tracking.GetEventCollection()) {
            if (!event.GetStatus()) {
                eventsCollection.add(event);
            }
        }
    }

    public LinkedHashMap<Date, Integer> allTimesGraphData() {
        LinkedHashMap<Date, Integer> count = new LinkedHashMap<>();
        boolean flag = false;

        for (Event event : eventsCollection) {
            flag = false;
                if (count.size() == 0) {
                    count.put(getZeroTimeDate(event.GetEventDate()), 1);
                } else {
                    for (int i = 0; i < count.size(); i++) {
                        if (count.containsKey(getZeroTimeDate(event.GetEventDate()))) {
                            int iterationCount = count.get(getZeroTimeDate(event.GetEventDate())) + 1;
                            count.put(getZeroTimeDate(event.GetEventDate()), iterationCount);
                            flag = true;
                            break;
                        }
                    }
                    if (flag == false) {
                        count.put(getZeroTimeDate(event.GetEventDate()), 1);
                    }
                }
            }
        return count;
    }

    public LinkedHashMap<Date, Integer> lastWeekGraphData() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        Date start = c.getTime();
        c.add(Calendar.DATE, 6);
        Date end = c.getTime();

        return createHashMap(start, end);
    }

    public LinkedHashMap<Date, Integer> lastMonthGraphData() {
        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DATE, -i);
        Date start = c.getTime();
        Date end = Calendar.getInstance(TimeZone.getDefault()).getTime();

        return createHashMap(start, end);
    }

    public LinkedHashMap<Date , Integer> lastThreeMonthGraphData() {
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

        return createHashMap(start, end);
    }

    public LinkedHashMap<Date , Integer> halfYearGraphData() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -6);
        Date start = c.getTime();
        Date end = Calendar.getInstance(TimeZone.getDefault()).getTime();

        return createHashMap(start, end);

    }

    public LinkedHashMap<Date, Integer> lastYearGraphData() {

        int lastYearCount = 0;
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);
        Date start = c.getTime();
        Date end = Calendar.getInstance(TimeZone.getDefault()).getTime();

        return createHashMap(start, end);
    }

    public LinkedHashMap<Date , Integer> userPeriodGraphData(Date start, Date end) {

        return createHashMap(start, end);

    }

    private static Date getZeroTimeDate(Date fecha) {
        Date res = fecha;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }

    private LinkedHashMap<Date, Integer> createHashMap(Date start, Date end) {

        LinkedHashMap<Date, Integer> count = new LinkedHashMap<>();
        boolean flag = false;

        for (Event event : eventsCollection) {
            flag = false;
            if (event.GetEventDate().compareTo(start) >= 0 && event.GetEventDate().compareTo(end) <= 0) {
                if (count.size() == 0) {
                    count.put(getZeroTimeDate(event.GetEventDate()), 1);
                } else {
                    for (int i = 0; i < count.size(); i++) {
                        if (count.containsKey(getZeroTimeDate(event.GetEventDate()))) {
                            int iterationCount = count.get(getZeroTimeDate(event.GetEventDate())) + 1;
                            count.put(getZeroTimeDate(event.GetEventDate()), iterationCount);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        count.put(getZeroTimeDate(event.GetEventDate()), 1);
                    }
                }
            }
        }

        return count;
    }
}
