package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.ui.presenters.CreateTrackingContract;
import ru.lod_misis.ithappened.ui.presenters.CreateTrackingPresenterImpl;

@Module(includes = MainModule.class)
public class AddNewTrackingModule {

    @Provides
    public CreateTrackingContract.CreateTrackingPresenter provideAddNewTrackingPresenter (TrackingService trackingService ,
                                                                                          FactService factService) {
        return new CreateTrackingPresenterImpl(trackingService , factService);
    }

}
