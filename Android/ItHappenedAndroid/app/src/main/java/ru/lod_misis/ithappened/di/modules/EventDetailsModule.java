package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Presenters.EventDetailsContract;

@Module
public class EventDetailsModule {

    @Provides
    public EventDetailsContract.EventDetailsPresenter provideEventDetailsPresenter(){

    }

}
