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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void AddEvent (Event newEvent)
    {
        CustomizationCheck(newEvent.GetCount(), counter);
        CustomizationCheck(newEvent.GetScale(), scale);
        CustomizationCheck(newEvent.GetComment(), comment);
        eventCollection.add(newEvent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void EditEvent(UUID eventId,
                          Optional<Double> newCount,
                          Optional<Scale> newScale,
                          Optional<String> newComment,
                          Optional<TimeZone> newDate)
    {
        Optional<Event> eventOptional;
        Event editedEvent;
        eventOptional = eventCollection.stream().filter((event -> event.GetEventId() == eventId)).findFirst();
        if (!eventOptional.isPresent())
            throw new IllegalArgumentException("Event with such id doesn't exist");
        editedEvent = eventOptional.get();
        if (ChangesCheck(newCount, counter))
            editedEvent.EditCount(newCount);
        if (ChangesCheck(newScale, scale))
            editedEvent.EditValueOfScale(newScale);
        if (ChangesCheck(newComment, comment))
            editedEvent.EditComment(newComment);
        if (newDate.isPresent())
            editedEvent.EditDate(newDate.get());
        int index = eventCollection.indexOf(eventOptional.get());
        eventCollection.set(index, editedEvent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void EditTracking(Optional<TrackingCustomization> editedCounter,
                             Optional<TrackingCustomization> editedScale,
                             Optional<TrackingCustomization> editedComment,
                             Optional<String> editedTrackingName)
    {
        if (editedCounter.isPresent())
            counter = editedCounter.get();
        if (editedScale.isPresent())
            scale = editedScale.get();
        if (editedComment.isPresent())
            comment = editedComment.get();
        if (editedTrackingName.isPresent())
            trackingName = editedTrackingName.get();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean ChangesCheck(Object value, TrackingCustomization customization)
    {
        if (value != Optional.empty() && customization != TrackingCustomization.None)
            return true;
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void CustomizationCheck(Object value, TrackingCustomization customization)
    {
        if (value == Optional.empty() && customization == TrackingCustomization.Required)
        {
            throw new IllegalArgumentException("Non-optional parameters can not be empty");
        }

        if (value != Optional.empty() && customization == TrackingCustomization.None)
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
