package ru.lod_misis.ithappened.ui.presenters;

import ru.lod_misis.ithappened.domain.models.TrackingV1;

public interface CreateTrackingContract {
    interface CreateTrackingView{
        void createTracking();
        void requestPermissionForGeoposition();
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
