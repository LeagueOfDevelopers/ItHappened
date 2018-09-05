package ru.lod_misis.ithappened.Models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Domain.Tracking;

public class DbModelV1 extends RealmObject {
    private RealmList<TrackingV1> trackingV1Collection;
    @PrimaryKey
    private String userId;

    public DbModelV1(List<TrackingV1> trackingV1Collection, String userId) {
        this.trackingV1Collection =  new RealmList<>();
        this.trackingV1Collection.addAll(trackingV1Collection);
        this.userId = userId;
    }


    public DbModelV1(DbModel dbModel){
        this.trackingV1Collection =  new RealmList<>();
        for (Tracking tracking : dbModel.trackingCollection) {
            this.trackingV1Collection.add(new TrackingV1(tracking));
        }
        this.userId = dbModel.userId;
    }

    public DbModelV1(){
    }

    public RealmList<TrackingV1> getTrackingV1Collection() {
        return trackingV1Collection;
    }

    public void setTrackingV1Collection(RealmList<TrackingV1> trackingV1Collection) {
        this.trackingV1Collection = trackingV1Collection;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}