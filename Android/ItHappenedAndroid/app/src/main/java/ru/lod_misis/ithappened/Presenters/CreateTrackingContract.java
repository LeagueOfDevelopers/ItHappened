package ru.lod_misis.ithappened.Presenters;

import ru.lod_misis.ithappened.Domain.TrackingV1;

public interface CreateTrackingContract {
    interface CreateTrackingView{
        void createTracking();
        void requestPermissionForGeoposition();
        void requestPermissionForCamera();
        void startConfigurationView();
        void satredConfiguration();
        void showMessage(String message);
        void finishCreatingTracking();

    }
    interface CreateTrackingPresenter{
        void init();
        void attachView(CreateTrackingView createTrackingView);
        void detachView();
        void createNewTracking();
        void saveNewTracking(TrackingV1 newTrackingV1);
        void requestPermission(int codePermission);

        Boolean isViewAttached();
    }
}
