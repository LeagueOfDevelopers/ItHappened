package ru.lod_misis.ithappened.Domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TrackingV1 extends RealmObject {

    public TrackingV1(String trackingName,
                      UUID trackingId,
                      TrackingCustomization scale,
                      TrackingCustomization rating,
                      TrackingCustomization comment,
                      TrackingCustomization geoposition,
                      String scaleName,
                      String color)
    {
        this.color = color;
        this.trackingName = trackingName;
        SetScaleCustomization(scale);
        SetRatingCustomization(rating);
        SetCommentCustomization(comment);
        SetGeopositionCustomization(geoposition);
        this.trackingId = trackingId.toString();
        trackingDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        dateOfChange = trackingDate;
        eventV1Collection = new RealmList<>();
        this.scaleName = scaleName;
    }

    public TrackingV1(String trackingName,
                      UUID trackingId,
                      TrackingCustomization scale,
                      TrackingCustomization rating,
                      TrackingCustomization comment,
                      TrackingCustomization geoposition,
                      Date trackingDate,
                      List<EventV1> eventV1Collection,
                      boolean status, Date changeDate,
                      String scaleName, String color)
    {
        this.color = color;
        this.trackingName = trackingName;
        SetScaleCustomization(scale);
        SetRatingCustomization(rating);
        SetCommentCustomization(comment);
        SetGeopositionCustomization(geoposition);
        this.trackingId = trackingId.toString();
        this.trackingDate = trackingDate;
        this.eventV1Collection = new RealmList<>();
        this.eventV1Collection.addAll(eventV1Collection);
        dateOfChange = changeDate;
        isDeleted = status;
        this.scaleName = scaleName;
    }

    public TrackingV1(){
    }

    public TrackingV1(Tracking tracking){
        this.color = "-5658199";
        this.trackingName = tracking.trackingName;
        SetScaleCustomization(TrackingCustomization.valueOf(tracking.scale));
        SetRatingCustomization(TrackingCustomization.valueOf(tracking.rating));
        SetCommentCustomization(TrackingCustomization.valueOf(tracking.comment));
        SetGeopositionCustomization(TrackingCustomization.valueOf((tracking.geoposition)));
        this.trackingId = tracking.trackingId;
        this.trackingDate = tracking.trackingDate;
        eventV1Collection = new RealmList<>();
        for (Event ev: tracking.eventCollection) {
            eventV1Collection.add(new EventV1(ev));
        }
        dateOfChange = tracking.dateOfChange;
        isDeleted = tracking.isDeleted;
        this.scaleName = tracking.scaleName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void AddEvent (EventV1 newEventV1)
    {
        CustomizationCheck(newEventV1.getScale(), GetScaleCustomization());
        CustomizationCheck(newEventV1.getRating(), GetRatingCustomization());
        CustomizationCheck(newEventV1.getComment(), GetCommentCustomization());
        CustomizationCheck(newEventV1.getGeoposition(), GetGeopositionCustomization());
        eventV1Collection.add(newEventV1);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void RemoveEvent (UUID eventID)
    {
        Integer deletionEvent = null;
        Integer i=0;
        for (EventV1 eventV1 : eventV1Collection) {
            if (eventV1.GetEventId().equals(eventID))
                deletionEvent =i;
            i++;
        }
        if (deletionEvent == null)
            throw new IllegalArgumentException ("EventV1 with such id dosn't exist");
        EventV1 eventV1 = eventV1Collection.get(deletionEvent);
        eventV1.RemoveEvent();
        eventV1Collection.set(deletionEvent, eventV1);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void EditEvent(UUID eventId,
                          Double newScale,
                          Rating newRating,
                          String newComment,
                          String newGeoposition,
                          Date newDate)
    {
        EventV1 editedEventV1 = null;
        int index = 0;
        boolean contains = false;
        for (EventV1 eventV1 : eventV1Collection)
        {
            if (eventV1.GetEventId().equals(eventId))
            {
                contains = true;
                editedEventV1 = eventV1;
                break;
            }
            index++;
        }
        if (!contains)
            throw new IllegalArgumentException("EventV1 with such id doesn't exist");
        if (ChangesCheck(newScale, GetScaleCustomization()))
            editedEventV1.EditScale(newScale);
        if (ChangesCheck(newRating, GetRatingCustomization()))
            editedEventV1.EditValueOfRating(newRating);
        if (ChangesCheck(newComment, GetCommentCustomization()))
            editedEventV1.EditComment(newComment);
        if (ChangesCheck(newGeoposition, GetGeopositionCustomization()))
            editedEventV1.EditGeoposition(newGeoposition);
        if (newDate!=null)
            editedEventV1.EditDate(newDate);
        eventV1Collection.set(index, editedEventV1);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void EditTracking(TrackingCustomization editedScale,
                             TrackingCustomization editedRating,
                             TrackingCustomization editedComment,
                             TrackingCustomization editedGeoposition,
                             String editedTrackingName,
                             String scaleName, String color)
    {
        if(scaleName != null)
            setScaleName(scaleName);
        if (color != null)
            this.color = color;
        if (editedScale != null)
            SetScaleCustomization(editedScale);
        if (editedRating != null)
            SetRatingCustomization(editedRating);
        if (editedComment != null)
            SetCommentCustomization(editedComment);
        if (editedGeoposition != null)
            SetGeopositionCustomization(editedGeoposition);
        if (editedTrackingName != null)
            trackingName = editedTrackingName;
        if (color != null) this.color = color;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    private boolean ChangesCheck(Object value, TrackingCustomization customization)
    {
        return value != null && customization != TrackingCustomization.None;
    }

    private void CustomizationCheck(Object value, TrackingCustomization customization)
    {
        if (value == null && customization == TrackingCustomization.Required)
        {
            throw new IllegalArgumentException("Non-optional parameters can not be empty");
        }

        if (value != null && customization == TrackingCustomization.None)
        {
            throw new IllegalArgumentException("None customizations can not take a value");
        }
    }

    public String getTrackingName() {
        return trackingName;
    }

    public void setTrackingName(String trackingName) {
        this.trackingName = trackingName;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Date getTrackingDate() {
        return trackingDate;
    }

    public void setTrackingDate(Date trackingDate) {
        this.trackingDate = trackingDate;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RealmList<EventV1> getEventV1Collection() {
        return eventV1Collection;
    }

    public void setEventV1Collection(RealmList<EventV1> eventV1Collection) {
        this.eventV1Collection = eventV1Collection;
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

    public EventV1 GetEvent(UUID eventId)
    {
        for (EventV1 item: eventV1Collection) {
            if (item.GetEventId().equals(eventId))
                return item;
        }
        throw new IllegalArgumentException("EventV1 with such ID doesn't exist");
    }

    public void DeleteTracking()
    {
        isDeleted = true;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
        for (EventV1 eventV1 : eventV1Collection) {
            eventV1.RemoveEvent();
        }
    }

    public String GetTrackingName() {return trackingName;}
    public UUID GetTrackingID() {return UUID.fromString(trackingId);}
    public Date GetTrackingDate () {return trackingDate;}
    public List<EventV1> GetEventHistory() {
        if(eventV1Collection!=null) {
            Collections.sort(eventV1Collection, new Comparator<EventV1>() {
                @Override
                public int compare(EventV1 eventV1, EventV1 t1) {
                    return t1.GetEventDate().compareTo(eventV1.GetEventDate());
                }
            });
        }
        return eventV1Collection;
    }
    public TrackingCustomization GetScaleCustomization(){ return TrackingCustomization.valueOf(scale);}
    public TrackingCustomization GetCommentCustomization(){ return TrackingCustomization.valueOf(comment);}
    public TrackingCustomization GetRatingCustomization(){ return TrackingCustomization.valueOf(rating);}
    public TrackingCustomization GetGeopositionCustomization(){ return TrackingCustomization.valueOf(geoposition);}
    public Date GetDateOfChange() {return dateOfChange; }
    public boolean GetStatus() { return isDeleted; }
    public String getScaleName() { return scaleName; }

    public void SetTrackingName(String name) { trackingName = name;}
    public void SetTrackingID(UUID id) { trackingId = id.toString();}
    public void SetTrackingDate (Date date) { trackingDate = date;}
    public void SetEventCollection(List<EventV1> eventV1List) {
        eventV1Collection = new RealmList<>();
        eventV1Collection.addAll(eventV1List);
    }
    public void SetScaleCustomization(TrackingCustomization scl){  scale = scl.toString();}
    public void SetCommentCustomization(TrackingCustomization comm){ comment = comm.toString();}
    public void SetRatingCustomization(TrackingCustomization rat){ rating = rat.toString();}
    public void SetGeopositionCustomization(TrackingCustomization geo){ geoposition = geo.toString();}
    public void SetDateOfChange(Date date) { dateOfChange = date; }
    public void SetStatus(boolean status) { isDeleted = status; }
    public void setScaleName(String name) {scaleName = name;}


    @Expose
    @SerializedName("scaleName")
    private String scaleName = "Шкала";
    @Expose
    private String trackingName;
    @Expose
    @PrimaryKey
    private String trackingId;
    @Expose
    @SerializedName("trackingDate")
    private Date trackingDate;
    @Expose
    @SerializedName("scale")
    private String scale;
    @Expose
    @SerializedName("rating")
    private String rating;
    @Expose
    @SerializedName("comment")
    private String comment;
    private String geoposition;
    @Expose
    @SerializedName("eventCollection")
    private RealmList<EventV1> eventV1Collection;
    @Expose
    private Date dateOfChange;
    @Expose
    private boolean isDeleted = false;
    @Expose
    private String color;
}
