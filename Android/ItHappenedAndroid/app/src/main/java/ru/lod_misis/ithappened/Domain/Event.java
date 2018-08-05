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
import io.realm.annotations.Required;

/**
 * Created by Ded on 07.06.2018.
 */

public class Event extends RealmObject {

    public Event(UUID eventId, UUID trackingID, Date date, Double scale, Rating rating, String comment,Double lotitude,Double longitude)
    {
        this.eventId = eventId.toString();
        this.dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.lotitude=lotitude;
        this.longitude=longitude;
        this.trackingId = trackingID.toString();
        this.eventDate = date;
    }

    public Event(){}

    @Expose
    @SerializedName("eventId")
    public String eventId;
    @Expose
    @SerializedName("trackingId")
    public String trackingId;
    @Expose
    @SerializedName("eventDate")
    public Date eventDate;
    @Expose
    @SerializedName("scale")
    public Double scale;
    @Expose
    @SerializedName("rating")
    public Rating rating;
    @Expose
    @SerializedName("comment")
    public String comment;
    public Double lotitude;
    public Double longitude;
    @Expose
    @SerializedName("dateOfChange")
    public Date dateOfChange;
    @Expose
    @SerializedName("isDeleted")
    public boolean isDeleted = false;
}
