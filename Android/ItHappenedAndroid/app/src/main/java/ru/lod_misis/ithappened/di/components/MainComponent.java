package ru.lod_misis.ithappened.di.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.lod_misis.ithappened.Activities.AddNewEventActivity;
import ru.lod_misis.ithappened.Activities.AddNewTrackingActivity;
import ru.lod_misis.ithappened.Activities.EditEventActivity;
import ru.lod_misis.ithappened.Activities.EditTrackingActivity;
import ru.lod_misis.ithappened.Activities.EventDetailsActivity;
import ru.lod_misis.ithappened.Activities.EventsForTrackingActivity;
import ru.lod_misis.ithappened.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Fragments.DeleteEventFromFragmentDialog;
import ru.lod_misis.ithappened.Fragments.EventsFragment;
import ru.lod_misis.ithappened.Fragments.ProfileSettingsFragment;
import ru.lod_misis.ithappened.Fragments.StatisticsFragment;
import ru.lod_misis.ithappened.Fragments.TrackingsFragment;
import ru.lod_misis.ithappened.NotificationJobService;
import ru.lod_misis.ithappened.Retrofit.ItHappenedApplication;
import ru.lod_misis.ithappened.di.modules.AddNewEventModule;
import ru.lod_misis.ithappened.di.modules.AddNewTrackingModule;
import ru.lod_misis.ithappened.di.modules.EditTrackingModule;
import ru.lod_misis.ithappened.di.modules.EventDetailsModule;
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
        EventDetailsModule.class,
        EventsHistoryModule.class,
        ProfileSettingsModule.class,
        StatisticsModule.class,
        TrackingsModule.class,
        UserActionModule.class,
        EditTrackingModule.class})
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

}
