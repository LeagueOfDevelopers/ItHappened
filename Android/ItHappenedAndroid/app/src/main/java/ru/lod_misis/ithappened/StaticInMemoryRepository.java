package ru.lod_misis.ithappened;

import android.content.Context;

import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.TrackingRepository;

public class StaticInMemoryRepository {

    private ITrackingRepository instance;

    public StaticInMemoryRepository(Context context, String userId){
        instance = new TrackingRepository(context, userId);
    }

    public ITrackingRepository getInstance(){
        return instance;
    }

}
