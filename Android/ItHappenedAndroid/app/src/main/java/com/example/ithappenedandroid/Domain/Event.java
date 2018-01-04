package com.example.ithappenedandroid.Domain;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Event
{
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public Event(UUID eventId, UUID trackingID, Double scale, Rating rating, String comment)
=======
    public Event(UUID eventId, UUID trackingID, Optional<Double> count, Optional<Scale> scale, Optional<String> comment)
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
    public Event(UUID eventId, UUID trackingID, Optional<Double> count, Optional<Scale> scale, Optional<String> comment)
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
    public Event(UUID eventId, UUID trackingID, Optional<Double> count, Optional<Scale> scale, Optional<String> comment)
>>>>>>> parent of 525bbbf... removed stream api and optional
    {
        this.eventId = eventId;
        eventDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.trackingId = trackingID;
    }

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public void EditDate(Date newDate) { eventDate = newDate; }
    public void EditScale(Double scale) { this.scale = scale; }
    public void EditValueOfRating(Rating rating){ this.rating = rating; }
    public void EditComment(String comment) { this.comment = comment; }
=======
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
    public void EditDate(TimeZone newDate) { eventDate = newDate; }
    public void EditCount(Optional<Double> count) { this.count = count; }
    public void EditValueOfScale(Optional<Scale> scale){ this.scale = scale; }
    public void EditComment(Optional<String> comment) { this.comment = comment; }
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional

    public Date GetEventDate() {return eventDate;}
    public UUID GetEventId() {return eventId;}
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public Double GetScale() {return scale;}
    public Rating GetRating() {return rating;}
    public String GetComment() {return comment;}
=======
    public Optional<Double> GetCount() {return count;}
    public Optional<Scale> GetScale() {return scale;}
    public Optional<String> GetComment() {return comment;}
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
    public Optional<Double> GetCount() {return count;}
    public Optional<Scale> GetScale() {return scale;}
    public Optional<String> GetComment() {return comment;}
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
    public Optional<Double> GetCount() {return count;}
    public Optional<Scale> GetScale() {return scale;}
    public Optional<String> GetComment() {return comment;}
>>>>>>> parent of 525bbbf... removed stream api and optional
    public UUID GetTrackingId() { return trackingId; }

    private UUID eventId;
    private UUID trackingId;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private Date eventDate;
    private Double scale;
    private Rating rating;
    private String comment;
=======
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
    private TimeZone eventDate;
    private Optional<Double> count;
    private Optional<Scale> scale;
    private Optional<String> comment;
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
}
