package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.domain.statistics.FactCalculator;
import ru.lod_misis.ithappened.ui.presenters.CreateTrackingContract;
import ru.lod_misis.ithappened.ui.presenters.CreateTrackingPresenterImpl;

@Module(includes = MainModule.class)
public class AddNewTrackingModule {

    @Provides
    public CreateTrackingContract.CreateTrackingPresenter provideAddNewTrackingPresenter (TrackingDataSource trackingRepository ,
                                                                                          FactCalculator factCalculator) {
        return new CreateTrackingPresenterImpl(trackingRepository , factCalculator);
    }

}
