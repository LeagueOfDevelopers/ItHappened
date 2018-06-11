package ru.lod_misis.ithappened.Statistics.Facts.Models;

import org.joda.time.DateTime;

public class DayEventData {

    private Integer Year;
    private Integer Month;
    private Integer Day;
    private Integer EventCount;

    public DayEventData(DateTime date) {
        Year = date.getYear();
        Month = date.getMonthOfYear();
        Day = date.getDayOfMonth();
        EventCount = 0;
    }

    public boolean equals(DayEventData data) {
        return data.getYear() == Year && data.getMonth() == Month && data.getDay() == Day;
    }

    public boolean IsItThisDay(DateTime date) {
        return Year == date.getYear() && Month == date.getMonthOfYear() && Day == date.getDayOfMonth();
    }

    public void CountIncrement() {
        EventCount++;
    }

    public int getYear() {
        return Year;
    }

    public int getMonth() {
        return Month;
    }

    public int getDay() {
        return Day;
    }

    public int getEventCount() {
        return EventCount;
    }
}
