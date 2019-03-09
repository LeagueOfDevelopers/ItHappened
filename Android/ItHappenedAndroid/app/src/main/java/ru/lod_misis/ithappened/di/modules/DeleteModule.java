package ru.lod_misis.ithappened.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.ui.presenters.DeletePresenterImpl;
import ru.lod_misis.ithappened.ui.presenters.DeleteContract;

@Module(includes = MainModule.class)
public class DeleteModule {

    @Provides
    public DeleteContract.DeletePresenter provideEventDetailsPresenter(TrackingService service, FactService factService){
        return new DeletePresenterImpl(service, factService);
    }

}
