package ru.lod_misis.ithappened.Models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Domain.Tracking;

public class NewDbModel extends RealmObject {
    RealmList<NewTracking> newTrackingCollection;
    RealmList<NewEvent> newEventCollection;
    @PrimaryKey
    String userId;

    public NewDbModel(List<NewTracking> newTrackingCollection, String userId) {
        this.newTrackingCollection =  new RealmList<>();
        this.newTrackingCollection.addAll(newTrackingCollection);
        this.userId = userId;
    }

    public NewDbModel(DbModel dbModel){
        this.newTrackingCollection =  new RealmList<>();
        for (Tracking tracking : dbModel.trackingCollection) {
            this.newTrackingCollection.add(new NewTracking(tracking));
        }
        this.userId = dbModel.userId;
    }

    public NewDbModel(){
    }

    public RealmList<NewTracking> getNewTrackingCollection() {
        return newTrackingCollection;
    }

    public void setNewTrackingCollection(RealmList<NewTracking> newTrackingCollection) {
        this.newTrackingCollection = newTrackingCollection;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RealmList<NewEvent> getNewEventCollection() {
        return newEventCollection;
    }

    public void setNewEventCollection(RealmList<NewEvent> newEventCollection) {
        this.newEventCollection = newEventCollection;
    }
}
