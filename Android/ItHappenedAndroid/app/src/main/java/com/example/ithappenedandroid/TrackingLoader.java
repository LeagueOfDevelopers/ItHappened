package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Interfaces.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class TrackingLoader implements DataLoader {

    List<Tracking> trackings = new ArrayList<Tracking>();

    @Override
    public List<Tracking> loadingData() {

        for(int i=0;i<100;i++){
            trackings.add(new Tracking("Cобытие"));
        }

        return trackings;
        }


    }
