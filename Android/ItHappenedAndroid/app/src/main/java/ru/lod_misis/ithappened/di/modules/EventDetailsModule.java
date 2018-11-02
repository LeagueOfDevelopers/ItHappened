package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Domain.TrackingService;
import ru.lod_misis.ithappened.Data.Repositories.InMemoryFactRepository;
import ru.lod_misis.ithappened.UI.Presenters.EventDetailsContract;
import ru.lod_misis.ithappened.UI.Presenters.EventDetailsPresenterImpl;

@Module(includes = MainModule.class)
public class EventDetailsModule {

    @Provides
    public EventDetailsContract.EventDetailsPresenter provideEventDetailsPresenter(TrackingService service, InMemoryFactRepository factRepository){
        return new EventDetailsPresenterImpl(service, factRepository);
    }

}
