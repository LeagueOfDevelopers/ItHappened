package ru.lod_misis.ithappened.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.data.repository.InMemoryFactRepositoryImpl;
import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.ui.presenters.StatisticsContract;
import ru.lod_misis.ithappened.ui.presenters.StatisticsInteractorImpl;

@Module(includes = MainModule.class)
public class StatisticsModule {

    @Provides
    public StatisticsContract.StatisticsInteractor provideStatisticsInteractor(Context context, FactService factService){
        return new StatisticsInteractorImpl(context, factService);
    }

}
