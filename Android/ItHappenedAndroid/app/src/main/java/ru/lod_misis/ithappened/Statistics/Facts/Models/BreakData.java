package ru.lod_misis.ithappened.Statistics.Facts.Models;

import java.util.Date;
import java.util.UUID;

public class BreakData {

    private Date Interval;
    private Date FirstEventDate;
    private Date SecondEventDate;
    private UUID FirstEventId;
    private UUID SecondEventId;

    public BreakData(Date firstEventDate, Date secondEventDate, UUID firstEventId, UUID secondEventId) {
        FirstEventDate = firstEventDate;
        SecondEventDate = secondEventDate;
        FirstEventId = firstEventId;
        SecondEventId = secondEventId;
        Interval = new Date(SecondEventDate.getTime() - FirstEventDate.getTime());
    }

    public Date getInterval() {
        return Interval;
    }

    public Date getFirstEventDate() {
        return FirstEventDate;
    }

    public Date getSecondEventDate() {
        return SecondEventDate;
    }

    public UUID getFirstEventId() {
        return FirstEventId;
    }

    public UUID getSecondEventId() {
        return SecondEventId;
    }
}
