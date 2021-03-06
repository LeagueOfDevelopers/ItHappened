package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.ui.presenters.EventDetailsContract;
import ru.lod_misis.ithappened.ui.presenters.EventDetailsPresenterImpl;

@Module(includes = MainModule.class)
public class EventDetailsModule {

    @Provides
    public EventDetailsContract.EventDetailsPresenter provideEventDetailsPresenter(TrackingService service) {
        return new EventDetailsPresenterImpl(service);
    }
}
