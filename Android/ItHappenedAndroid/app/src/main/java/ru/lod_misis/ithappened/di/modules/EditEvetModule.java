package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.ui.presenters.EditEventContract;
import ru.lod_misis.ithappened.ui.presenters.EditEventPresenterImpl;
import ru.lod_misis.ithappened.domain.statistics.FactCalculator;

@Module
public class EditEvetModule {

    @Provides
    public EditEventContract.EditEventPresenter provideEditEventPresenter(TrackingService trackingService, FactCalculator factCalculator) {
        return new EditEventPresenterImpl(trackingService, factCalculator);
    }

}
