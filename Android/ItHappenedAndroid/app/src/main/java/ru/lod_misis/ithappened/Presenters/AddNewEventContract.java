package ru.lod_misis.ithappened.Presenters;

import android.app.Activity;

import java.util.UUID;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;

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
