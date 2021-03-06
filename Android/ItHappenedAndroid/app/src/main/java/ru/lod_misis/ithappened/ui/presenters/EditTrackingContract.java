package ru.lod_misis.ithappened.ui.presenters;

import java.util.UUID;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;
import ru.lod_misis.ithappened.domain.models.TrackingV1;

public interface EditTrackingContract {

    interface EditTrackingView {
        void showError(String error);
        void completeEdit();
        String getEditTrackingName();
        TrackingCustomization getCommentCustomization();
        TrackingCustomization getRatingCustomization();
        TrackingCustomization getScaleCustomization();
        TrackingCustomization getPhotoCustomization();
        TrackingCustomization getGeopositionCustomization();
        String getScaleName();
        String getTrackingColor();

    }

    interface EditTrackingPresenter {
        void onViewAttached(EditTrackingView editTrackingView);
        void onViewDettached();
        void onEditClick();
        TrackingV1 getTrackingState();
        void setTrackingId(UUID trackingId);
    }

}
