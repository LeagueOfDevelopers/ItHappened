package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.ui.presenters.EditTrackingContract;
import ru.lod_misis.ithappened.ui.presenters.EditTrackingPresenterImpl;

@Module
public class EditTrackingModule {

    @Provides
    public EditTrackingContract.EditTrackingPresenter provideEditTrackingPresenter(TrackingService service) {
        return new EditTrackingPresenterImpl(service);
    }

}
