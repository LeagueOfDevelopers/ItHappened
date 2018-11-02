package ru.lod_misis.ithappened.Domain.Statistics.Facts.Models;

import org.joda.time.Duration;

import java.util.Date;
import java.util.UUID;

public class BreakData {

    private Duration Duration;
    private Date FirstEventDate;
    private Date SecondEventDate;
    private UUID FirstEventId;
    private UUID SecondEventId;

    public BreakData(Date firstEventDate, Date secondEventDate, UUID firstEventId, UUID secondEventId) {
        FirstEventDate = firstEventDate;
        SecondEventDate = secondEventDate;
        FirstEventId = firstEventId;
        SecondEventId = secondEventId;
        Duration = new Duration(SecondEventDate.getTime() - FirstEventDate.getTime());
    }

    public Duration getDuration() {
        return Duration;
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
