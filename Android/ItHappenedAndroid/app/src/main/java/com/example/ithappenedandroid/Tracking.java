package com.example.ithappenedandroid;

import java.time.OffsetDateTime;
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
        _trackingName = trackingName;
        _counter = counter;
        _scale = scale;
        _comment = comment;
        _trackingId = UUID.randomUUID();
        _trackingDate = TimeZone.getDefault();
        _eventCollection = new ArrayList<Event>();
    }

    public UUID AddEvent (Event newEvent)
    {
        if (newEvent.GetCount() == null && _counter == TrackingCustomization.Counter)
            throw new IllegalArgumentException("Non-optional parameters can not be empty");
        if (newEvent.GetCount() != null && _counter == TrackingCustomization.Unknown)
            throw new IllegalArgumentException("template does not have customization \"Counter\"");
        if (newEvent.GetScale() == null && _scale == TrackingCustomization.Scale)
            throw new IllegalArgumentException("Non-optional parameters can not be empty");
        if (newEvent.GetScale() != null && _scale == TrackingCustomization.Unknown)
            throw new IllegalArgumentException("template does not have customization \"Scale\"");
        if (newEvent.GetComment() == null && _comment == TrackingCustomization.Comment)
            throw new IllegalArgumentException("Non-optional parameters can not be empty");
        if (newEvent.GetComment() != null && _comment == TrackingCustomization.Unknown)
            throw new IllegalArgumentException("template does not have customization \"Comment\"");
        _eventCollection.add(newEvent);
        return newEvent.GetEventId();
    }

    public String GetTrackingName() {return _trackingName;}
    public UUID GetTrackingID() {return _trackingId;}
    public TimeZone GetTrackingDate () {return _trackingDate;};
    public List<Event> GetEventCollection() { return _eventCollection;}

    private String _trackingName;
    private UUID _trackingId;
    private TimeZone _trackingDate;
    private TrackingCustomization _counter;
    private TrackingCustomization _scale;
    private TrackingCustomization _comment;
    private List<Event> _eventCollection;
}
