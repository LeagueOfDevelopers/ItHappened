package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Presenters.EventsHistoryContract;
import ru.lod_misis.ithappened.Presenters.EventsHistoryPresenterImpl;

@Module(includes = MainModule.class)
public class EventsHistoryModule {

    @Provides
    public EventsHistoryContract.EventsHistoryPresenter provideEventsHistoryPresenter(TrackingService service){
        return new EventsHistoryPresenterImpl(service);
    }

}
