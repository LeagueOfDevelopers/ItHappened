package ru.lod_misis.ithappened.di.modules;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Presenters.CreateTrackingPresenter;
import ru.lod_misis.ithappened.Statistics.FactCalculator;

@Module(includes = MainModule.class)
public class AddNewTrackingModule {

    @Provides
    public CreateTrackingPresenter provideAddNewTrackingPresenter(SharedPreferences sharedPreferences,
                                                                  ITrackingRepository trackingRepository,
                                                                  FactCalculator factCalculator){
        return new CreateTrackingPresenter(sharedPreferences, trackingRepository, factCalculator);
    }

}
