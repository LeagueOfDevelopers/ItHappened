package ru.lod_misis.ithappened.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.EventV1;

public class EventSource extends RealmObject{
    @PrimaryKey
    private String eventId;
    private String trackingId;
    private boolean occuredOn;

    public EventSource(String eventId, String trackingId, boolean occuredOn) {
        this.eventId = eventId;
        this.trackingId = trackingId;
        this.occuredOn = occuredOn;
    }

    public EventSource(Event event) {
        this.eventId = event.eventId;
        this.trackingId = event.trackingId;
        this.occuredOn = !event.isDeleted;
    }

    public EventSource(EventV1 event) {
        this.eventId = event.getEventId();
        this.trackingId = event.getTrackingId();
        this.occuredOn = !event.isDeleted();
    }

    public EventSource() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public boolean getOccuredOn(){
        return occuredOn;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public boolean isOccuredOn() {
        return occuredOn;
    }

    public void setOccuredOn(boolean occuredOn) {
        this.occuredOn = occuredOn;
    }
}
