package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.Infrastructure.InMemoryTrackingRepository;

public class RepositorySingleton {

    private static ITrackingRepository instance;

    public static ITrackingRepository getInstance(){
        if(instance == null){
            instance = new InMemoryTrackingRepository();
        }
        return instance;
    }

}
