package com.example.ithappenedandroid.Domain;

import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

public class Event
{
    public Event(UUID eventId, UUID trackingID, Double count, Scale scale, String comment)
    {
        this.eventId = eventId;
        eventDate = TimeZone.getDefault();
        this.count = count;
        this.scale = scale;
        this.comment = comment;
        this.trackingId = trackingID;
    }

    public void EditDate(TimeZone newDate) { eventDate = newDate; }
    public void EditCount(Double count) { this.count = count; }
    public void EditValueOfScale(Scale scale){ this.scale = scale; }
    public void EditComment(String comment) { this.comment = comment; }

    public TimeZone GetEventDate() {return eventDate;}
    public UUID GetEventId() {return eventId;}
    public Double GetCount() {return count;}
    public Scale GetScale() {return scale;}
    public String GetComment() {return comment;}
    public UUID GetTrackingId() { return trackingId; }

    private UUID eventId;
    private UUID trackingId;
    private TimeZone eventDate;
    private Double count;
    private Scale scale;
    private String comment;
}
