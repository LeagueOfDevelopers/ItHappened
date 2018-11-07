package ru.lod_misis.ithappened.ui.presenters;


import java.util.UUID;

import ru.lod_misis.ithappened.domain.TrackingService;

public interface EventDetailsContract {
    interface EventDetailsView {
        void startConfigurationView();

        void startedConfiguration(TrackingService service, UUID trackingId, UUID eventId);

        void showMessage(String message);

        void finishDetailsEventActivity();

        void deleteEvent();

        void editEvent();

    }

    interface EventDetailsPresenter {
        void init();

        void attachView(EventDetailsView eventDetailsView, UUID trackingId, UUID eventId);

        void detachView();

        void deleteEvent();

        void okClicked();

        void canselClicked();

        void editEvent();

        void beforeFinish();

        Boolean isViewAttached();
    }
}
