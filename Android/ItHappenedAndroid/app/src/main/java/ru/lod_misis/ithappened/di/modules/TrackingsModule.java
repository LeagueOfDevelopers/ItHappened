package ru.lod_misis.ithappened.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.ui.presenters.TrackingsContract;
import ru.lod_misis.ithappened.ui.presenters.TrackingsPresenterImpl;
import ru.lod_misis.ithappened.domain.statistics.FactCalculator;

@Module(includes = MainModule.class)
public class TrackingsModule {

    @Provides
    public TrackingsContract.TrackingsPresenter provideTrackingsPresenter(TrackingService service,
                                                                          Context context,
                                                                          FactCalculator factCalculator){
        return new TrackingsPresenterImpl(service, context, factCalculator);
    }

}
