package ru.lod_misis.ithappened.Domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Tracking extends RealmObject {

    public Tracking(String trackingName,
                    UUID trackingId,
                    TrackingCustomization scale,
                    TrackingCustomization rating,
                    TrackingCustomization comment)
    {
        this.trackingName = trackingName;
        SetScaleCustomization(scale);
        SetRatingCustomization(rating);
        SetCommentCustomization(comment);
        this.trackingId = trackingId.toString();
        trackingDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        eventCollection = new RealmList<>();
    }

    public Tracking(String trackingName,
                    UUID trackingId,
                    TrackingCustomization scale,
                    TrackingCustomization rating,
                    TrackingCustomization comment,
                    Date trackingDate,
                    List<Event> eventCollection,
                    boolean status, Date changeDate)
    {
        this.trackingName = trackingName;
        SetScaleCustomization(scale);
        SetRatingCustomization(rating);
        SetCommentCustomization(comment);
        this.trackingId = trackingId.toString();
        this.trackingDate = trackingDate;
        this.eventCollection = new RealmList<>();
        this.eventCollection.addAll(eventCollection);
        dateOfChange = changeDate;
        isDeleted = status;
    }

    public Tracking(){

    }


    public void AddEvent (Event newEvent)
    {
        CustomizationCheck(newEvent.getScale(), GetScaleCustomization());
        CustomizationCheck(newEvent.getRating(), GetRatingCustomization());
        CustomizationCheck(newEvent.getComment(), GetCommentCustomization());
        eventCollection.add(newEvent);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void RemoveEvent (UUID eventID)
    {
        Integer deletionEvent = null;
        Integer i=0;
        for (Event event: eventCollection) {
            if (event.GetEventId().equals(eventID))
                deletionEvent =i;
            i++;
        }
        if (deletionEvent == null)
            throw new IllegalArgumentException ("Event with such id dosn't exist");
        Event event = eventCollection.get(deletionEvent);
        event.RemoveEvent();
        eventCollection.set(deletionEvent, event);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void EditEvent(UUID eventId,
                          Double newScale,
                          Rating newRating,
                          String newComment,
                          Date newDate)
    {
        Event editedEvent = null;
        int index = 0;
        boolean contains = false;
        for (Event event: eventCollection)
        {
            if (event.GetEventId().equals(eventId))
            {
                contains = true;
                editedEvent = event;
                break;
            }
            index++;
        }
        if (!contains)
            throw new IllegalArgumentException("Event with such id doesn't exist");
        if (ChangesCheck(newScale, GetScaleCustomization()))
            editedEvent.EditScale(newScale);
        if (ChangesCheck(newRating, GetRatingCustomization()))
            editedEvent.EditValueOfRating(newRating);
        if (ChangesCheck(newComment, GetCommentCustomization()))
            editedEvent.EditComment(newComment);
        if (newDate!=null)
            editedEvent.EditDate(newDate);
        eventCollection.set(index, editedEvent);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void EditTracking(TrackingCustomization editedScale,
                             TrackingCustomization editedRating,
                             TrackingCustomization editedComment,
                             String editedTrackingName)
    {
        if (editedScale != null)
            SetScaleCustomization(editedScale);
        if (editedRating != null)
            SetRatingCustomization(editedRating);
        if (editedComment != null)
            SetCommentCustomization(editedComment);
        if (editedTrackingName != null)
            trackingName = editedTrackingName;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    private boolean ChangesCheck(Object value, TrackingCustomization customization)
    {
        if (value != null && customization != TrackingCustomization.None)
            return true;
        return false;
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

    public RealmList<Event> getEventCollection() {
        return eventCollection;
    }

    public void setEventCollection(RealmList<Event> eventCollection) {
        this.eventCollection = eventCollection;
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

    public Event GetEvent(UUID eventId)
    {
        for (Event item: eventCollection) {
            if (item.GetEventId().equals(eventId))
                return item;
        }
        throw new IllegalArgumentException("Event with such ID doesn't exist");
    }

    public void DeleteTracking()
    {
        isDeleted = true;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
        for (Event event: eventCollection) {
            event.RemoveEvent();
        }
    }

    public String GetTrackingName() {return trackingName;}
    public UUID GetTrackingID() {return UUID.fromString(trackingId);}
    public Date GetTrackingDate () {return trackingDate;}
    public List<Event> GetEventCollection() { return eventCollection;}
    public TrackingCustomization GetScaleCustomization(){ return TrackingCustomization.valueOf(scale);}
    public TrackingCustomization GetCommentCustomization(){ return TrackingCustomization.valueOf(comment);}
    public TrackingCustomization GetRatingCustomization(){ return TrackingCustomization.valueOf(rating);}
    public Date GetDateOfChange() {return dateOfChange; }
    public boolean GetStatus() { return isDeleted; }

    public void SetTrackingName(String name) { trackingName = name;}
    public void SetTrackingID(UUID id) { trackingId = id.toString();}
    public void SetTrackingDate (Date date) { trackingDate = date;}
    public void SetEventCollection(List<Event> eventList) {
        eventCollection = new RealmList<>();
        eventCollection.addAll(eventList);
    }
    public void SetScaleCustomization(TrackingCustomization scl){  scale = scl.toString();}
    public void SetCommentCustomization(TrackingCustomization comm){ comment = comm.toString();}
    public void SetRatingCustomization(TrackingCustomization rat){ rating = rat.toString();}
    public void SetDateOfChange(Date date) { dateOfChange = date; }
    public void SetStatus(boolean status) { isDeleted = status; }

    @Expose
    private String trackingName;
    @PrimaryKey
    @Expose
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
    @Expose
    private RealmList<Event> eventCollection;
    @Expose
    private Date dateOfChange;
    @Expose
    private boolean isDeleted = false;
}
