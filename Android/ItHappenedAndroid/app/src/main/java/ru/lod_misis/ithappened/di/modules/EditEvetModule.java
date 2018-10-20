package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Presenters.EditEventContract;
import ru.lod_misis.ithappened.Presenters.EditEventPresenterImpl;
import ru.lod_misis.ithappened.Statistics.FactCalculator;

@Module
public class EditEvetModule {

    @Provides
    public EditEventContract.EditEventPresenter provideEditEventPresenter(TrackingService trackingService, FactCalculator factCalculator) {
        return new EditEventPresenterImpl(trackingService, factCalculator);
    }

}
