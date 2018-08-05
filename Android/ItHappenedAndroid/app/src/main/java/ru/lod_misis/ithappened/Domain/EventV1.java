package ru.lod_misis.ithappened.Domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class EventV1 extends RealmObject
{
    public EventV1(UUID eventId, UUID trackingID, Date date, Double scale, Rating rating, String comment,Double lotitude,Double longitude)
    {
        this.eventId = eventId.toString();
        this.dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.longitude=longitude;
        this.lotitude=lotitude;
        this.trackingId = trackingID.toString();
        this.eventDate = date;
    }

    public EventV1(Event event){
        this.eventId = event.eventId;
        this.eventDate = event.eventDate;
        this.scale = event.scale;
        if(event.rating != null)
            this.rating = new Rating(event.rating.GetRatingValue());
        else
            this.rating = null;
        this.comment = event.comment;
        this.lotitude=event.lotitude;
        this.longitude=event.longitude;
        this.trackingId = event.trackingId;
        dateOfChange = event.dateOfChange;
        isDeleted = event.isDeleted;
    }

    public EventV1(){}

    public EventV1(UUID eventId, UUID trackingID, Date eventDate,
                   Double scale, Rating rating, String comment,Double lotitude,Double longitude,
                   boolean status, Date changeDate)
    {
        this.eventId = eventId.toString();
        this.eventDate = eventDate;
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.lotitude=lotitude;
        this.longitude=longitude;
        this.trackingId = trackingID.toString();
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
        dateOfChange =Calendar.getInstance(TimeZone.getDefault()).getTime();
    }
    public void EditComment(String comment) {
        this.comment = comment;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }
    public void EditGeoposition(Double lotitude,Double longitude) {
        this.lotitude=lotitude;
        this.longitude=longitude;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }
    public void RemoveEvent()
    {
        isDeleted = true;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }


    public Date GetEventDate() {return eventDate;}
    public UUID GetEventId() {return UUID.fromString(eventId);}
    public Double GetScale() {return scale;}
    public Rating GetRating() {return rating;}
    public String GetComment() {return comment;}
    public UUID GetTrackingId() { return UUID.fromString(trackingId); }
    public Date GetDateOfChange() {return dateOfChange; }
    public boolean GetStatus() { return isDeleted; }

    public void SetEventDate(Date date) { eventDate = date ;}
    public void SetEventId(UUID id) { eventId = id.toString(); }
    public void SetScale(Double scl) { scale = scl;}
    public void SetRating(Rating  rtng) { rating = rtng; }
    public void SetComment(String comm) { comment = comm; }
    public void SetTrackingId(UUID id) { trackingId = id.toString(); }
    public void SetDateOfChange(Date date) { dateOfChange = date; }
    public void SetStatus(boolean status) { isDeleted = status; }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getLotitude() {
        return lotitude;
    }

    public void setLotitude(Double lotitude) {
        this.lotitude = lotitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getDateOfChange() {
        return dateOfChange;
    }

    public void setDateOfChange(Date dateOfChange) {
        this.dateOfChange = dateOfChange;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Expose
    @SerializedName("eventId")
    @PrimaryKey
    private String eventId;
    @Expose
    @SerializedName("trackingId")
    private String trackingId;
    @Expose
    @SerializedName("eventDate")
    @Index
    private Date eventDate;
    @Expose
    @SerializedName("scale")
    private Double scale;
    @Expose
    @SerializedName("rating")
    private Rating rating;
    @Expose
    @SerializedName("comment")
    private String comment;
    private Double lotitude;
    private Double longitude;
    @Expose
    @SerializedName("dateOfChange")
    private Date dateOfChange;
    @Expose
    @SerializedName("isDeleted")
    private boolean isDeleted = false;
}
