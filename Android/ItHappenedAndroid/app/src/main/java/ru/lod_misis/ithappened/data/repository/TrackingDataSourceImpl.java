package ru.lod_misis.ithappened.data.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import ru.lod_misis.ithappened.domain.models.Comparison;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.Rating;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.data.dto.DbModel;
import ru.lod_misis.ithappened.data.dto.DbModelV1;

public class TrackingDataSourceImpl implements TrackingDataSource {

    @Inject
    public TrackingDataSourceImpl(Context cntxt, String userId) {
        context = cntxt;

        this.userId = userId;
    }

    public void updateTrackingCollection(List<TrackingV1> trackingV1Collection) {
        realm.beginTransaction();
        DbModelV1 model = new DbModelV1(trackingV1Collection, userId);

        realm.copyToRealmOrUpdate(model);

        realm.commitTransaction();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TrackingV1 getTracking(UUID trackingId) {
        TrackingV1 trackingV1 = realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingId.toString())
                .findFirst();
        if (trackingV1 == null)
            throw new IllegalArgumentException("TrackingV1 with such ID doesn't exists");
        return realm.copyFromRealm(trackingV1);
    }

    public List<TrackingV1> getTrackingCollection() {
        DbModelV1 model = realm.where(DbModelV1.class)
                .equalTo("userId", userId).findFirst();

        if (model == null) return new ArrayList<>();
        if (model.getTrackingCollection() == null) return new ArrayList<>();

        List<TrackingV1> trackingV1Collection = model.getTrackingCollection().where()
                .equalTo("isDeleted", false)
                .findAllSorted("dateOfChange", Sort.DESCENDING);

        return realm.copyFromRealm(trackingV1Collection);
    }

    public void createTracking(TrackingV1 trackingV1) {
        realm.beginTransaction();

        boolean contains = false;

        if (realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingV1.getTrackingId().toString()).findFirst() != null)
            contains = true;

        DbModelV1 model = realm.where(DbModelV1.class)
                .equalTo("userId", userId).findFirst();

        if (model == null) {
            model = new DbModelV1(new ArrayList<TrackingV1>(), userId);
            realm.insert(model);
        }

        if (!contains) {
            model.getTrackingCollection().add(trackingV1);
            realm.copyToRealmOrUpdate(model);
        } else throw new IllegalArgumentException("TrackingV1 with such ID already exists");

        realm.commitTransaction();
    }

    public void createEvent(UUID trackingId, EventV1 newEventV1) {
        TrackingV1 trackingV1 = realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingId.toString()).findFirst();

        if (trackingV1 == null)
            throw new IllegalArgumentException("TrackingV1 with such ID doesn't exists");

        realm.beginTransaction();
        trackingV1.addEvent(newEventV1);

        realm.commitTransaction();
    }

    public EventV1 getEvent(UUID eventId) {
        EventV1 event = realm.where(EventV1.class).
                equalTo("eventId", eventId.toString())
                .findFirst();

        if (event == null) return null;

        return realm.copyFromRealm(event);
    }

    public void updateEvent(EventV1 event) {
        if (realm.where(TrackingV1.class)
                .equalTo("trackingId", event.getTrackingId().toString()).findFirst() == null)
            throw new IllegalArgumentException("Tracking with such ID doesn't exists");

        if (realm.where(EventV1.class)
                .equalTo("eventId", event.getEventId().toString()).findFirst() == null)
            throw new IllegalArgumentException("Event with such ID doesn't exists");

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
    }

    public void updateTracking(TrackingV1 tracking) {
        TrackingV1 trackingV1 = realm.where(TrackingV1.class)
                .equalTo("trackingId", tracking.getTrackingId().toString()).findFirst();

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
        trackingV1.deleteTracking();
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

    public List<EventV1> filterEvents(List<UUID> trackingId, Date dateFrom, Date dateTo,
                                      Comparison scaleComparison, Double scale,
                                      Comparison ratingComparison, Rating rating,
                                      int indexFrom, int indexTo) {
        if (trackingId != null && trackingId.size() == 0)
            return new ArrayList<>();

        DbModelV1 dbModel = realm.where(DbModelV1.class).equalTo("userId", userId).findFirst();

        if (dbModel == null) return new ArrayList<>();
        if (dbModel.getTrackingCollection() == null) return new ArrayList<>();

        RealmResults<TrackingV1> trackingResult = dbModel.getTrackingCollection().where().
                equalTo("isDeleted", false).findAll();
        String[] idArray = new String[trackingResult.size()];

        for (int i =0; i<trackingResult.size(); i++) {
            idArray[i] = trackingResult.get(i).getTrackingId().toString();
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
                if (eventV1.getRating() != null)
                    if (compareValues(ratingComparison,
                            eventV1.getRating().getRating().doubleValue(),
                            rating.getRating().doubleValue()))
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

    public List<EventV1> getEventCollection(UUID trackingId) {
        realm.beginTransaction();
        List<EventV1> collection = realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingId.toString()).findFirst()
                .getEventHistory();
        realm.commitTransaction();

        return realm.copyFromRealm(collection);
    }

    public void configureRealm() {
        Realm.init(context);
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

    private boolean compareValues(Comparison comparison, Double firstValue, Double secondValue) {
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