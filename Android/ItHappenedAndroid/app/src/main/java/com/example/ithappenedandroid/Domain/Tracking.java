package com.example.ithappenedandroid.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class Tracking {

    public Tracking(String trackingName,
                    UUID trackingId,
                    TrackingCustomization scale,
                    TrackingCustomization rating,
                    TrackingCustomization comment)
    {
        this.trackingName = trackingName;
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.trackingId = trackingId;
        trackingDate = TimeZone.getDefault();
        eventCollection = new ArrayList<Event>();
    }

    public Tracking(String trackingName,
                    UUID trackingId,
                    TrackingCustomization scale,
                    TrackingCustomization rating,
                    TrackingCustomization comment,
                    TimeZone trackingDate,
                    List<Event> eventCollection)
    {
        this.trackingName = trackingName;
        this.scale = scale;
        this.rating = rating;
        this.comment = comment;
        this.trackingId = trackingId;
        this.trackingDate = trackingDate;
        this.eventCollection = eventCollection;
    }


    public void AddEvent (Event newEvent)
    {
        CustomizationCheck(newEvent.GetScale(), scale);
        CustomizationCheck(newEvent.GetRating(), rating);
        CustomizationCheck(newEvent.GetComment(), comment);
        eventCollection.add(newEvent);
    }

    public void EditEvent(UUID eventId,
                          Double newScale,
                          Rating newRating,
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
        if (ChangesCheck(newScale, scale))
            editedEvent.EditScale(newScale);
        if (ChangesCheck(newRating, rating))
            editedEvent.EditValueOfRating(newRating);
        if (ChangesCheck(newComment, comment))
            editedEvent.EditComment(newComment);
        if (newDate!=null)
           // editedEvent.EditDate(newDate);
        eventCollection.set(index, editedEvent);
    }

    public void EditTracking(TrackingCustomization editedScale,
                             TrackingCustomization editedRating,
                             TrackingCustomization editedComment,
                             String editedTrackingName)
    {
        if (editedScale != null)
            scale = editedScale;
        if (editedRating != null)
            rating = editedRating;
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

    public Event GetEvent(UUID eventId)
    {
        for (Event item: eventCollection) {
            if (item.GetEventId().equals(eventId))
                return item;
        }
        throw new IllegalArgumentException("Event with such ID doesn't exist");
    }


    public String GetTrackingName() {return trackingName;}
    public UUID GetTrackingID() {return trackingId;}
    public TimeZone GetTrackingDate () {return trackingDate;}
    public List<Event> GetEventCollection() { return eventCollection;}
    public TrackingCustomization GetScaleCustomization(){ return scale;}
    public TrackingCustomization GetCommentCustomization(){ return comment;}
    public TrackingCustomization GetRatingCustomization(){ return rating;}

    private String trackingName;
    private UUID trackingId;
    private TimeZone trackingDate;
    private TrackingCustomization scale;
    private TrackingCustomization rating;
    private TrackingCustomization comment;

    private List<Event> eventCollection;
}
