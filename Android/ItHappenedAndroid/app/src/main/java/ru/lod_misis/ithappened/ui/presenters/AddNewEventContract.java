package ru.lod_misis.ithappened.ui.presenters;

import android.app.Activity;

import java.util.UUID;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;

public interface AddNewEventContract {
    interface AddNewEventView{
        void addNewEvent();
        void requestPermissionForGeoposition();
        void requestPermissionForCamera();
        void startConfigurationView();
        void startedConfiguration(UUID trackingId, TrackingV1 trackingV1);
        void showMessage(String message);
        void finishAddEventActivity();
    }
    interface AddNewEventPresenter{
        void addNewEvent();
        void init(Activity activity);
        void attachView(AddNewEventView createTrackingView);
        void detachView();
        void requestPermission(int codePermission);
        void saveEvent(EventV1 eventV1,UUID trackingId);
        Boolean isViewAttached();
    }
}
