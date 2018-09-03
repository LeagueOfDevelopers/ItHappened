package ru.lod_misis.ithappened.di.components;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import ru.lod_misis.ithappened.di.modules.MainModule;

@Singleton
@Component(modules = {MainModule.class})
public interface MainComponent {
}
