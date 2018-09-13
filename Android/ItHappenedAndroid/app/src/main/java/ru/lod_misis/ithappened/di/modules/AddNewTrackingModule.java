package ru.lod_misis.ithappened.di.modules;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Presenters.CreateTrackingContract;
import ru.lod_misis.ithappened.Presenters.CreateTrackingPresenterImpl;
import ru.lod_misis.ithappened.Statistics.FactCalculator;

@Module(includes = MainModule.class)
public class AddNewTrackingModule {

    @Provides
    public CreateTrackingContract.CreateTrackingPresenter provideAddNewTrackingPresenter(SharedPreferences sharedPreferences,
                                                                                         ITrackingRepository trackingRepository,
                                                                                         FactCalculator factCalculator){
        return new CreateTrackingPresenterImpl(sharedPreferences, trackingRepository, factCalculator);
    }

}
