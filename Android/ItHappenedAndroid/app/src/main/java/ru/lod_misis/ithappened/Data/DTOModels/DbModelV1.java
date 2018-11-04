package ru.lod_misis.ithappened.Data.DTOModels;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Models.Tracking;

public class DbModelV1 extends RealmObject {
    private RealmList<TrackingV1> trackingCollection;
    @PrimaryKey
    private String userId;

    public DbModelV1(List<TrackingV1> trackingCollection, String userId) {
        this.trackingCollection =  new RealmList<>();
        this.trackingCollection.addAll(trackingCollection);
        this.userId = userId;
    }


    public DbModelV1(DbModel dbModel){
        this.trackingCollection =  new RealmList<>();
        for (Tracking tracking : dbModel.trackingCollection) {
            this.trackingCollection.add(new TrackingV1(tracking));
        }
        this.userId = dbModel.userId;
    }

    public DbModelV1(){
    }

    public RealmList<TrackingV1> getTrackingCollection() {
        return trackingCollection;
    }

    public void setTrackingCollection(RealmList<TrackingV1> trackingCollection) {
        this.trackingCollection = trackingCollection;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}