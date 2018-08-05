package ru.lod_misis.ithappened.Domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Ded on 07.06.2018.
 */

public class Tracking extends RealmObject{

    public Tracking(String scaleName, String trackingName,
                    String trackingId, Date trackingDate,
                    String scale, String rating, String comment,String geoposition,
                    RealmList<Event> eventCollection, Date dateOfChange,
                    boolean isDeleted, String color) {
        this.scaleName = scaleName;
        this.trackingName = trackingName;
        this.trackingId = trackingId;
        this.trackingDate = trackingDate;
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.geoposition=geoposition;
        this.eventCollection = eventCollection;
        this.dateOfChange = dateOfChange;
        this.isDeleted = isDeleted;
    }

    public Tracking(){
    }

    @Expose
    @SerializedName("scaleName")
    public String scaleName = "Шкала";
    @Expose
    public String trackingName;
    @Expose
    public String trackingId;
    @Expose
    @SerializedName("trackingDate")
    public Date trackingDate;
    @Expose
    @SerializedName("scale")
    public String scale;
    @Expose
    @SerializedName("rating")
    public String rating;
    @Expose
    @SerializedName("comment")
    public String comment;
    public String geoposition;
    @Expose
    public RealmList<Event> eventCollection;
    @Expose
    public Date dateOfChange;
    @Expose
    public boolean isDeleted = false;
}
