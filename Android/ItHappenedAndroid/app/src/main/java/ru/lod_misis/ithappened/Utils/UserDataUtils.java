package ru.lod_misis.ithappened.Utils;

import android.content.SharedPreferences;

import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.StaticInMemoryRepository;

import static android.content.Context.MODE_PRIVATE;

public class  UserDataUtils {
    public static ITrackingRepository setUserDataSet(SharedPreferences sharedPreferences){
        ITrackingRepository trackingRepository;
        if (sharedPreferences.getString("LastId", "").isEmpty()) {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
            trackingRepository = StaticInMemoryRepository.getInstance();
        } else {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
            trackingRepository = StaticInMemoryRepository.getInstance();
        }
        return trackingRepository;
    }
}
