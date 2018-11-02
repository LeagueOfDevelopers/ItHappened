package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Domain.TrackingService;
import ru.lod_misis.ithappened.Data.Repositories.ITrackingRepository;
import ru.lod_misis.ithappened.UI.Presenters.AddNewEventContract;
import ru.lod_misis.ithappened.UI.Presenters.AddNewEventPresenterImpl;
import ru.lod_misis.ithappened.Domain.Statistics.FactCalculator;

@Module(includes = MainModule.class)
public class AddNewEventModule {

    @Provides
    public AddNewEventContract.AddNewEventPresenter provieAddNewEventPresenter(ITrackingRepository trackingRepository,
                                                                               TrackingService trackingService,
                                                                               FactCalculator factCalculator){
        return new AddNewEventPresenterImpl(trackingRepository, trackingService, factCalculator);
    }

}
