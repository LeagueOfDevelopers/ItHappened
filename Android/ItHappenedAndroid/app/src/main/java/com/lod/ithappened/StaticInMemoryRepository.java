package com.lod.ithappened;

import android.content.Context;

import com.lod.ithappened.Infrastructure.ITrackingRepository;
import com.lod.ithappened.Infrastructure.TrackingRepository;

public class StaticInMemoryRepository {

    private ITrackingRepository instance;

    public StaticInMemoryRepository(Context context){
        instance = new TrackingRepository(context);
    }

    public ITrackingRepository getInstance(){
        return instance;
    }

}
