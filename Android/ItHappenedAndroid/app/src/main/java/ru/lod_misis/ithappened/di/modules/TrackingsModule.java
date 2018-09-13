package ru.lod_misis.ithappened.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Presenters.TrackingsContract;
import ru.lod_misis.ithappened.Presenters.TrackingsPresenterImpl;
import ru.lod_misis.ithappened.Statistics.FactCalculator;

@Module
public class TrackingsModule {

    @Provides
    public TrackingsContract.TrackingsPresenter provideTrackingsPresenter(TrackingService service,
                                                                          Context context,
                                                                          FactCalculator factCalculator){
        return new TrackingsPresenterImpl(service, context, factCalculator);
    }

}
