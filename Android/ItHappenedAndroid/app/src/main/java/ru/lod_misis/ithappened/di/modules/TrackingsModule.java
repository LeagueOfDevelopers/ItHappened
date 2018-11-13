package ru.lod_misis.ithappened.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.ui.presenters.TrackingsContract;
import ru.lod_misis.ithappened.ui.presenters.TrackingsPresenterImpl;

@Module(includes = MainModule.class)
public class TrackingsModule {

    @Provides
    public TrackingsContract.TrackingsPresenter provideTrackingsPresenter(TrackingService service,
                                                                          Context context,
                                                                          FactService factService){
        return new TrackingsPresenterImpl(service, context, factService);
    }

}
