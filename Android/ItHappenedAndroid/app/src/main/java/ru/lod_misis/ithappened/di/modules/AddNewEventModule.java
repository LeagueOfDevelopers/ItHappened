package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Presenters.AddNewEventContract;
import ru.lod_misis.ithappened.Presenters.AddNewEventPresenterImpl;
import ru.lod_misis.ithappened.Statistics.FactCalculator;

@Module(includes = MainModule.class)
public class AddNewEventModule {

    @Provides
    public AddNewEventContract.AddNewEventPresenter provieAddNewEventPresenter(ITrackingRepository trackingRepository,
                                                                               TrackingService trackingService,
                                                                               FactCalculator factCalculator){
        return new AddNewEventPresenterImpl(trackingRepository, trackingService, factCalculator);
    }

}
