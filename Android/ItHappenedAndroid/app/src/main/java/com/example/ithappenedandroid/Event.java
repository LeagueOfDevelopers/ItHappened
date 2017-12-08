package com.example.ithappenedandroid;

import java.time.OffsetDateTime;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Ded on 08.12.2017.
 */

public class Event
{
    public Event()
    {
        _eventId = UUID.randomUUID();
        _eventDate = TimeZone.getDefault();
    }

    public void SetValueOfCounter(Integer count) { _count = count;}
    public void SetValueOfScale(Integer scale)
    {
        if (scale > 10 || scale<1)
            throw new IndexOutOfBoundsException("Value of scale out of range");
        _scale = scale;
    }
    public void WriteComment(String comment) { _comment = comment;}

    public TimeZone GetEventDate() {return _eventDate;}
    public UUID GetEventId() {return _eventId;}
    public Integer GetCount() {return _count;}
    public Integer GetScale() {return _scale;}
    public String GetComment() {return _comment;}

    private UUID _eventId;
    private TimeZone _eventDate;
    private Integer _count;
    private Integer _scale;
    private String _comment;
}
