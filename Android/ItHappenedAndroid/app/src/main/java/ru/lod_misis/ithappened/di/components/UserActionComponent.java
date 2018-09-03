package ru.lod_misis.ithappened.di.components;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import ru.lod_misis.ithappened.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.di.modules.UserActionModule;
import ru.lod_misis.ithappened.di.scopes.UserActionScope;

@UserActionScope
@Component(modules = {UserActionModule.class})
interface UserActionComponent {

    void inject(UserActionsActivity userActionsActivity);
}
