package ru.lod_misis.ithappened.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Data.Repositories.InMemoryFactRepository;
import ru.lod_misis.ithappened.UI.Presenters.StatisticsContract;
import ru.lod_misis.ithappened.UI.Presenters.StatisticsInteractorImpl;

@Module(includes = MainModule.class)
public class StatisticsModule {

    @Provides
    public StatisticsContract.StatisticsInteractor provideStatisticsInteractor(Context context, InMemoryFactRepository repository){
        return new StatisticsInteractorImpl(context, repository);
    }

}
