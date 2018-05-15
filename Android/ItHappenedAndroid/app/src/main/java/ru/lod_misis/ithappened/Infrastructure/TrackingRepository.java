package ru.lod_misis.ithappened.Infrastructure;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmResults;
import io.realm.RealmSchema;
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
        realm.beginTransaction();
        DbModel model = new DbModel(trackingCollection, userId);
        RealmResults<DbModel> result = realm.where(DbModel.class).equalTo("userId", userId).findAll();
        if (result.size() != 0)
            result.deleteFromRealm(0);
        realm.copyToRealm(model);
        realm.commitTransaction();
    }

    public void setUserId(String userId) { this.userId = userId; }

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
        RealmResults<DbModel> results = realm.where(DbModel.class)
                .equalTo("userId", userId).findAll();
        List<DbModel> modelCollection = realm.copyFromRealm(results);
        List<Tracking> trackingCollectionToReturn = new ArrayList<>();
        if (modelCollection.size() == 0)
            return new ArrayList<Tracking>();
        trackingCollectionToReturn.addAll(modelCollection.get(0).getTrackingCollection());
        //return trackingCollectionToReturn;
        Collections.sort(trackingCollectionToReturn, new Comparator<Tracking>() {
            @Override
            public int compare(Tracking tracking, Tracking t1) {
                return t1.GetDateOfChange().compareTo(tracking.GetDateOfChange());
            }
        });
        return trackingCollectionToReturn;
    }

    public void AddNewTracking(Tracking tracking)
    {
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
        }else{
            if(dateFrom!=null){
                notFilteredEvents.clear();
                notFilteredEvents.addAll(filteredEvents);
                filteredEvents.clear();
                for (Event event : notFilteredEvents) {
                    if (event.GetEventDate().compareTo(dateFrom) >= 0)
                        filteredEvents.add(event);
                }
            }
            if(dateTo!=null){
                notFilteredEvents.clear();
                notFilteredEvents.addAll(filteredEvents);
                filteredEvents.clear();
                for (Event event : notFilteredEvents) {
                    if (event.GetEventDate().compareTo(dateTo) <= 0)
                        filteredEvents.add(event);
                }
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
                    if (CompareValues(ratingComparison,
                            event.GetRating().GetRatingValue().doubleValue(),
                            rating.GetRatingValue().doubleValue()))
                        filteredEvents.add(event);
            }
        }

        Collections.sort(filteredEvents, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return t1.GetEventDate().compareTo(event.GetEventDate());
            }
        });

        return RemoveDeletedEventsAndTrackingsFromCollection(
                filteredEvents);
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
        realm.beginTransaction();
        RealmResults<DbModel> result = realm.where(DbModel.class)
                .equalTo("userId", userId).findAll();
        if (result.isEmpty())
            throw new IllegalArgumentException("User with such ID doesn't exists");
        List<DbModel> modelCollection = realm.copyFromRealm(result);
        DbModel model = modelCollection.get(0);
        RealmList<Tracking> trackingCollection = model.getTrackingCollection();
        int index=0;
        for (Tracking trc: trackingCollection) {
            if (trc.GetTrackingID().equals(tracking.GetTrackingID()))
                break;
            index++;
        }
        trackingCollection.set(index, tracking);
        Log.e("DB", index+" "+tracking.GetTrackingName());
        model.setTrackingCollection(trackingCollection);
        DbModel newModel = new DbModel(model.getTrackingCollection(), userId);
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
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

    public void configureRealm()
    {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("ItHappened.realm").schemaVersion(2).migration(new RealmMigration() {
                    @Override
                    public void migrate(final DynamicRealm dynamicRealm, long oldVersion, long newVersion) {
                        final RealmSchema schema = dynamicRealm.getSchema();

                        if (oldVersion == 1){
                            final RealmObjectSchema newSchema = schema.get("DbModel");
                            newSchema.addField("color", String.class);
                            newSchema.transform(new RealmObjectSchema.Function() {
                                @Override
                                public void apply(DynamicRealmObject dynamicRealmObject) {
                                    dynamicRealmObject.setString("color", "111111");
                                }
                            });
                        }
                    }
                })
                .build();

        realm = Realm.getInstance(config);
    }

    Context context;
    Realm realm;
    private String userId;
}
