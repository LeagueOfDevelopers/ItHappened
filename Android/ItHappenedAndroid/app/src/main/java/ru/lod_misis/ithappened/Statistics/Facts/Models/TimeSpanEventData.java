package ru.lod_misis.ithappened.Statistics.Facts.Models;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimeSpanEventData {

    private Integer EventCount;
    private DateTime Date;
    private List<UUID> EventIds;

    public TimeSpanEventData(DateTime date) {
        Date = date;
        EventCount = 0;
        EventIds = new ArrayList<>();
    }

    public List<UUID> getEventIds() {
        return EventIds;
    }

    public boolean equals(TimeSpanEventData data) {
        return data.getYear() == Date.getYear() && data.getDay() == Date.getDayOfMonth() &&
                data.getMonth() == Date.getMonthOfYear();
    }

    public boolean IsItThisDay(DateTime date) {
        return date.getYear() == Date.getYear() && date.getMonthOfYear() == Date.getMonthOfYear() &&
                date.getDayOfMonth() == Date.getDayOfMonth();
    }

    public boolean IsItThisWeek(DateTime date) {
        return date.getYear() == Date.getYear() && date.getWeekOfWeekyear() == Date.getWeekOfWeekyear();
    }

    public List<DateTime> getWeekBorders() {
        int day = Date.getDayOfWeek();
        List<DateTime> borders = new ArrayList<>();
        borders.add(Date.minusDays(day - 1));
        borders.add(Date.plusDays(7 - day));
        return borders;
    }

    public void CountIncrement(UUID EventId) {
        EventCount++;
        EventIds.add(EventId);
    }

    public int getYear() {
        return Date.getYear();
    }

    public int getDay() {
        return Date.getDayOfMonth();
    }

    public int getEventCount() {
        return EventCount;
    }

    public Integer getMonth() {
        return Date.getMonthOfYear();
    }
}
