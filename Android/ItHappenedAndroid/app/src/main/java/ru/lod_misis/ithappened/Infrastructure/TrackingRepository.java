package ru.lod_misis.ithappened.Infrastructure;

import android.content.Context;

import java.util.ArrayList;
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
import io.realm.Sort;
import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Models.DbModel;
import ru.lod_misis.ithappened.Models.DbModelV1;

public class TrackingRepository implements ITrackingRepository {

    public TrackingRepository(Context cntxt, String userId) {
        context = cntxt;
        Realm.init(context);
        this.userId = userId;
    }

    public void SaveTrackingCollection(List<TrackingV1> trackingV1Collection) {
        realm.beginTransaction();
        DbModelV1 model = new DbModelV1(trackingV1Collection, userId);

        realm.copyToRealmOrUpdate(model);

        realm.commitTransaction();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TrackingV1 GetTracking(UUID trackingId) {
        TrackingV1 trackingV1 = realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingId.toString())
                .findFirst();
        if (trackingV1 == null)
            throw new IllegalArgumentException("TrackingV1 with such ID doesn't exists");
        return realm.copyFromRealm(trackingV1);
    }

    public List<TrackingV1> GetTrackingCollection() {
        DbModelV1 model = realm.where(DbModelV1.class)
                .equalTo("userId", userId).findFirst();

        if (model == null) return new ArrayList<>();
        if (model.getTrackingV1Collection() == null) return new ArrayList<>();

        List<TrackingV1> trackingV1Collection = model.getTrackingV1Collection().where()
                .equalTo("isDeleted", false)
                .findAllSorted("dateOfChange", Sort.DESCENDING);

        return realm.copyFromRealm(trackingV1Collection);
    }

    public void AddNewTracking(TrackingV1 trackingV1) {
        realm.beginTransaction();

        boolean contains = false;

        if (realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingV1.getTrackingId()).findFirst() != null)
            contains = true;

        DbModelV1 model = realm.where(DbModelV1.class)
                .equalTo("userId", userId).findFirst();

        if (model == null) {
            model = new DbModelV1(new ArrayList<TrackingV1>(), userId);
            realm.insert(model);
        }

        if (!contains) {
            model.getTrackingV1Collection().add(trackingV1);
            realm.copyToRealmOrUpdate(model);
        } else throw new IllegalArgumentException("TrackingV1 with such ID already exists");

        realm.commitTransaction();
    }

    public void addEvent(UUID trackingId, EventV1 newEventV1) {
        TrackingV1 trackingV1 = realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingId.toString()).findFirst();

        if (trackingV1 == null)
            throw new IllegalArgumentException("TrackingV1 with such ID doesn't exists");

        realm.beginTransaction();
        trackingV1.AddEvent(newEventV1);

        realm.commitTransaction();
    }

    public EventV1 getEvent(UUID eventId) {
        EventV1 event = realm.where(EventV1.class).
                equalTo("eventId", eventId.toString())
                .findFirst();

        if (event == null) return null;

        return realm.copyFromRealm(event);
    }

    public void editEvent(EventV1 event) {
        if (realm.where(TrackingV1.class)
                .equalTo("trackingId", event.GetTrackingId().toString()).findFirst() == null)
            throw new IllegalArgumentException("Tracking with such ID doesn't exists");

        if (realm.where(EventV1.class)
                .equalTo("eventId", event.GetEventId().toString()).findFirst() == null)
            throw new IllegalArgumentException("Event with such ID doesn't exists");

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
    }

    public void editTracking(TrackingV1 tracking) {
        TrackingV1 trackingV1 = realm.where(TrackingV1.class)
                .equalTo("trackingId", tracking.getTrackingId()).findFirst();

        if (trackingV1 == null)
            throw new IllegalArgumentException("TrackingV1 with such ID doesn't exists");

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(tracking);
        realm.commitTransaction();
    }

    public void deleteTracking(UUID trackingId) {
        TrackingV1 trackingV1 = realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingId.toString()).findFirst();

        if (trackingV1 == null)
            throw new IllegalArgumentException("TrackingV1 with such ID doesn't exists");

        realm.beginTransaction();
        trackingV1.DeleteTracking();
        realm.commitTransaction();
    }

    public void deleteEvent(UUID eventId) {
        EventV1 eventV1 = realm.where(EventV1.class)
                .equalTo("eventId", eventId.toString()).findFirst();

        if (eventV1 == null)
            throw new IllegalArgumentException("Event with such ID doesn't exists");

        realm.beginTransaction();
        eventV1.setDeleted(true);
        realm.commitTransaction();
    }

    public List<EventV1> FilterEvents(List<UUID> trackingId, Date dateFrom, Date dateTo,
                                      Comparison scaleComparison, Double scale,
                                      Comparison ratingComparison, Rating rating,
                                      int indexFrom, int indexTo) {
        if (trackingId != null && trackingId.size() == 0)
            return new ArrayList<>();

        DbModelV1 dbModel = realm.where(DbModelV1.class).equalTo("userId", userId).findFirst();

        if (dbModel == null) return new ArrayList<>();
        if (dbModel.getTrackingV1Collection() == null) return new ArrayList<>();

        RealmResults<TrackingV1> trackingResult = dbModel.getTrackingV1Collection().where().
                equalTo("isDeleted", false).findAll();
        String[] idArray = new String[trackingResult.size()];

        for (int i =0; i<trackingResult.size(); i++) {
            idArray[i] = trackingResult.get(i).getTrackingId();
        }

        RealmResults<EventV1> events = realm.where(EventV1.class)
                .in("trackingId", idArray)
                .equalTo("isDeleted", false).findAll();

        if (trackingId != null) {
            String[] trackings = new String[trackingId.size()];

            for (int i = 0; i < trackings.length; i++)
                trackings[i] = trackingId.get(i).toString();

            events = events.where().in("trackingId", trackings).findAll();
        }

        if (dateFrom != null) {
            events = events.where()
                    .greaterThan("eventDate", dateFrom).findAll();
        }
        if (dateTo != null) {
            events = events.where()
                    .lessThan("eventDate", dateTo).findAll();
        }

        if (scaleComparison != null && scale != null) {
            if (scaleComparison == Comparison.Equal)
                events = events.where().equalTo("scale", scale).findAll();
            if (scaleComparison == Comparison.Less)
                events = events.where().lessThan("scale", scale).findAll();
            if (scaleComparison == Comparison.More)
                events = events.where().greaterThan("scale", scale).findAll();
        }

        List<EventV1> eventsToReturn = events.where().findAllSorted("eventDate", Sort.DESCENDING);
        if (ratingComparison != null && rating != null) {
            List<EventV1> filteredEventV1s = new ArrayList<>();
            for (EventV1 eventV1 : eventsToReturn) {
                if (eventV1.GetRating() != null)
                    if (CompareValues(ratingComparison,
                            eventV1.GetRating().GetRatingValue().doubleValue(),
                            rating.GetRatingValue().doubleValue()))
                        filteredEventV1s.add(eventV1);
            }

            eventsToReturn = filteredEventV1s;
        }

        int size = eventsToReturn.size();

        if (size == 0)
            return realm.copyFromRealm(eventsToReturn);
        if (indexFrom >= size)
            return new ArrayList<>();
        if (indexTo >= size)
            return realm.copyFromRealm(eventsToReturn.subList(indexFrom, size));
        return realm.copyFromRealm(eventsToReturn.subList(indexFrom, indexTo + 1));
    }

    public void ChangeTracking(TrackingV1 trackingV1) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(trackingV1);
        realm.commitTransaction();
    }

    public List<EventV1> getEventCollection(UUID trackingId) {
        realm.beginTransaction();
        List<EventV1> collection = realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingId.toString()).findFirst()
                .GetEventHistory();
        realm.commitTransaction();

        return realm.copyFromRealm(collection);
    }

    public void configureRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("ItHappened.realm").schemaVersion(2).migration(new RealmMigrations()).build();
        realm = Realm.getInstance(config);

        migrateData();
    }

    private void migrateData() {
        if (realm.isEmpty())
            return;
        if (!realm.where(DbModel.class).findAll().isEmpty()) {
            migrationV0();
        }
    }

    private void migrationV0() {
        RealmResults<DbModel> results = realm.where(DbModel.class).findAll();
        List<DbModel> modelList = realm.where(DbModel.class).findAll().subList(0, results.size());

        List<DbModelV1> newModelList = new ArrayList<>();
        for (DbModel model : modelList) {
            newModelList.add(new DbModelV1(model));
        }

        realm.beginTransaction();
        realm.deleteAll();

        realm.insert(newModelList);

        realm.commitTransaction();
    }

    private boolean CompareValues(Comparison comparison, Double firstValue, Double secondValue) {
        if (comparison == Comparison.Less) {
            return (firstValue < secondValue);
        }
        if (comparison == Comparison.Equal) {
            return (firstValue.equals(secondValue));
        }
        if (comparison == Comparison.More) {
            return (firstValue > secondValue);
        }
        return false;
    }

    private Context context;
    private Realm realm;
    private String userId;
}