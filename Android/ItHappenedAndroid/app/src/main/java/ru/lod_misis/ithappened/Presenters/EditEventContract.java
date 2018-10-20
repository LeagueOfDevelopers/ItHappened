package ru.lod_misis.ithappened.Presenters;

import java.util.Date;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;

public interface EditEventContract {

    interface EditEventView {
        void showEditResult();

        void showMessage(String message);

        void onViewCreated(TrackingCustomization comment,
                           TrackingCustomization rating,
                           TrackingCustomization scale,
                           TrackingCustomization photo,
                           TrackingCustomization geoposition,
                           Date eventDate,
                           String commentValue,
                           Double scaleValue,
                           Rating ratingValue,
                           String scaleName);

        String getComment();
        Double getScale();
        Rating getRating();
        Double getLongitude();
        Double getLatitude();
        String getPhotoPath();
        String getDate();
        void reportEvent(int resourceId);
    }

    interface EditEventPresenter {

        void onViewAttached(EditEventView view);

        void addEventClick(int commentState, int ratingState, int scaleState, int geopositionState);

        void onViewCreated();

        void setIdentificators(UUID trackingId, UUID eventId);

        void onViewDettached();
    }

}
