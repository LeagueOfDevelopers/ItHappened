package ru.lod_misis.ithappened.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Presenters.UserActionContract;
import ru.lod_misis.ithappened.Presenters.UserActionPresenterImpl;
import ru.lod_misis.ithappened.di.scopes.UserActionScope;

@Module(includes = {MainModule.class})
public class UserActionModule {

    @UserActionScope
    @Provides
    public UserActionContract.UserActionPresenter provideUserActionPresenter(Context context,
                                                                             SharedPreferences sharedPreferences,
                                                                             ITrackingRepository repository){
        return new UserActionPresenterImpl(context, sharedPreferences, repository);
    }

}
