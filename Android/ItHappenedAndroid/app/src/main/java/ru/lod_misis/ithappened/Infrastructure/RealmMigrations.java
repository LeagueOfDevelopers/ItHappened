package ru.lod_misis.ithappened.Infrastructure;

import java.util.Date;
import java.util.UUID;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class RealmMigrations implements RealmMigration {

    private UUID classId;

    RealmMigrations(){
        classId = UUID.randomUUID();
    }

    @Override
    public void migrate(DynamicRealm dynamicRealm, long oldVersion, long l1) {
        if (oldVersion == 0) {
            RealmSchema schema = dynamicRealm.getSchema();

            RealmObjectSchema newEventSchema = schema.create("EventV1");
            RealmObjectSchema newTrackingSchema = schema.create("TrackingV1");
            RealmObjectSchema newDbModelSchema = schema.create("DbModelV1");
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
            newTrackingSchema.addRealmListField("eventV1Collection", newEventSchema);


            newDbModelSchema.addField("userId", String.class).addPrimaryKey("userId");
            newDbModelSchema.addRealmListField("trackingV1Collection", newTrackingSchema);

            oldVersion++;
        }

        if (oldVersion == 1) {
            RealmSchema schema = dynamicRealm.getSchema();

            RealmObjectSchema trackingSchema = schema.get("TrackingV1");
            RealmObjectSchema eventSchema = schema.get("EventV1");

            trackingSchema.addField("geoposition", String.class);

            eventSchema.addField("lotitude", Double.class);
            eventSchema.addField("longitude", Double.class);
            eventSchema.addField("photo", String.class);

            trackingSchema.transform(new RealmObjectSchema.Function() {
                @Override
                public void apply(DynamicRealmObject dynamicRealmObject) {
                    dynamicRealmObject.set("geoposition", "None");
                }
            });
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RealmMigrations))
            return false;
        if (obj == this)
            return true;

        return classId.equals(((RealmMigrations) obj).classId);
    }

    @Override
    public int hashCode(){
        return classId.hashCode();
    }
}