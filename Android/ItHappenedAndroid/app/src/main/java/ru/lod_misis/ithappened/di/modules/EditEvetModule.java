package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Domain.TrackingService;
import ru.lod_misis.ithappened.UI.Presenters.EditEventContract;
import ru.lod_misis.ithappened.UI.Presenters.EditEventPresenterImpl;
import ru.lod_misis.ithappened.Domain.Statistics.FactCalculator;

@Module
public class EditEvetModule {

    @Provides
    public EditEventContract.EditEventPresenter provideEditEventPresenter(TrackingService trackingService, FactCalculator factCalculator) {
        return new EditEventPresenterImpl(trackingService, factCalculator);
    }

}
