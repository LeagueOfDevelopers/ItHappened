package com.example.ithappenedandroid.Domain;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Tracking extends RealmObject {

    public Tracking(){}

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
        eventCollection = new ArrayList<Event>();
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
        this.eventCollection = eventCollection;
        dateOfChange = changeDate;
        isDeleted = status;
    }


    public void AddEvent (Event newEvent)
    {
        CustomizationCheck(newEvent.GetScale(), GetScaleCustomization());
        CustomizationCheck(newEvent.GetRating(), GetRatingCustomization());
        CustomizationCheck(newEvent.GetComment(), GetCommentCustomization());
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
    public void SetEventCollection(List<Event> eventList) { eventCollection = eventList;}
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
    private Date trackingDate;
    @Expose
    private String scale;
    @Expose
    private String rating;
    @Expose
    private String comment;
    @Expose
    private List<Event> eventCollection;
    @Expose
    private Date dateOfChange;
    @Expose
    private boolean isDeleted = false;
}
