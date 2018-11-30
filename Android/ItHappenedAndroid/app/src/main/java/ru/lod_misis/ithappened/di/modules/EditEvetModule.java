package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.ui.presenters.EditEventContract;
import ru.lod_misis.ithappened.ui.presenters.EditEventPresenterImpl;

@Module
public class EditEvetModule {

    @Provides
    public EditEventContract.EditEventPresenter provideEditEventPresenter
            (TrackingService trackingService, FactService factService) {
        return new EditEventPresenterImpl(trackingService, factService);
    }

}
