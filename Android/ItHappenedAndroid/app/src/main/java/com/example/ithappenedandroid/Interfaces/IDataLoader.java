package com.example.ithappenedandroid.Interfaces;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;

import java.util.List;

public interface IDataLoader {

List<Event> loadingEvents();
List<Tracking> loadingTrackings();

}
