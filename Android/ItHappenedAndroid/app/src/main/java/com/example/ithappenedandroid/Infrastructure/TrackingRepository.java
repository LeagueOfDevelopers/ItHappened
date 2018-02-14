package com.example.ithappenedandroid.Infrastructure;

import android.content.Context;

import com.example.ithappenedandroid.Domain.Comparison;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Rating;
import com.example.ithappenedandroid.Domain.Tracking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class TrackingRepository implements ITrackingRepository{

    public TrackingRepository(Context cntxt)
    {
        context = cntxt;
        Realm.init(context);
    }

    public void SaveTrackingCollection(List<Tracking> trackingCollection)
    {
        onCreate();
        realm.beginTransaction();
        for (Tracking tracking: trackingCollection) {
            realm.copyToRealmOrUpdate(tracking);
        }
        realm.commitTransaction();
    }

    public Tracking GetTracking(UUID trackingId)
    {
        List<Tracking> trackingCollection = GetTrackingCollection();

        for (Tracking item: trackingCollection)
        {
            if (item.GetTrackingID().equals(trackingId))
            {
                return item;
            }
        }
        throw new IllegalArgumentException("Tracking with such ID doesn't exists");
    }

    public List<Tracking> GetTrackingCollection()
    {
        onCreate();
        RealmResults<Tracking> results = realm.where(Tracking.class).findAll();
        List<Tracking> trackingCollection = realm.copyFromRealm(results);
        return trackingCollection;
    }

    public void AddNewTracking(Tracking tracking)
    {
        onCreate();
        realm.beginTransaction();
        RealmResults<Tracking> results = realm.where(Tracking.class).findAll();
        List<Tracking> trackingCollection = realm.copyFromRealm(results);
        boolean contains = false;
        for (Tracking item: trackingCollection)
        {
            if (item.GetTrackingID().equals(tracking.GetTrackingID()))
            {
                contains = true;
                break;
            }
        }
        if (!contains)
            realm.copyToRealm(tracking);

        else throw new IllegalArgumentException("Tracking with such ID already exists");
        realm.commitTransaction();
    }

    public List<Event> FilterEvents(List<UUID> trackingId, Date dateFrom, Date dateTo,
                                    Comparison scaleComparison, Double scale,
                                    Comparison ratingComparison, Rating rating) {

        onCreate();
        RealmResults<Tracking> results = realm.where(Tracking.class).findAll();
        List<Tracking> trackingCollection = realm.copyFromRealm(results);

        List<Event> notFilteredEvents = new ArrayList<Event>();
        List<Event> filteredEvents = new ArrayList<Event>();
        for (Tracking trackig : trackingCollection) {
            notFilteredEvents.addAll(trackig.GetEventCollection());
        }

        filteredEvents.addAll(notFilteredEvents);

        if (trackingId != null) {
            filteredEvents.clear();
            for (Event event : notFilteredEvents) {
                if (trackingId.contains(event.GetTrackingId()))
                    filteredEvents.add(event);
            }
        }

        if (dateFrom != null && dateTo != null) {
            notFilteredEvents.clear();
            notFilteredEvents.addAll(filteredEvents);
            filteredEvents.clear();
            for (Event event : notFilteredEvents) {
                if (event.GetEventDate().compareTo(dateFrom) >= 0 && event.GetEventDate().compareTo(dateTo) <= 0)
                    filteredEvents.add(event);
            }
        }

        if (scaleComparison != null && scale != null) {
            notFilteredEvents.clear();
            notFilteredEvents.addAll(filteredEvents);
            filteredEvents.clear();
            for (Event event : notFilteredEvents) {
                if(event.GetScale()!=null)
                if (CompareValues(scaleComparison, event.GetScale(), scale))
                    filteredEvents.add(event);
            }
        }

        if (ratingComparison != null && rating != null) {
            notFilteredEvents.clear();
            notFilteredEvents.addAll(filteredEvents);
            filteredEvents.clear();
            for (Event event : notFilteredEvents) {
                if (event.GetRating() != null)
                    if (CompareValues(ratingComparison, event.GetRating().GetRatingValue().doubleValue(),
                            rating.GetRatingValue().doubleValue()))
                        filteredEvents.add(event);
            }
        }

        return RemoveDeletedEventsAndTrackingsFromCollection(filteredEvents);
    }

    private boolean CompareValues(Comparison comparison, Double firstValue, Double secondValue)
    {
        if (comparison == Comparison.Less)
        {
            if (firstValue < secondValue)
                return true;
            return false;
        }
        if (comparison == Comparison.Equal)
        {
            if (firstValue.equals(secondValue))
                return true;
            return false;
        }
        if (comparison == Comparison.More)
        {
            if (firstValue > secondValue)
                return true;
        }
        return false;
    }

    public void ChangeTracking(final Tracking tracking) {
        onCreate();
        realm.beginTransaction();
        RealmResults<Tracking> result = realm.where(Tracking.class)
                .equalTo("trackingId", tracking.GetTrackingID().toString())
                .findAll();
        if (result.isEmpty())
            throw new IllegalArgumentException("Tracking with such ID doesn't exists");
        result.deleteFromRealm(0);
        realm.copyToRealm(tracking);
        realm.commitTransaction();
    }

    private void onCreate()
    {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("ItHappened.realm")
                .build();

        realm = Realm.getInstance(config);
    }

    private List<Event> RemoveDeletedEventsAndTrackingsFromCollection(List<Event> collection)
    {
        List<Event> collectionToReturn = new ArrayList<>();
        for (Event event : collection )
        {
            if (!event.isDeleted())
                collectionToReturn.add(event);
        }
        return collectionToReturn;
    }

    Context context;
    Realm realm;
}
