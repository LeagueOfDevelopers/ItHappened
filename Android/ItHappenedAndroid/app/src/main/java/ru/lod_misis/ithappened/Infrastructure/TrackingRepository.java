package ru.lod_misis.ithappened.Infrastructure;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.DynamicRealm;
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
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Models.DbModel;
import ru.lod_misis.ithappened.Models.DbModelV1;
import ru.lod_misis.ithappened.Models.EventSource;

public class TrackingRepository implements ITrackingRepository {

    public TrackingRepository(Context cntxt, String userId) {
        context = cntxt;
        Realm.init(context);
        this.userId = userId;
    }

    public void SaveTrackingCollection(List<TrackingV1> trackingV1Collection) {
        realm.beginTransaction();
        DbModelV1 model = new DbModelV1(trackingV1Collection, userId);
        model.setEventSourceCollection(addEventsToEventCollection(trackingV1Collection, userId));

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
        RealmResults<DbModelV1> results = realm.where(DbModelV1.class)
                .equalTo("userId", userId).findAll();
        List<DbModelV1> modelCollection = realm.copyFromRealm(results);
        List<TrackingV1> trackingV1CollectionToReturn = new ArrayList<>();

        if (modelCollection.size() == 0)
            return new ArrayList<>();

        trackingV1CollectionToReturn.addAll(modelCollection.get(0).getTrackingV1Collection());

        Collections.sort(trackingV1CollectionToReturn, new Comparator<TrackingV1>() {
            @Override
            public int compare(TrackingV1 trackingV1, TrackingV1 t1) {
                return t1.GetDateOfChange().compareTo(trackingV1.GetDateOfChange());

            }
        });
        return trackingV1CollectionToReturn;
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
        DbModelV1 model = realm.where(DbModelV1.class)
                .equalTo("userId", userId).findFirst();

        if (model == null)
            throw new IllegalArgumentException("User with such ID doesn't exists");

        model.getEventSourceCollection().add(new EventSource(newEventV1));
        realm.commitTransaction();
    }

    public EventV1 getEvent(UUID eventId) {
        EventV1 event = realm.where(EventV1.class).
                equalTo("eventId", eventId.toString())
                .findFirst();

        if (event == null) return null;

        return realm.copyFromRealm(event);
    }

    public void editEvent(UUID trackingId, UUID eventId,
                          Double newScale,
                          Rating newRating,
                          String newComment,
                          Date newDate){
        TrackingV1 trackingV1 = realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingId.toString()).findFirst();
        if (trackingV1 == null)
            throw new IllegalArgumentException("TrackingV1 with such ID doesn't exists");

        realm.beginTransaction();
        TrackingV1 tracking = realm.copyFromRealm(trackingV1);
        tracking.EditEvent(eventId, newScale, newRating, newComment, newDate);

        realm.copyToRealmOrUpdate(tracking);
        realm.commitTransaction();
    }

    public void editTracking(UUID trackingId,
                             TrackingCustomization editedCounter,
                             TrackingCustomization editedScale,
                             TrackingCustomization editedComment,
                             String editedTrackingName,
                             String scaleName,
                             String color) {
        TrackingV1 trackingV1 = realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingId.toString()).findFirst();

        if (trackingV1 == null)
            throw new IllegalArgumentException("TrackingV1 with such ID doesn't exists");

        realm.beginTransaction();
        trackingV1.EditTracking(editedCounter, editedScale, editedComment,
                editedTrackingName, scaleName, color);
        realm.commitTransaction();
    }

    public void deleteTracking(UUID trackingId) {
        TrackingV1 trackingV1 = realm.where(TrackingV1.class)
                .equalTo("trackingId", trackingId.toString()).findFirst();

        if (trackingV1 == null)
            throw new IllegalArgumentException("TrackingV1 with such ID doesn't exists");

        realm.beginTransaction();

        trackingV1.DeleteTracking();
        realm.copyToRealmOrUpdate(trackingV1);

        List<EventSource> eventSources = realm.where(EventSource.class)
                .contains("trackingId", trackingId.toString()).findAll();

        for (EventSource eventSource : eventSources)
            eventSource.setOccuredOn(false);

        realm.commitTransaction();
    }

    public void deleteEvent(UUID eventId) {
        EventV1 eventV1 = realm.where(EventV1.class)
                .equalTo("eventId", eventId.toString()).findFirst();

        EventSource eventSource = realm.where(EventSource.class)
                .equalTo("eventId", eventId.toString()).findFirst();

        if (eventV1 == null || eventSource == null)
            throw new IllegalArgumentException("Event with such ID doesn't exists");

        realm.beginTransaction();

        eventV1.setDeleted(true);
        eventSource.setOccuredOn(false);
        realm.copyToRealmOrUpdate(eventV1);

        realm.commitTransaction();
    }

    public List<EventV1> FilterEvents(List<UUID> trackingId, Date dateFrom, Date dateTo,
                                      Comparison scaleComparison, Double scale,
                                      Comparison ratingComparison, Rating rating,
                                      int indexFrom, int indexTo) {
        if (trackingId != null && trackingId.size() == 0)
            return new ArrayList<>();

        List<String> idList = getEventsForFilter();

        if (idList.isEmpty()) return new ArrayList<>();

        String[] idArray = new String[idList.size()];

        for (int i = 0; i < idArray.length; i++)
            idArray[i] = idList.get(i);

        RealmResults<EventV1> events = realm.where(EventV1.class)
                .in("eventId", idArray)
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
                .name("ItHappened.realm").schemaVersion(1).migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm dynamicRealm, long oldVersion, long l1) {

                        if (oldVersion == 0) {
                            RealmSchema schema = dynamicRealm.getSchema();

                            RealmObjectSchema newEventSchema = schema.create("EventV1");
                            RealmObjectSchema newTrackingSchema = schema.create("TrackingV1");
                            RealmObjectSchema newDbModelSchema = schema.create("DbModelV1");
                            RealmObjectSchema ratingSchema = schema.get("Rating");
                            RealmObjectSchema eventSourceSchema = schema.create("EventSource");

                            newEventSchema.addField("eventId", String.class).addPrimaryKey("eventId");
                            newEventSchema.addField("trackingId", String.class);
                            newEventSchema.addField("eventDate", Date.class).addIndex("eventDate");
                            newEventSchema.addField("dateOfChange", Date.class);
                            newEventSchema.addField("scale", Double.class);
                            newEventSchema.addRealmObjectField("rating", ratingSchema);
                            newEventSchema.addField("comment", String.class);
                            newEventSchema.addField("isDeleted", boolean.class);

                            newTrackingSchema.addField("trackingId", String.class).addPrimaryKey("trackingId");
                            newTrackingSchema.addField("scaleName", String.class);
                            newTrackingSchema.addField("trackingName", String.class);
                            newTrackingSchema.addField("scale", String.class);
                            newTrackingSchema.addField("rating", String.class);
                            newTrackingSchema.addField("comment", String.class);
                            newTrackingSchema.addField("color", String.class);
                            newTrackingSchema.addField("dateOfChange", Date.class);
                            newTrackingSchema.addField("trackingDate", Date.class);
                            newTrackingSchema.addField("isDeleted", boolean.class);
                            newTrackingSchema.addRealmListField("eventV1Collection", newEventSchema);

                            eventSourceSchema.addField("eventId", String.class)
                                    .addPrimaryKey("eventId");
                            eventSourceSchema.addField("trackingId", String.class);
                            eventSourceSchema.addField("occuredOn", boolean.class);

                            newDbModelSchema.addField("userId", String.class).addPrimaryKey("userId");
                            newDbModelSchema.addRealmListField("eventSourceCollection", eventSourceSchema);
                            newDbModelSchema.addRealmListField("trackingV1Collection", newTrackingSchema);

                        }
                    }
                }).build();
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

    private boolean CompareValues(Comparison comparison, Double firstValue, Double secondValue)
    {
        if (comparison == Comparison.Less)
        {
            return (firstValue < secondValue);
        }
        if (comparison == Comparison.Equal)
        {
            return (firstValue.equals(secondValue));
        }
        if (comparison == Comparison.More)
        {
            return (firstValue > secondValue);
        }
        return false;
    }

    private RealmList<EventSource> addEventsToEventCollection(List<TrackingV1> trackingV1Collection, String userId) {

        DbModelV1 dbModelV1 = realm.where(DbModelV1.class).
                equalTo("userId", userId).findFirst();
        RealmList<EventSource> eventSourceCollection;
        try {
            eventSourceCollection = dbModelV1.getEventSourceCollection();
        } catch (Exception e) {
            eventSourceCollection = new RealmList<>();
        }

        if (eventSourceCollection.size() == 0) {
            for (TrackingV1 trackingV1 : trackingV1Collection) {
                if(trackingV1.GetEventHistory()!=null) {
                    for (EventV1 event : trackingV1.GetEventHistory())
                        eventSourceCollection.add(new EventSource(event));
                }
            }
        } else
            for (TrackingV1 trackingV1 : trackingV1Collection) {
                List<EventV1> eventV1List = trackingV1.getEventV1Collection();
                if (eventV1List != null) {
                    for (EventV1 eventV1 : eventV1List) {
                        boolean contains = false;

                        for (EventSource source : eventSourceCollection) {
                            if (source.getEventId().equals(eventV1.GetEventId()))
                                contains = true;
                        }
                        if (!contains)
                            eventSourceCollection.add(new EventSource(eventV1));
                    }
                }
            }
        return eventSourceCollection;
    }

    private List<String> getEventsForFilter() {
        DbModelV1 dbModel = realm.where(DbModelV1.class)
                .equalTo("userId", userId)
                .findFirst();

        if (dbModel == null || dbModel.getEventSourceCollection() == null)
            return new ArrayList<>();

        List<EventSource> eventCollection = realm.copyFromRealm
                (dbModel.getEventSourceCollection());
        List<String> ids = new ArrayList<>();

        for (EventSource event : eventCollection)
            if (event.getOccuredOn())
                ids.add(event.getEventId());

        return ids;
    }

    private List<EventV1> RemoveDeletedEventsAndTrackingsFromCollection(List<EventV1> collection) {
        List<EventV1> collectionToReturn = new ArrayList<>();
        for (EventV1 eventV1 : collection) {
            if (!eventV1.isDeleted())
                collectionToReturn.add(eventV1);
        }
        return collectionToReturn;
    }

    private Context context;
    private Realm realm;
    private String userId;
}