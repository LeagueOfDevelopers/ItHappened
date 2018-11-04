package ru.lod_misis.ithappened.Domain.Models;

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

public class TrackingV1 extends RealmObject {

    public TrackingV1(String trackingName,
                      UUID trackingId,
                      TrackingCustomization scale,
                      TrackingCustomization rating,
                      TrackingCustomization comment,
                      TrackingCustomization geoposition,
                      TrackingCustomization photo,
                      String scaleName,
                      String color) {
        this.color = color;
        this.trackingName = trackingName;
        setScaleCustomization(scale);
        setRatingCustomization(rating);
        setCommentCustomization(comment);
        setGeopositionCustomization(geoposition);
        setPhotoCustomization(photo);
        this.trackingId = trackingId.toString();
        trackingDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        dateOfChange = trackingDate;
        eventCollection = new RealmList<>();
        this.scaleName = scaleName;
    }

    public TrackingV1(String trackingName,
                      UUID trackingId,
                      TrackingCustomization scale,
                      TrackingCustomization rating,
                      TrackingCustomization comment,
                      TrackingCustomization geoposition,
                      TrackingCustomization photo,
                      Date trackingDate,
                      List<EventV1> eventCollection,
                      boolean status, Date changeDate,
                      String scaleName, String color) {
        this.color = color;
        this.trackingName = trackingName;
        setScaleCustomization(scale);
        setRatingCustomization(rating);
        setCommentCustomization(comment);
        setGeopositionCustomization(geoposition);
        setPhotoCustomization(photo);
        this.trackingId = trackingId.toString();
        this.trackingDate = trackingDate;
        this.eventCollection = new RealmList<>();
        this.eventCollection.addAll(eventCollection);
        dateOfChange = changeDate;
        isDeleted = status;
        this.scaleName = scaleName;
    }

    public TrackingV1() {
    }

    public TrackingV1(Tracking tracking) {
        this.color = "-5658199";
        this.trackingName = tracking.trackingName;
        setScaleCustomization(TrackingCustomization.valueOf(tracking.scale));
        setRatingCustomization(TrackingCustomization.valueOf(tracking.rating));
        setCommentCustomization(TrackingCustomization.valueOf(tracking.comment));
        this.trackingId = tracking.trackingId;
        this.trackingDate = tracking.trackingDate;
        eventCollection = new RealmList<>();
        for (Event ev : tracking.eventCollection) {
            eventCollection.add(new EventV1(ev));
        }
        dateOfChange = tracking.dateOfChange;
        isDeleted = tracking.isDeleted;
        this.scaleName = tracking.scaleName;
    }

    public void addEvent(EventV1 newEventV1) {
        customizationCheck(newEventV1.getScale(), getScaleCustomization());
        customizationCheck(newEventV1.getRating(), getRatingCustomization());
        customizationCheck(newEventV1.getComment(), getCommentCustomization());
        customizationCheck(newEventV1.getLotitude(), getGeopositionCustomization());
        customizationCheck(newEventV1.getLongitude(), getGeopositionCustomization());
        eventCollection.add(newEventV1);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void removeEvent(UUID eventID) {
        Integer deletionEvent = null;
        Integer i = 0;
        for (EventV1 eventV1 : eventCollection) {
            if (eventV1.getEventId().equals(eventID))
                deletionEvent = i;
            i++;
        }
        if (deletionEvent == null)
            throw new IllegalArgumentException("EventV1 with such id dosn't exist");
        EventV1 eventV1 = eventCollection.get(deletionEvent);
        eventV1.removeEvent();
        eventCollection.set(deletionEvent, eventV1);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void editEvent(UUID eventId,
                          Double newScale,
                          Rating newRating,
                          String newComment,
                          Double newLotitude,
                          Double newLongitude,
                          String newPhoto,
                          Date newDate) {
        EventV1 editedEventV1 = null;
        int index = 0;
        boolean contains = false;
        for (EventV1 eventV1 : eventCollection) {
            if (eventV1.getEventId().equals(eventId)) {
                contains = true;
                editedEventV1 = eventV1;
                break;
            }
            index++;
        }
        if (!contains)
            throw new IllegalArgumentException("EventV1 with such id doesn't exist");
        if (changesCheck(newScale, getScaleCustomization()))
            editedEventV1.editScale(newScale);
        if (changesCheck(newRating, getRatingCustomization()))
            editedEventV1.editValueOfRating(newRating);
        if (changesCheck(newComment, getCommentCustomization()))
            editedEventV1.editComment(newComment);
        if (changesCheck(newLotitude, getGeopositionCustomization()) && changesCheck(newLongitude, getGeopositionCustomization()))
            editedEventV1.editGeoposition(newLotitude, newLongitude);
        if (changesCheck(newPhoto, getPhotoCustomization()))
            editedEventV1.editPhoto(newPhoto);
        if (newDate != null)
            editedEventV1.editDate(newDate);
        eventCollection.set(index, editedEventV1);
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    public void editTracking(TrackingCustomization editedScale,
                             TrackingCustomization editedRating,
                             TrackingCustomization editedComment,
                             TrackingCustomization editedGeoposition,
                             TrackingCustomization editedPhoto,
                             String editedTrackingName,
                             String scaleName, String color) {
        if (scaleName != null)
            setScaleName(scaleName);
        if (color != null)
            this.color = color;
        if (editedScale != null)
            setScaleCustomization(editedScale);
        if (editedRating != null)
            setRatingCustomization(editedRating);
        if (editedComment != null)
            setCommentCustomization(editedComment);
        if (editedGeoposition != null)
            setGeopositionCustomization(editedGeoposition);
        if (editedPhoto != null)
            setPhotoCustomization(editedPhoto);
        if (editedTrackingName != null)
            trackingName = editedTrackingName;
        if (color != null) this.color = color;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
    }

    private boolean changesCheck(Object value, TrackingCustomization customization) {
        return value != null && customization != TrackingCustomization.None;
    }

    private void customizationCheck(Object value, TrackingCustomization customization) {
        if (value == null && customization == TrackingCustomization.Required) {
            throw new IllegalArgumentException("Non-optional parameters can not be empty");
        }

        if (value != null && customization == TrackingCustomization.None) {
            throw new IllegalArgumentException("None customizations can not take a value");
        }
    }

    public EventV1 getEvent(UUID eventId) {
        for (EventV1 item : eventCollection) {
            if (item.getEventId().equals(eventId))
                return item;
        }
        throw new IllegalArgumentException("EventV1 with such ID doesn't exist");
    }

    public void deleteTracking() {
        isDeleted = true;
        dateOfChange = Calendar.getInstance(TimeZone.getDefault()).getTime();
        for (EventV1 eventV1 : eventCollection) {
            eventV1.removeEvent();
        }
    }

    public List<EventV1> getEventHistory() {
        if (eventCollection != null) {
            Collections.sort(eventCollection, new Comparator<EventV1>() {
                @Override
                public int compare(EventV1 eventV1, EventV1 t1) {
                    return t1.getEventDate().compareTo(eventV1.getEventDate());
                }
            });
        }
        return eventCollection;
    }

    public String getTrackingName() {
        return trackingName;
    }

    public void setTrackingName(String trackingName) {
        this.trackingName = trackingName;
    }

    public void setTrackingId(UUID id) {
        trackingId = id.toString();
    }

    public UUID getTrackingId() {
        return UUID.fromString(trackingId);
    }

    public Date getTrackingDate() {
        return trackingDate;
    }

    public void setTrackingDate(Date trackingDate) {
        this.trackingDate = trackingDate;
    }

    public void setScaleCustomization(TrackingCustomization scl) {
        scale = scl.toString();
    }

    public TrackingCustomization getScaleCustomization() {
        return TrackingCustomization.valueOf(scale);
    }

    public void setCommentCustomization(TrackingCustomization comm) {
        comment = comm.toString();
    }

    public TrackingCustomization getCommentCustomization() {
        return TrackingCustomization.valueOf(comment);
    }

    public void setRatingCustomization(TrackingCustomization rat) {
        rating = rat.toString();
    }

    public TrackingCustomization getRatingCustomization() {
        return TrackingCustomization.valueOf(rating);
    }

    public void setGeopositionCustomization(TrackingCustomization geo) {
        geoposition = geo.toString();
    }

    public TrackingCustomization getGeopositionCustomization() {
        return TrackingCustomization.valueOf(geoposition);
    }

    public void setPhotoCustomization(TrackingCustomization phot) {
        photo = phot.toString();
    }

    public TrackingCustomization getPhotoCustomization() {
        return TrackingCustomization.valueOf(photo);
    }

    public Date getDateOfChange() {
        return dateOfChange;
    }

    public void setDateOfChange(Date dateOfChange) {
        this.dateOfChange = dateOfChange;
    }

    public boolean setDeleted() {
        return isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setScaleName(String name) {
        scaleName = name;
    }

    public String getScaleName() {
        return scaleName;
    }

    public RealmList<EventV1> getEventCollection() {
        return eventCollection;
    }

    public void setEventCollection(RealmList<EventV1> eventV1Collection) {
        this.eventCollection = eventV1Collection;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


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
    private String geoposition;
    private String photo;
    @Expose
    @SerializedName("eventCollection")
    private RealmList<EventV1> eventCollection;
    @Expose
    private Date dateOfChange;
    @Expose
    private boolean isDeleted = false;
    @Expose
    private String color;
}
