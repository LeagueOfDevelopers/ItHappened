package com.example.ithappenedandroid;

import android.content.Context;

import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.Infrastructure.TrackingRepository;

public class StaticInMemoryRepository {

    private ITrackingRepository instance;

    public StaticInMemoryRepository(Context context){
        instance = new TrackingRepository(context);
    }

    public ITrackingRepository getInstance(){
        return instance;
    }

}
