package ru.lod_misis.ithappened.di.modules;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Data.Repositories.ITrackingRepository;
import ru.lod_misis.ithappened.UI.Presenters.CreateTrackingContract;
import ru.lod_misis.ithappened.UI.Presenters.CreateTrackingPresenterImpl;
import ru.lod_misis.ithappened.Domain.Statistics.FactCalculator;

@Module(includes = MainModule.class)
public class AddNewTrackingModule {

    @Provides
    public CreateTrackingContract.CreateTrackingPresenter provideAddNewTrackingPresenter(SharedPreferences sharedPreferences,
                                                                                         ITrackingRepository trackingRepository,
                                                                                         FactCalculator factCalculator){
        return new CreateTrackingPresenterImpl(sharedPreferences, trackingRepository, factCalculator);
    }

}
