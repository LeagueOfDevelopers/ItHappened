package ru.lod_misis.ithappened.Statistics.Facts.Models;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TimeSpanEventData {

    private Integer EventCount;
    private DateTime Date;
    private List<UUID> EventIds;
    private List<Date> illustartionData;

    public TimeSpanEventData(DateTime date) {
        Date = date;
        EventCount = 0;
        EventIds = new ArrayList<>();
    }

    public List<UUID> getEventIds() {
        return EventIds;
    }

    public boolean equals(TimeSpanEventData data) {
        return data.getDate().getYear() == Date.getYear()
                && data.getDate().getDayOfMonth() == Date.getDayOfMonth()
                && data.getDate().getMonthOfYear() == Date.getMonthOfYear();
    }

    public boolean IsItThisDay(DateTime date) {
        return date.getYear() == Date.getYear() && date.getMonthOfYear() == Date.getMonthOfYear() &&
                date.getDayOfMonth() == Date.getDayOfMonth();
    }

    public boolean IsItThisWeek(DateTime date) {
        return date.getYear() == Date.getYear() && date.getWeekOfWeekyear() == Date.getWeekOfWeekyear();
    }

    public DateTime getLeftWeekBorder() {
        int day = Date.getDayOfWeek();
        return Date.minusDays(day - 1);
    }

    public DateTime getRightWeekBorder() {
        int day = Date.getDayOfWeek();
        return Date.plusDays(7 - day);
    }

    public void CountIncrement(UUID EventId) {
        EventCount++;
        EventIds.add(EventId);
    }

    public DateTime getDate() {
        return Date;
    }

    public List<java.util.Date> getIllustartionData() {
        return illustartionData;
    }

    public int getEventCount() {
        return EventCount;
    }
}
