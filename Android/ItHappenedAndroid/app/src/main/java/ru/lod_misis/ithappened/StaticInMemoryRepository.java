package ru.lod_misis.ithappened;

import android.content.Context;

import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.TrackingRepository;

public class StaticInMemoryRepository {

    private static ITrackingRepository instance;

    public static void setInstance(Context context, String userId)
    {
        if (instance != null) {
            setUserId(userId);
            return;
        }
        instance = new TrackingRepository(context, userId);
        instance.configureRealm();
    }

    public static void setUserId(String userId) { instance.setUserId(userId); }

    public static ITrackingRepository getInstance(){
        return instance;
    }

}
