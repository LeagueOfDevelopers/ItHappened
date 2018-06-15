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

public class NewTracking extends RealmObject {

    public NewTracking(String trackingName,
                       UUID trackingId,
                       TrackingCustomization scale,
                       TrackingCustomization rating,
                       TrackingCustomization comment,
                       String scaleName,
                       String color)
    {
        this.color = color;
        this.trackingName = trackingName;
        SetScaleCustomization(scale);
        SetRatingCustomization(rating);
        SetCommentCustomization(comment);
        this.trackingId = trackingId.toString();
        trackingDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        dateOfChange = trackingDate;
        newEventCollection = new RealmList<>();
        this.scaleName = scaleName;
    }

    public NewTracking(String trackingName,
                       UUID trackingId,
                       TrackingCustomization scale,
                       TrackingCustomization rating,
                       TrackingCustomization comment,
                       Date trackingDate,
                       List<NewEvent> newEventCollection,
                       boolean status, Date changeDate,
                       String scaleName, String color)
    {
        this.color = color;
        this.trackingName = trackingName;
        SetScaleCustomization(scale);
        SetRatingCustomization(rating);
        SetCommentCustomization(comment);
        this.trackingId = trackingId.toString();
        this.trackingDate = trackingDate;
        this.newEventCollection = new RealmList<>();
        this.newEventCollection.addAll(newEventCollection);
        dateOfChange = changeDate;
        isDeleted = status;
        this.scaleName = scaleName;
    }

    public NewTracking(){
    }

    public NewTracking(Tracking tracking){
        this.color = tracking.color;
        this.trackingName = tracking.trackingName;
        SetScaleCustomization(TrackingCustomization.valueOf(tracking.scale));
        SetRatingCustomization(TrackingCustomization.valueOf(tracking.rating));
        SetCommentCustomization(TrackingCustomization.valueOf(tracking.comment));
        this.trackingId = tracking.trackingId;
        this.trackingDate = tracking.trackingDate;
        newEventCollection = new RealmList<>();
        for (Event ev: tracking.eventCollection) {
            newEventCollection.add(new NewEvent(ev));
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

    public void AddEvent (NewEvent newNewEvent)
    {
        CustomizationCheck(newNewEvent.getScale(), GetScaleCustomization());
        CustomizationCheck(newNewEvent.getRating(), GetRatingCustomization());
        CustomizationCheck(newNewEvent.getComment(), GetCommentCustomization());
        newEventCollection.add(newNewEvent);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void RemoveEvent (UUID eventID)
    {
        Integer deletionEvent = null;
        Integer i=0;
        for (NewEvent newEvent : newEventCollection) {
            if (newEvent.GetEventId().equals(eventID))
                deletionEvent =i;
            i++;
        }
        if (deletionEvent == null)
            throw new IllegalArgumentException ("NewEvent with such id dosn't exist");
        NewEvent newEvent = newEventCollection.get(deletionEvent);
        newEvent.RemoveEvent();
        newEventCollection.set(deletionEvent, newEvent);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void EditEvent(UUID eventId,
                          Double newScale,
                          Rating newRating,
                          String newComment,
                          Date newDate)
    {
        NewEvent editedNewEvent = null;
        int index = 0;
        boolean contains = false;
        for (NewEvent newEvent : newEventCollection)
        {
            if (newEvent.GetEventId().equals(eventId))
            {
                contains = true;
                editedNewEvent = newEvent;
                break;
            }
            index++;
        }
        if (!contains)
            throw new IllegalArgumentException("NewEvent with such id doesn't exist");
        if (ChangesCheck(newScale, GetScaleCustomization()))
            editedNewEvent.EditScale(newScale);
        if (ChangesCheck(newRating, GetRatingCustomization()))
            editedNewEvent.EditValueOfRating(newRating);
        if (ChangesCheck(newComment, GetCommentCustomization()))
            editedNewEvent.EditComment(newComment);
        if (newDate!=null)
            editedNewEvent.EditDate(newDate);
        newEventCollection.set(index, editedNewEvent);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void EditTracking(TrackingCustomization editedScale,
                             TrackingCustomization editedRating,
                             TrackingCustomization editedComment,
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

    public RealmList<NewEvent> getNewEventCollection() {
        return newEventCollection;
    }

    public void setNewEventCollection(RealmList<NewEvent> newEventCollection) {
        this.newEventCollection = newEventCollection;
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

    public NewEvent GetEvent(UUID eventId)
    {
        for (NewEvent item: newEventCollection) {
            if (item.GetEventId().equals(eventId))
                return item;
        }
        throw new IllegalArgumentException("NewEvent with such ID doesn't exist");
    }

    public void DeleteTracking()
    {
        isDeleted = true;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
        for (NewEvent newEvent : newEventCollection) {
            newEvent.RemoveEvent();
        }
    }

    public String GetTrackingName() {return trackingName;}
    public UUID GetTrackingID() {return UUID.fromString(trackingId);}
    public Date GetTrackingDate () {return trackingDate;}
    public List<NewEvent> GetEventCollection() {
        Collections.sort(newEventCollection, new Comparator<NewEvent>() {
            @Override
            public int compare(NewEvent newEvent, NewEvent t1) {
               return t1.GetEventDate().compareTo(newEvent.GetEventDate());
            }
        });
        return newEventCollection;
    }
    public TrackingCustomization GetScaleCustomization(){ return TrackingCustomization.valueOf(scale);}
    public TrackingCustomization GetCommentCustomization(){ return TrackingCustomization.valueOf(comment);}
    public TrackingCustomization GetRatingCustomization(){ return TrackingCustomization.valueOf(rating);}
    public Date GetDateOfChange() {return dateOfChange; }
    public boolean GetStatus() { return isDeleted; }
    public String getScaleName() { return scaleName; }

    public void SetTrackingName(String name) { trackingName = name;}
    public void SetTrackingID(UUID id) { trackingId = id.toString();}
    public void SetTrackingDate (Date date) { trackingDate = date;}
    public void SetEventCollection(List<NewEvent> newEventList) {
        newEventCollection = new RealmList<>();
        newEventCollection.addAll(newEventList);
    }
    public void SetScaleCustomization(TrackingCustomization scl){  scale = scl.toString();}
    public void SetCommentCustomization(TrackingCustomization comm){ comment = comm.toString();}
    public void SetRatingCustomization(TrackingCustomization rat){ rating = rat.toString();}
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
    @Expose
    private RealmList<NewEvent> newEventCollection;
    @Expose
    private Date dateOfChange;
    @Expose
    private boolean isDeleted = false;
    @Expose
    private String color;
}
