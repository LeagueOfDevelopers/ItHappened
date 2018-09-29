package ru.lod_misis.ithappened.di.modules;

import android.support.transition.Visibility;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Presenters.EditTrackingContract;
import ru.lod_misis.ithappened.Presenters.EditTrackingPresenterImpl;

@Module
public class EditTrackingModule {

    @Provides
    public EditTrackingContract.EditTrackingPresenter provideEditTrackingPresenter(TrackingService service) {
        return new EditTrackingPresenterImpl(service);
    }

}
