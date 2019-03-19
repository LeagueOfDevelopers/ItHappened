package ru.lod_misis.ithappened.di.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.lod_misis.ithappened.di.modules.DeleteModule;
import ru.lod_misis.ithappened.di.modules.EventDetailsModule;
import ru.lod_misis.ithappened.ui.activities.AddNewEventActivity;
import ru.lod_misis.ithappened.ui.activities.AddNewTrackingActivity;
import ru.lod_misis.ithappened.ui.activities.EditEventActivity;
import ru.lod_misis.ithappened.ui.activities.EditTrackingActivity;
import ru.lod_misis.ithappened.ui.activities.EventDetailsActivity;
import ru.lod_misis.ithappened.ui.activities.EventsForTrackingActivity;
import ru.lod_misis.ithappened.ui.activities.IntroActivity;
import ru.lod_misis.ithappened.ui.activities.UserActionsActivity;
import ru.lod_misis.ithappened.ui.dialog.DeleteEventDialog;
import ru.lod_misis.ithappened.ui.dialog.DeleteEventFromFragmentDialog;
import ru.lod_misis.ithappened.ui.fragments.EventsFragment;
import ru.lod_misis.ithappened.ui.fragments.IntroLastFragment;
import ru.lod_misis.ithappened.ui.fragments.ProfileSettingsFragment;
import ru.lod_misis.ithappened.ui.fragments.StartIntroFragment;
import ru.lod_misis.ithappened.ui.fragments.StatisticsFragment;
import ru.lod_misis.ithappened.ui.fragments.TrackingsFragment;
import ru.lod_misis.ithappened.ui.background.NotificationJobService;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.di.modules.AddNewEventModule;
import ru.lod_misis.ithappened.di.modules.AddNewTrackingModule;
import ru.lod_misis.ithappened.di.modules.EditEvetModule;
import ru.lod_misis.ithappened.di.modules.EditTrackingModule;
import ru.lod_misis.ithappened.di.modules.EventsHistoryModule;
import ru.lod_misis.ithappened.di.modules.MainModule;
import ru.lod_misis.ithappened.di.modules.ProfileSettingsModule;
import ru.lod_misis.ithappened.di.modules.StatisticsModule;
import ru.lod_misis.ithappened.di.modules.TrackingsModule;
import ru.lod_misis.ithappened.di.modules.UserActionModule;

@Singleton
@Component(modules = {MainModule.class,
        AddNewEventModule.class,
        AddNewTrackingModule.class,
        DeleteModule.class,
        EventsHistoryModule.class,
        ProfileSettingsModule.class,
        StatisticsModule.class,
        TrackingsModule.class,
        UserActionModule.class,
        EditTrackingModule.class,
        EditEvetModule.class,
        EventDetailsModule.class,
})

public interface MainComponent {

    void inject(UserActionsActivity activity);

    void inject(TrackingsFragment fragment);

    void inject(AddNewTrackingActivity addNewTrackingActivity);

    void inject(AddNewEventActivity addNewEventActivity);

    void inject(EventsFragment fragment);

    void inject(ProfileSettingsFragment fragment);

    void inject(StatisticsFragment statisticsFragment);

    void inject(EditEventActivity activity);

    void inject(EditTrackingActivity activity);

    void inject(EventDetailsActivity activity);

    void inject(EventsForTrackingActivity activity);

    void inject(DeleteEventFromFragmentDialog dialog);

    void inject(NotificationJobService service);

    void inject(ItHappenedApplication itHappenedApplication);

    void inject(IntroLastFragment introLastFragment);

    void inject(StartIntroFragment startIntroFragment);

    void inject(IntroActivity introActivity);

    void inject(DeleteEventDialog deleteEventDialog);

}
