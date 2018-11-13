package ru.lod_misis.ithappened.ui.presenters;

import java.util.UUID;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;

public interface AddNewEventContract {
    interface AddNewEventView {
        void requestPermissionForGeoposition ();

        void startConfigurationView ();

        void startedConfiguration (TrackingV1 trackingV1);

        void showMessage (String message);

        void finishAddEventActivity ();
    }

    interface AddNewEventPresenter {

        void init (UUID trackingId);

        void attachView (AddNewEventView createTrackingView);

        void detachView ();

        void requestPermission (int codePermission);

        void saveEvent (EventV1 eventV1 , UUID trackingId);
    }
}
