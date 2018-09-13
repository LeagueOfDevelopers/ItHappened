package ru.lod_misis.ithappened.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Presenters.StatisticsContract;
import ru.lod_misis.ithappened.Presenters.StatisticsInteractorImpl;
import ru.lod_misis.ithappened.Presenters.StatisticsPresenterImpl;

@Module(includes = MainModule.class)
public class StatisticsModule {

    @Provides
    public StatisticsContract.StatisticsInteractor provideStatisticsInteractor(Context context, InMemoryFactRepository repository){
        return new StatisticsInteractorImpl(context, repository);
    }

}
