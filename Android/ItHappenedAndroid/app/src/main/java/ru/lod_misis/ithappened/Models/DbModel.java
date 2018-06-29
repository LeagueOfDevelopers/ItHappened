package ru.lod_misis.ithappened.Models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;

/**
 * Created by Ded on 07.06.2018.
 */

public class DbModel extends RealmObject {

    public DbModel(List<Tracking> trackingCollection, String userId) {
        this.trackingCollection =  new RealmList<>();
        this.trackingCollection.addAll(trackingCollection);
        this.userId = userId;
    }

    public DbModel(){
    }

    RealmList<Tracking> trackingCollection;
    @PrimaryKey
    String userId;
}
