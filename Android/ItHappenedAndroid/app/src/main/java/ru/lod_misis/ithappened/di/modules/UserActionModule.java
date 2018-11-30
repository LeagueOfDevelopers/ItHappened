package ru.lod_misis.ithappened.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.ui.presenters.UserActionContract;
import ru.lod_misis.ithappened.ui.presenters.UserActionPresenterImpl;

@Module(includes = {MainModule.class})
public class UserActionModule {

    @Provides
    public UserActionContract.UserActionPresenter provideUserActionPresenter(Context context,
                                                                             SharedPreferences sharedPreferences,
                                                                             TrackingDataSource repository){
        return new UserActionPresenterImpl(context, sharedPreferences, repository);
    }

}
