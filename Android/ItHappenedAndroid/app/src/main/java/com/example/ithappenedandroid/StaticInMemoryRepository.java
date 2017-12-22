package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.Infrastructure.InMemoryTrackingRepository;

public class StaticInMemoryRepository {

    private static ITrackingRepository instance = new InMemoryTrackingRepository();

    private StaticInMemoryRepository(){

    }

    public static ITrackingRepository getInstance(){
        return instance;
    }

}
