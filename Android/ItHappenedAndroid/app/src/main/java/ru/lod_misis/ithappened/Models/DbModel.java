package ru.lod_misis.ithappened.Models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ru.lod_misis.ithappened.Domain.Tracking;

public class DbModel extends RealmObject {
    RealmList<Tracking> trackingCollection;
    @PrimaryKey
    String userId;

    public DbModel(List<Tracking> trackingCollection, String userId) {
        this.trackingCollection =  new RealmList<>();
        this.trackingCollection.addAll(trackingCollection);
        this.userId = userId;
    }

    public DbModel(){

    }

    public RealmList<Tracking> getTrackingCollection() {
        return trackingCollection;
    }

    public void setTrackingCollection(RealmList<Tracking> trackingCollection) {
        this.trackingCollection = trackingCollection;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
