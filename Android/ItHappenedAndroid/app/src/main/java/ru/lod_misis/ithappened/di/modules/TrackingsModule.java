package ru.lod_misis.ithappened.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Domain.TrackingService;
import ru.lod_misis.ithappened.UI.Presenters.TrackingsContract;
import ru.lod_misis.ithappened.UI.Presenters.TrackingsPresenterImpl;
import ru.lod_misis.ithappened.Domain.Statistics.FactCalculator;

@Module(includes = MainModule.class)
public class TrackingsModule {

    @Provides
    public TrackingsContract.TrackingsPresenter provideTrackingsPresenter(TrackingService service,
                                                                          Context context,
                                                                          FactCalculator factCalculator){
        return new TrackingsPresenterImpl(service, context, factCalculator);
    }

}
