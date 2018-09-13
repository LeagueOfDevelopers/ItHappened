package ru.lod_misis.ithappened.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import ru.lod_misis.ithappened.Presenters.ProfileSettingsFragmentContract;
import ru.lod_misis.ithappened.Presenters.ProfileSettingsFragmentPresenterImpl;

@Module(includes = MainModule.class)
public class ProfileSettingsModule {

    @Provides
    public ProfileSettingsFragmentContract.ProfileSettingsFragmentPresenter provideProvideSettingsPresenter(SharedPreferences sharedPreferences,
                                                                                                            Context context) {
        return new ProfileSettingsFragmentPresenterImpl(sharedPreferences, context);
    }

}
