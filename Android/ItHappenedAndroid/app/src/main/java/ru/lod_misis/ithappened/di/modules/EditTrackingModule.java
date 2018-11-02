package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Domain.TrackingService;
import ru.lod_misis.ithappened.UI.Presenters.EditTrackingContract;
import ru.lod_misis.ithappened.UI.Presenters.EditTrackingPresenterImpl;

@Module
public class EditTrackingModule {

    @Provides
    public EditTrackingContract.EditTrackingPresenter provideEditTrackingPresenter(TrackingService service) {
        return new EditTrackingPresenterImpl(service);
    }

}
