package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.ui.presenters.EventsHistoryContract;
import ru.lod_misis.ithappened.ui.presenters.EventsHistoryPresenterImpl;

@Module(includes = MainModule.class)
public class EventsHistoryModule {

    @Provides
    public EventsHistoryContract.EventsHistoryPresenter provideEventsHistoryPresenter(TrackingService service){
        return new EventsHistoryPresenterImpl(service);
    }

}
