package com.example.ithappenedandroid.Domain;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

public class Tracking {

    public Tracking(String trackingName,
                    UUID trackingId,
                    TrackingCustomization counter,
                    TrackingCustomization scale,
                    TrackingCustomization comment)
    {
        this.trackingName = trackingName;
        this.counter = counter;
        this.scale = scale;
        this.comment = comment;
        this.trackingId = trackingId;
        trackingDate = TimeZone.getDefault();
        eventCollection = new ArrayList<Event>();
    }

    public Tracking(String trackingName,
                    UUID trackingId,
                    TrackingCustomization counter,
                    TrackingCustomization scale,
                    TrackingCustomization comment,
                    TimeZone trackingDate,
                    List<Event> eventCollection)
    {
        this.trackingName = trackingName;
        this.counter = counter;
        this.scale = scale;
        this.comment = comment;
        this.trackingId = trackingId;
        this.trackingDate = trackingDate;
        this.eventCollection = eventCollection;
    }


    public void AddEvent (Event newEvent)
    {
        CustomizationCheck(newEvent.GetCount(), counter);
        CustomizationCheck(newEvent.GetScale(), scale);
        CustomizationCheck(newEvent.GetComment(), comment);
        eventCollection.add(newEvent);
    }

    public void EditEvent(UUID eventId,
                          Double newCount,
                          Scale newScale,
                          String newComment,
                          TimeZone newDate)
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
        if (ChangesCheck(newCount, counter))
            editedEvent.EditCount(newCount);
        if (ChangesCheck(newScale, scale))
            editedEvent.EditValueOfScale(newScale);
        if (ChangesCheck(newComment, comment))
            editedEvent.EditComment(newComment);
        if (newDate!=null)
            editedEvent.EditDate(newDate);
        eventCollection.set(index, editedEvent);
    }

    public void EditTracking(TrackingCustomization editedCounter,
                             TrackingCustomization editedScale,
                             TrackingCustomization editedComment,
                             String editedTrackingName)
    {
        if (editedCounter != null)
            counter = editedCounter;
        if (editedScale != null)
            scale = editedScale;
        if (editedComment != null)
            comment = editedComment;
        if (editedTrackingName != null)
            trackingName = editedTrackingName;
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

    public String GetTrackingName() {return trackingName;}
    public UUID GetTrackingID() {return trackingId;}
    public TimeZone GetTrackingDate () {return trackingDate;}
    public List<Event> GetEventCollection() { return eventCollection;}
    public TrackingCustomization GetCounterCustomization(){ return counter;}
    public TrackingCustomization GetCommentCustomization(){ return comment;}
    public TrackingCustomization GetScaleCustomization(){ return scale;}

    private String trackingName;
    private UUID trackingId;
    private TimeZone trackingDate;
    private TrackingCustomization counter;
    private TrackingCustomization scale;
    private TrackingCustomization comment;

    private List<Event> eventCollection;
}
