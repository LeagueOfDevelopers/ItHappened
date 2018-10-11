package ru.lod_misis.ithappened.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Presenters.UserActionContract;
import ru.lod_misis.ithappened.Presenters.UserActionPresenterImpl;

@Module(includes = {MainModule.class})
public class UserActionModule {

    @Provides
    public UserActionContract.UserActionPresenter provideUserActionPresenter(Context context,
                                                                             SharedPreferences sharedPreferences,
                                                                             ITrackingRepository repository){
        return new UserActionPresenterImpl(context, sharedPreferences, repository);
    }

}
