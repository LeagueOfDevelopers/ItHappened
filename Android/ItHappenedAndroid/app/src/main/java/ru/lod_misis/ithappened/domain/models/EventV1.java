package ru.lod_misis.ithappened.domain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class EventV1 extends RealmObject {
    public EventV1(UUID eventId, UUID trackingID, Date date,
                   Double scale, Rating rating, String comment,
                   Double lotitude, Double longitude, String photo) {
        this.eventId = eventId.toString();
        this.dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.longitude = longitude;
        this.lotitude = lotitude;
        this.photo = photo;
        this.trackingId = trackingID.toString();
        this.eventDate = date;
    }

    public EventV1(Event event) {
        this.eventId = event.eventId;
        this.eventDate = event.eventDate;
        this.scale = event.scale;
        if (event.rating != null)
            this.rating = new Rating(event.rating.getRating());
        else
            this.rating = null;
        this.comment = event.comment;
        this.trackingId = event.trackingId;
        dateOfChange = event.dateOfChange;
        isDeleted = event.isDeleted;
    }

    public EventV1() {
    }

    public EventV1(UUID eventId, UUID trackingID, Date eventDate,
                   Double scale, Rating rating, String comment, Double lotitude, Double longitude, String photo,
                   boolean status, Date changeDate) {
        this.eventId = eventId.toString();
        this.eventDate = eventDate;
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.lotitude = lotitude;
        this.longitude = longitude;
        this.photo = photo;
        this.trackingId = trackingID.toString();
        dateOfChange = changeDate;
        isDeleted = status;
    }

    public void editDate(Date newDate) {
        eventDate = newDate;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void editScale(Double scale) {
        this.scale = scale;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void editValueOfRating(Rating rating) {
        this.rating = rating;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void editComment(String comment) {
        this.comment = comment;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void editGeoposition(Double lotitude, Double longitude) {
        this.lotitude = lotitude;
        this.longitude = longitude;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void editPhoto(String photo) {
        this.photo = photo;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void removeEvent() {
        isDeleted = true;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }


    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date date) {
        eventDate = date;
    }

    public UUID getEventId() {
        return UUID.fromString(eventId);
    }

    public void setEventId(UUID id) {
        eventId = id.toString();
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scl) {
        scale = scl;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rtng) {
        rating = rtng;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comm) {
        comment = comm;
    }

    public UUID getTrackingId() {
        return UUID.fromString(trackingId);
    }

    public void setTrackingId(UUID id) {
        trackingId = id.toString();
    }

    public Date getDateOfChange() {
        return dateOfChange;
    }

    public void setDateOfChange(Date date) {
        dateOfChange = date;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
    private String photo;
    @Expose
    @SerializedName("dateOfChange")
    private Date dateOfChange;
    @Expose
    @SerializedName("isDeleted")
    private boolean isDeleted = false;
}
