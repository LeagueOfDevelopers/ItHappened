package com.example.ithappenedandroid.Domain;

import java.util.ArrayList;
import java.util.Date;
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void AddEvent (Event newEvent)
    {
        CustomizationCheck(newEvent.GetScale(), scale);
        CustomizationCheck(newEvent.GetRating(), rating);
        CustomizationCheck(newEvent.GetComment(), comment);
        eventCollection.add(newEvent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void EditEvent(UUID eventId,
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                          Double newScale,
                          Rating newRating,
                          String newComment,
                          Date newDate)
=======
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
                          Optional<Double> newCount,
                          Optional<Scale> newScale,
                          Optional<String> newComment,
                          Optional<TimeZone> newDate)
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
    {
        Optional<Event> eventOptional;
        Event editedEvent;
        eventOptional = eventCollection.stream().filter((event -> event.GetEventId() == eventId)).findFirst();
        if (!eventOptional.isPresent())
            throw new IllegalArgumentException("Event with such id doesn't exist");
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
        editedEvent = eventOptional.get();
        if (ChangesCheck(newCount, counter))
            editedEvent.EditCount(newCount);
>>>>>>> parent of 525bbbf... removed stream api and optional
        if (ChangesCheck(newScale, scale))
            editedEvent.EditScale(newScale);
        if (ChangesCheck(newRating, rating))
            editedEvent.EditValueOfRating(newRating);
        if (ChangesCheck(newComment, comment))
            editedEvent.EditComment(newComment);
        if (newDate.isPresent())
            editedEvent.EditDate(newDate.get());
        int index = eventCollection.indexOf(eventOptional.get());
        eventCollection.set(index, editedEvent);
    }

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void EditTracking(Optional<TrackingCustomization> editedCounter,
                             Optional<TrackingCustomization> editedScale,
                             Optional<TrackingCustomization> editedComment,
                             Optional<String> editedTrackingName)
    {
=======
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void EditTracking(Optional<TrackingCustomization> editedCounter,
                             Optional<TrackingCustomization> editedScale,
                             Optional<TrackingCustomization> editedComment,
                             Optional<String> editedTrackingName)
    {
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void EditTracking(Optional<TrackingCustomization> editedCounter,
                             Optional<TrackingCustomization> editedScale,
                             Optional<TrackingCustomization> editedComment,
                             Optional<String> editedTrackingName)
    {
>>>>>>> parent of 525bbbf... removed stream api and optional
        if (editedCounter.isPresent())
            counter = editedCounter.get();
        if (editedScale.isPresent())
            scale = editedScale.get();
        if (editedComment.isPresent())
            comment = editedComment.get();
        if (editedTrackingName.isPresent())
            trackingName = editedTrackingName.get();
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
=======
>>>>>>> parent of 525bbbf... removed stream api and optional
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
