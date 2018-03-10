package ru.lod_misis.ithappened.Infrastructure;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Models.DbModel;

public class TrackingRepository implements ITrackingRepository{

    public TrackingRepository(Context cntxt, String userId)
    {
        context = cntxt;
        Realm.init(context);
        this.userId = userId;
    }

    public void SaveTrackingCollection(List<Tracking> trackingCollection)
    {
        onCreate();
        realm.beginTransaction();
        DbModel model = new DbModel(trackingCollection, userId);
        RealmResults<DbModel> result = realm.where(DbModel.class).equalTo("userId", userId).findAll();
        result.deleteAllFromRealm();
        realm.copyToRealm(model);
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
        RealmResults<DbModel> results = realm.where(DbModel.class)
                .equalTo("userId", userId).findAll();
        List<DbModel> modelCollection = realm.copyFromRealm(results);
        List<Tracking> trackingCollectionToReturn = new ArrayList<>();
        if (modelCollection.size() == 0)
            return new ArrayList<Tracking>();
        trackingCollectionToReturn.addAll(modelCollection.get(0).getTrackingCollection());
        return trackingCollectionToReturn;
    }

    public void AddNewTracking(Tracking tracking)
    {
        onCreate();
       realm.beginTransaction();
        RealmResults<DbModel> results = realm.where(DbModel.class)
                .equalTo("userId", userId).findAll();
        List<DbModel> model = realm.copyFromRealm(results);

        if (model.size() == 0) {
            List<Tracking> trackingList = new ArrayList<>();
            trackingList.add(tracking);
            DbModel newModel = new DbModel(trackingList, userId);
            realm.copyToRealm(newModel);
        }
        else {
            List<Tracking> trackingCollection = model.get(0).getTrackingCollection();
            boolean contains = false;

            for (Tracking item : trackingCollection) {
                if (item.GetTrackingID().equals(tracking.GetTrackingID())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                List<Tracking> newList = new ArrayList<>();
                RealmResults<DbModel> resultsWithSize = realm.where(DbModel.class)
                        .equalTo("userId", userId).findAll();
                List<DbModel> modelWithSize = realm.copyFromRealm(results);
                for (Tracking item : modelWithSize.get(0).getTrackingCollection()) {
                    newList.add(item);
                    }
                    newList.add(tracking);
                realm.copyToRealmOrUpdate(new DbModel(newList, userId));
            }
            else throw new IllegalArgumentException("Tracking with such ID already exists");
        }
        realm.commitTransaction();
    }

    public List<Event> FilterEvents(List<UUID> trackingId, Date dateFrom, Date dateTo,
                                    Comparison scaleComparison, Double scale,
                                    Comparison ratingComparison, Rating rating) {

        List<Tracking> trackingCollection = GetTrackingCollection();

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
        RealmResults<DbModel> result = realm.where(DbModel.class)
                .equalTo("userId", userId).findAll();
        if (result.isEmpty())
            throw new IllegalArgumentException("User with such ID doesn't exists");
        DbModel model = result.first();
        List<Tracking> trackingCollection = model.getTrackingCollection();
        int index=0;
        for (Tracking trc: trackingCollection) {
            if (trc.GetTrackingID().equals(tracking.GetTrackingID()))
                break;
            index++;
        }
        trackingCollection.set(index, tracking);
        model.setTrackingCollection(trackingCollection);
        //result.deleteAllFromRealm();
        realm.copyToRealmOrUpdate(model);
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
    private String userId;
}
