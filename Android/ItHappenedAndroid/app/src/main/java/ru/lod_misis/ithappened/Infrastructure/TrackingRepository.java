package ru.lod_misis.ithappened.Infrastructure;

import android.content.Context;

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
import io.realm.internal.Table;
import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Models.DbModel;
import ru.lod_misis.ithappened.Models.NewDbModel;

public class TrackingRepository implements ITrackingRepository{

    public TrackingRepository(Context cntxt, String userId){
        context = cntxt;
        Realm.init(context);
        this.userId = userId;
    }

    public void SaveTrackingCollection(List<NewTracking> newTrackingCollection){
        realm.beginTransaction();
        NewDbModel model = new NewDbModel(newTrackingCollection, userId);
        model.setNewEventCollection(addEventsToEventCollection(newTrackingCollection, userId));

        realm.copyToRealmOrUpdate(model);

        realm.commitTransaction();
    }

    public void setUserId(String userId) { this.userId = userId; }

    public NewTracking GetTracking(UUID trackingId){
        NewTracking newTracking =  realm.where(NewTracking.class)
                .equalTo("trackingId", trackingId.toString())
                .findFirst();

        if (newTracking == null)
            throw new IllegalArgumentException("NewTracking with such ID doesn't exists");
        return newTracking;
    }

    public List<NewTracking> GetTrackingCollection(){
        RealmResults<NewDbModel> results = realm.where(NewDbModel.class)
                .equalTo("userId", userId).findAll();
        List<NewDbModel> modelCollection = realm.copyFromRealm(results);
        List<NewTracking> newTrackingCollectionToReturn = new ArrayList<>();
        if (modelCollection.size() == 0)
            return new ArrayList<NewTracking>();
        newTrackingCollectionToReturn.addAll(modelCollection.get(0).getNewTrackingCollection());
        Collections.sort(newTrackingCollectionToReturn, new Comparator<NewTracking>() {
            @Override
            public int compare(NewTracking newTracking, NewTracking t1) {
                return t1.GetDateOfChange().compareTo(newTracking.GetDateOfChange());
            }
        });
        return newTrackingCollectionToReturn;
    }

    public void AddNewTracking(NewTracking newTracking){
       realm.beginTransaction();

        boolean contains = false;

        if (realm.where(NewTracking.class)
                .equalTo("trackingId", newTracking.getTrackingId()).findFirst() != null)
            contains = true;

       NewDbModel model = realm.where(NewDbModel.class)
                .equalTo("userId", userId).findFirst();

        if (model == null) {
            model = new NewDbModel(new ArrayList<NewTracking>(), userId);
            realm.insert(model);
        }

        if (!contains) {
            model.getNewTrackingCollection().add(newTracking);
            realm.copyToRealmOrUpdate(model);
        }
        else throw new IllegalArgumentException("NewTracking with such ID already exists");

        realm.commitTransaction();
    }

    public void addEvent(UUID trackingId, NewEvent newNewEvent){
        NewTracking newTracking = realm.where(NewTracking.class)
                .equalTo("trackingId", trackingId.toString()).findFirst();

        if(newTracking == null)
            throw new IllegalArgumentException("NewTracking with such ID doesn't exists");

        realm.beginTransaction();
        newTracking.AddEvent(newNewEvent);
        NewDbModel model = realm.where(NewDbModel.class)
                .equalTo("userId", userId).findFirst();

        if (model == null)
            throw new IllegalArgumentException ("User with such ID doesn't exists");

        model.getNewEventCollection().add(newNewEvent);
        realm.commitTransaction();
    }

    public NewEvent getEvent(UUID eventId){
        return realm.where(NewEvent.class).
                equalTo("eventId", eventId.toString())
                .findFirst();
    }

    public void editEvent(UUID trackingId, UUID eventId,
                          Double newScale,
                          Rating newRating,
                          String newComment,
                          Date newDate){
        NewTracking newTracking = realm.where(NewTracking.class)
                .equalTo("trackingId", trackingId.toString()).findFirst();
        if(newTracking == null)
            throw new IllegalArgumentException("NewTracking with such ID doesn't exists");

        realm.beginTransaction();
        newTracking.EditEvent(eventId, newScale, newRating, newComment, newDate);
        realm.commitTransaction();
    }

    public void editTracking(UUID trackingId,
                             TrackingCustomization editedCounter,
                             TrackingCustomization editedScale,
                             TrackingCustomization editedComment,
                             String editedTrackingName,
                             String scaleName,
                             String color)
    {
        NewTracking newTracking = realm.where(NewTracking.class)
                .equalTo("trackingId", trackingId.toString()).findFirst();

        if(newTracking == null)
            throw new IllegalArgumentException("NewTracking with such ID doesn't exists");

        realm.beginTransaction();
        newTracking.EditTracking(editedCounter, editedScale, editedComment,
                editedTrackingName, scaleName, color);
        realm.commitTransaction();
    }

    public void deleteTracking(UUID trackingId){
        NewTracking newTracking = realm.where(NewTracking.class)
                .equalTo("trackingId", trackingId.toString()).findFirst();

        if (newTracking == null)
            throw new IllegalArgumentException("NewTracking with such ID doesn't exists");

        realm.beginTransaction();

        newTracking.setDeleted(true);
        realm.copyToRealmOrUpdate(newTracking);

        realm.commitTransaction();
    }

    public void deleteEvent(UUID eventId){
        NewEvent newEvent = realm.where(NewEvent.class)
                .equalTo("eventId", eventId.toString()).findFirst();

        if (newEvent == null)
            throw new IllegalArgumentException("NewTracking with such ID doesn't exists");

        realm.beginTransaction();

        newEvent.setDeleted(true);
        realm.copyToRealmOrUpdate(newEvent);

        realm.commitTransaction();
    }

    public List<NewEvent> FilterEvents(List<UUID> trackingId, Date dateFrom, Date dateTo,
                                       Comparison scaleComparison, Double scale,
                                       Comparison ratingComparison, Rating rating) {

        List<NewEvent> notFilteredNewEvents = getEventsForFilter();
        List<NewEvent> filteredNewEvents = new ArrayList<>();

        filteredNewEvents.addAll(notFilteredNewEvents);

        if (trackingId != null) {
            filteredNewEvents = new ArrayList<>();
            for (NewEvent newEvent : notFilteredNewEvents) {
                if (trackingId.contains(newEvent.GetTrackingId()))
                    filteredNewEvents.add(newEvent);
            }
        }

        if (dateFrom != null && dateTo != null) {
            notFilteredNewEvents = new ArrayList<>();
            notFilteredNewEvents.addAll(filteredNewEvents);
            filteredNewEvents = new ArrayList<>();
            for (NewEvent newEvent : notFilteredNewEvents) {
                if (newEvent.GetEventDate().compareTo(dateFrom) >= 0 && newEvent.GetEventDate().compareTo(dateTo) <= 0)
                    filteredNewEvents.add(newEvent);
            }
        }else{
            if(dateFrom!=null){
                notFilteredNewEvents = new ArrayList<>();
                notFilteredNewEvents.addAll(filteredNewEvents);
                filteredNewEvents = new ArrayList<>();
                for (NewEvent newEvent : notFilteredNewEvents) {
                    if (newEvent.GetEventDate().compareTo(dateFrom) >= 0)
                        filteredNewEvents.add(newEvent);
                }
            }
            if(dateTo!=null){
                notFilteredNewEvents = new ArrayList<>();
                notFilteredNewEvents.addAll(filteredNewEvents);
                filteredNewEvents = new ArrayList<>();
                for (NewEvent newEvent : notFilteredNewEvents) {
                    if (newEvent.GetEventDate().compareTo(dateTo) <= 0)
                        filteredNewEvents.add(newEvent);
                }
            }
        }

        if (scaleComparison != null && scale != null) {
            notFilteredNewEvents = new ArrayList<>();
            notFilteredNewEvents.addAll(filteredNewEvents);
            filteredNewEvents = new ArrayList<>();
            for (NewEvent newEvent : notFilteredNewEvents) {
                if(newEvent.GetScale()!=null)
                if (CompareValues(scaleComparison, newEvent.GetScale(), scale))
                    filteredNewEvents.add(newEvent);
            }
        }

        if (ratingComparison != null && rating != null) {
            notFilteredNewEvents = new ArrayList<>();
            notFilteredNewEvents.addAll(filteredNewEvents);
            filteredNewEvents = new ArrayList<>();
            for (NewEvent newEvent : notFilteredNewEvents) {
                if (newEvent.GetRating() != null)
                    if (CompareValues(ratingComparison,
                            newEvent.GetRating().GetRatingValue().doubleValue(),
                            rating.GetRatingValue().doubleValue()))
                        filteredNewEvents.add(newEvent);
            }
        }

        return RemoveDeletedEventsAndTrackingsFromCollection(
                filteredNewEvents);
    }

    public void ChangeTracking(NewTracking newTracking) {
        realm.beginTransaction();

        realm.copyToRealmOrUpdate(newTracking);

        realm.commitTransaction();
    }

    public List<NewEvent> getEventCollection(UUID trackingId){
        realm.beginTransaction();
        List<NewEvent> collection = realm.where(NewTracking.class)
                .equalTo("trackingId", trackingId.toString()).findFirst()
                .GetEventCollection();
        realm.commitTransaction();
        return collection;
    }

    public void configureRealm()
    {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("ItHappened.realm").schemaVersion(1).migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm dynamicRealm, long oldVersion, long newVersion) {
                        if (oldVersion == 0) {
                            RealmSchema schema = dynamicRealm.getSchema();

                            RealmObjectSchema newEventSchema = schema.create("NewEvent");
                            RealmObjectSchema newTrackingSchema = schema.create("NewTracking");
                            RealmObjectSchema newDbModelSchema = schema.create("NewDbModel");
                            RealmObjectSchema ratingSchema = schema.get("Rating");

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
                            newTrackingSchema.addRealmListField("newEventCollection", newEventSchema);

                            newDbModelSchema.addField("userId", String.class).addPrimaryKey("userId");
                            newDbModelSchema.addRealmListField("newEventCollection", newEventSchema);
                            newDbModelSchema.addRealmListField("newTrackingCollection", newTrackingSchema);

                            RealmObjectSchema eventSchema = schema.get("Event");

                            RealmObjectSchema dbModelSchema = schema.get("DbModel");
                            dbModelSchema.addRealmListField("eventCollection", schema.get(Event.class.getSimpleName()));

                            RealmObjectSchema trackingSchema = schema.get("Tracking");
                            trackingSchema.addField("color", String.class);

                            trackingSchema.transform(new RealmObjectSchema.Function() {
                                @Override
                                public void apply(DynamicRealmObject dynamicRealmObject) {
                                    dynamicRealmObject.setString("color", "11119017");
                                    final String id = dynamicRealmObject.getString("trackingId");
                                }
                            });
                        }
                    }
                }).build();

        Realm oldRealm = Realm.getInstance(config);

        RealmConfiguration newConfig = new RealmConfiguration.Builder()
                .name("ItHappened.realmV2").schemaVersion(1).build();


        realm = Realm.getInstance(newConfig);

        if(realm.isEmpty()) {
            RealmResults<DbModel> modelsToSave = oldRealm.where(DbModel.class).findAll();

            realm.beginTransaction();
            oldRealm.beginTransaction();

            for (DbModel dbModel : modelsToSave )
                realm.insert(new NewDbModel(dbModel));
            oldRealm.deleteAll();

            List<NewDbModel> models = realm.where(NewDbModel.class).isEmpty("newEventCollection")
                    .isNotEmpty("newTrackingCollection").findAll();
            for (NewDbModel model : models) {
                model.setNewEventCollection(addEventsToEventCollection(model.getNewTrackingCollection(), model.getUserId()));
                realm.copyToRealmOrUpdate(model);
            }
            oldRealm.commitTransaction();
            realm.commitTransaction();
        }
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

    private RealmList<NewEvent> addEventsToEventCollection(List<NewTracking> newTrackingCollection, String userId){

        NewDbModel newDbModel = realm.where(NewDbModel.class).
                equalTo("userId", userId).findFirst();
        RealmList<NewEvent> newEventCollection = newDbModel.getNewEventCollection();

        if (newEventCollection.size() == 0){
            for (NewTracking newTracking : newTrackingCollection) {
                newEventCollection.addAll(newTracking.GetEventCollection());
            }
        }
        else
        for (NewTracking newTracking : newTrackingCollection) {
            List<NewEvent> newEventList = newTracking.getNewEventCollection();
            for (NewEvent newEvent : newEventList){
                boolean contains = false;

                for (NewEvent newEventInCollection : newEventCollection) {
                    if(newEventInCollection.GetEventId().equals(newEvent.GetEventId()))
                        contains = true;
                }
                if (!contains)
                    newEventCollection.add(newEvent);
            }
        }
        return newEventCollection;
    }

    private List<NewEvent> getEventsForFilter(){
        return realm.where(NewDbModel.class).equalTo("userId", userId).findFirst().getNewEventCollection();
    }

    private List<NewEvent> RemoveDeletedEventsAndTrackingsFromCollection(List<NewEvent> collection)
    {
        List<NewEvent> collectionToReturn = new ArrayList<>();
        for (NewEvent newEvent : collection )
        {
            if (!newEvent.isDeleted())
                collectionToReturn.add(newEvent);
        }
        return collectionToReturn;
    }

    Context context;
    Realm realm;
    private String userId;
}