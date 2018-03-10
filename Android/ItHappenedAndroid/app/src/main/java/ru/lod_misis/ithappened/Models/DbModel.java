package ru.lod_misis.ithappened.Models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ru.lod_misis.ithappened.Domain.Tracking;

/**
 * Created by Ded on 10.03.2018.
 */

public class DbModel extends RealmObject {
    RealmList<Tracking> trackingCollection;
    @PrimaryKey
    String userId;

    public DbModel(List<Tracking> trackingCollection, String userId) {
        this.trackingCollection =  new RealmList<>();
        this.trackingCollection.addAll(trackingCollection);
        this.userId = userId;
    }

    public DbModel(){}

    public List<Tracking> getTrackingCollection() {
        return trackingCollection;
    }

    public void setTrackingCollection(List<Tracking> trackingCollection) {
        this.trackingCollection =  new RealmList<>();
        this.trackingCollection.addAll(trackingCollection);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
