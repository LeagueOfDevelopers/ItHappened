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
    public Date GetDateOfChange() {return dateOfChange; }
    public boolean GetStatus() { return isDeleted; }

    public void SetEventDate(Date date) { eventDate = date ;}
    public void SetEventId(UUID id) { eventId = id; }
    public void SetScale(Double scl) { scale = scl;}
    public void SetRating(Rating  rtng) { rating = rtng; }
    public void SetComment(String comm) { comment = comm; }
    public void SetTrackingId(UUID id) { trackingId = id; }
    public void SetDateOfChange(Date date) { dateOfChange = date; }
    public void SetStatus(boolean status) { isDeleted = status; }

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
    @Expose
    private Date dateOfChange;
    @Expose
    private boolean isDeleted = false;
}
