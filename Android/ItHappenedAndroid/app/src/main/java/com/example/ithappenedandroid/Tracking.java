package com.example.ithappenedandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class Tracking {

    public Tracking(String trackingName,
                    TrackingCustomization counter,
                    TrackingCustomization scale,
                    TrackingCustomization comment)
    {
        this.trackingName = trackingName;
        this.counter = counter;
        this.scale = scale;
        this.comment = comment;
        trackingId = UUID.randomUUID();
        trackingDate = TimeZone.getDefault();
        eventCollection = new ArrayList<Event>();
    }

    public void AddEvent (Event newEvent)
    {
        CustomizationCheck(newEvent.GetCount(), counter);
        CustomizationCheck(newEvent.GetScale(), scale);
        CustomizationCheck(newEvent.GetComment(), comment);
        eventCollection.add(newEvent);
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
    public TimeZone GetTrackingDate () {return trackingDate;};
    public List<Event> GetEventCollection() { return eventCollection;}

    private String trackingName;
    private UUID trackingId;
    private TimeZone trackingDate;
    private TrackingCustomization counter;
    private TrackingCustomization scale;
    private TrackingCustomization comment;
    private List<Event> eventCollection;
}
