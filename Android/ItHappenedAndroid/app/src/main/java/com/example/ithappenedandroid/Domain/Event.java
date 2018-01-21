package com.example.ithappenedandroid.Domain;

import com.google.gson.annotations.Expose;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Event
{
    public Event(UUID eventId, UUID trackingID, Double scale, Rating rating, String comment)
    {
        this.eventId = eventId;
        eventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.trackingId = trackingID;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public Event(UUID eventId, UUID trackingID,
                 Double scale, Rating rating, String comment,
                 boolean status, Date changeDate)
    {
        this.eventId = eventId;
        eventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.trackingId = trackingID;
        dateOfChange = changeDate;
        isDeleted = status;
    }

    public void EditDate(Date newDate) {
        eventDate = newDate;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void EditScale(Double scale) {
        this.scale = scale;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }
    public void EditValueOfRating(Rating rating){
        this.rating = rating;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }
    public void EditComment(String comment) {
        this.comment = comment;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void RemoveEvent()
    {
        isDeleted = true;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }


    public Date GetEventDate() {return eventDate;}
    public UUID GetEventId() {return eventId;}
    public Double GetScale() {return scale;}
    public Rating GetRating() {return rating;}
    public String GetComment() {return comment;}
    public UUID GetTrackingId() { return trackingId; }

    @Expose
    private UUID eventId;
    @Expose
    private UUID trackingId;
    @Expose
    private Date eventDate;
    @Expose
    private Double scale;
    @Expose
    private Rating rating;
    @Expose
    private String comment;
    private Date dateOfChange;
    private boolean isDeleted = false;
}
