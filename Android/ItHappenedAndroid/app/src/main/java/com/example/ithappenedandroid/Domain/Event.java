package com.example.ithappenedandroid.Domain;

import java.time.OffsetDateTime;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Ded on 08.12.2017.
 */

public class Event
{
    public Event(UUID ID, Double count, Scale scale, String comment)
    {
        eventId = ID;
        eventDate = TimeZone.getDefault();
        this.count = count;
        this.scale = scale;
        this.comment = comment;
    }

    public void EditCount(Double count) { count = count;}
    public void EditValueOfScale(Integer scale)
    {
        this.scale.ChangeScaleValue(scale);
    }
    public void EditComment(String comment) { comment = comment;}

    public TimeZone GetEventDate() {return eventDate;}
    public UUID GetEventId() {return eventId;}
    public Double GetCount() {return count;}
    public Scale GetScale() {return scale;}
    public String GetComment() {return comment;}

    private UUID eventId;
    private TimeZone eventDate;
    private Double count;
    private Scale scale;
    private String comment;
}
