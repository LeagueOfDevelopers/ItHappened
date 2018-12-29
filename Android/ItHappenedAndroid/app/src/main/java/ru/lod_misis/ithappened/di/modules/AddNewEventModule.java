package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.ui.presenters.AddNewEventContract;
import ru.lod_misis.ithappened.ui.presenters.AddNewEventPresenterImpl;

@Module(includes = MainModule.class)
public class AddNewEventModule {

    @Provides
    public AddNewEventContract.AddNewEventPresenter provieAddNewEventPresenter(TrackingDataSource trackingRepository,
                                                                               TrackingService trackingService,
                                                                               FactService factService){
        return new AddNewEventPresenterImpl(trackingRepository, trackingService, factService);
    }

}
