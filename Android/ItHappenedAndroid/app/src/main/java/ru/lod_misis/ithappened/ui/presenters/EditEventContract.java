package ru.lod_misis.ithappened.ui.presenters;

import java.util.Date;
import java.util.UUID;

import ru.lod_misis.ithappened.domain.models.Rating;
import ru.lod_misis.ithappened.domain.models.TrackingCustomization;

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
                           Double longitude,
                           Double latitude,
                           String photoPath,
                           String title,
                           String scaleName);

        void reportEvent(int resourceId);
        void addEvent();
    }

    interface EditEventPresenter {

        void onViewAttached(EditEventView view);

        void addEventClick();

        void onViewCreated();

        void setIdentificators(UUID trackingId, UUID eventId);

        void onViewDettached();

        void finish(Double scale,Rating rating,String comment,
                    Double latitude,Double longitude,String photoPath,Date eventDate);
    }

}
