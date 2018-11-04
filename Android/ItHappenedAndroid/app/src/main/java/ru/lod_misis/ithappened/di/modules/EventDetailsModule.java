package ru.lod_misis.ithappened.di.modules;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Presenters.EventDetailsContract;
import ru.lod_misis.ithappened.Presenters.EventDetailsPresenterImpl;

@Module(includes = MainModule.class)
public class EventDetailsModule {

    @Provides
    public EventDetailsContract.EventDetailsPresenter provideEventDetailsPresenter(TrackingService service, InMemoryFactRepository factRepository){
        return new EventDetailsPresenterImpl(service, factRepository);
    }

}
