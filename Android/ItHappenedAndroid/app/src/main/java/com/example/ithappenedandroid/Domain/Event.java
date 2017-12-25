package com.example.ithappenedandroid.Domain;

import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

public class Event
{
    public Event(UUID eventId, UUID trackingID, Optional<Double> count, Optional<Rating> rating, Optional<String> comment)
    {
        this.eventId = eventId;
        eventDate = TimeZone.getDefault();
        this.count = count;
        this.rating = rating;
        this.comment = comment;
        this.trackingId = trackingID;
    }

    public void EditDate(TimeZone newDate) { eventDate = newDate; }
    public void EditCount(Optional<Double> count) { this.count = count; }
    public void EditValueOfRating(Optional<Rating> rating){ this.rating = rating; }
    public void EditComment(Optional<String> comment) { this.comment = comment; }

    public TimeZone GetEventDate() {return eventDate;}
    public UUID GetEventId() {return eventId;}
    public Optional<Double> GetCount() {return count;}
    public Optional<Rating> GetRating() {return rating;}
    public Optional<String> GetComment() {return comment;}
    public UUID GetTrackingId() { return trackingId; }

    private UUID eventId;
    private UUID trackingId;
    private TimeZone eventDate;
    private Optional<Double> count;
    private Optional<Rating> rating;
    private Optional<String> comment;
}
