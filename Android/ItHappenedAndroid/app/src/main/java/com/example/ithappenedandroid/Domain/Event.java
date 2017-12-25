package com.example.ithappenedandroid.Domain;

import java.util.TimeZone;
import java.util.UUID;

public class Event
{
    public Event(UUID eventId, UUID trackingID, Double scale, Rating rating, String comment)
    {
        this.eventId = eventId;
        eventDate = TimeZone.getDefault();
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.trackingId = trackingID;
    }

    public void EditDate(TimeZone newDate) { eventDate = newDate; }

    public void EditScale(Double scale) { this.scale = scale; }
    public void EditValueOfRating(Rating rating){ this.rating = rating; }
    public void EditComment(String comment) { this.comment = comment; }

    public TimeZone GetEventDate() {return eventDate;}
    public UUID GetEventId() {return eventId;}
    public Double GetScale() {return scale;}
    public Rating GetRating() {return rating;}
    public String GetComment() {return comment;}
    public UUID GetTrackingId() { return trackingId; }

    private UUID eventId;
    private UUID trackingId;
    private TimeZone eventDate;
    private Double scale;
    private Rating rating;
    private String comment;
}
