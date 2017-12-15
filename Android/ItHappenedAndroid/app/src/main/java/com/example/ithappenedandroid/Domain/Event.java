package com.example.ithappenedandroid.Domain;

import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

public class Event
{
    public Event(UUID eventId, Optional<Double> count, Optional<Scale> scale, Optional<String> comment)
    {
        this.eventId = eventId;
        eventDate = TimeZone.getDefault();
        this.count = count;
        this.scale = scale;
        this.comment = comment;
    }

    public void EditCount(Optional<Double> count) { this.count = count;}
    public void EditValueOfScale(Optional<Scale> scale){ this.scale = scale; }
    public void EditComment(Optional<String> comment) { this.comment = comment;}

    public TimeZone GetEventDate() {return eventDate;}
    public UUID GetEventId() {return eventId;}
    public Optional<Double> GetCount() {return count;}
    public Optional<Scale> GetScale() {return scale;}
    public Optional<String> GetComment() {return comment;}

    private UUID eventId;
    private TimeZone eventDate;
    private Optional<Double> count;
    private Optional<Scale> scale;
    private Optional<String> comment;
}
