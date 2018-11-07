package ru.lod_misis.ithappened.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.data.repository.TrackingDataSourceImpl;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.data.repository.InMemoryFactRepository;
import ru.lod_misis.ithappened.domain.statistics.FactCalculator;

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
    public TrackingDataSource provideTrackingRepository(Context context, String userId){
        return new TrackingDataSourceImpl(context, userId);
    }

    @Singleton
    @Provides
    public InMemoryFactRepository provideFactRepository(){
        return new InMemoryFactRepository();
    }

    @Singleton
    @Provides
    public TrackingService provideTrackingService(TrackingDataSource trackingRepository){
        return new TrackingService(trackingRepository);
    }

    @Singleton
    @Provides
    public FactCalculator provideFactCalculator(TrackingDataSource trackingRepository){
        return new FactCalculator(trackingRepository);
    }
}
