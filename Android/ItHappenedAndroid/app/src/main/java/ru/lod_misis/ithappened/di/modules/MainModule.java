package ru.lod_misis.ithappened.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Domain.TrackingService;
import ru.lod_misis.ithappened.Data.Repositories.ITrackingRepository;
import ru.lod_misis.ithappened.Data.Repositories.InMemoryFactRepository;
import ru.lod_misis.ithappened.Data.Repositories.TrackingRepository;
import ru.lod_misis.ithappened.Domain.Statistics.FactCalculator;

@Module
public class MainModule {

    Context context;

    public MainModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences(Context context){
        return context.getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
    }

    @Provides
    public String provideUserId(SharedPreferences sharedPreferences){
        if (sharedPreferences.getString("LastId", "").isEmpty()) {
            return sharedPreferences.getString("Offline", "");
        } else {
            return sharedPreferences.getString("LastId", "");
        }
    }

    @Singleton
    @Provides
    public ITrackingRepository provideTrackingRepository(Context context, String userId){
        return new TrackingRepository(context, userId);
    }

    @Singleton
    @Provides
    public InMemoryFactRepository provideFactRepository(){
        return new InMemoryFactRepository();
    }

    @Singleton
    @Provides
    public TrackingService provideTrackingService(ITrackingRepository trackingRepository){
        return new TrackingService(trackingRepository);
    }

    @Singleton
    @Provides
    public FactCalculator provideFactCalculator(ITrackingRepository trackingRepository){
        return new FactCalculator(trackingRepository);
    }
}
