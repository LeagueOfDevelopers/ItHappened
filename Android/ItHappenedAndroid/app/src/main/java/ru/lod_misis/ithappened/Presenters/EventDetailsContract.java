package ru.lod_misis.ithappened.Presenters;


import java.util.UUID;

import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;

public interface EventDetailsContract {
    interface EventDetailsView{
        void startConfigurationView();
        void startedConfiguration(ITrackingRepository collection,UUID trackingId, UUID eventId);
        void showMessage(String message);
        void finishDetailsEventActivity();
        void deleteEvent();
        void editEvent();

    }

    interface EventDetailsPresenter{
        void init();
        void attachView(EventDetailsView eventDetailsView);
        void detachView();
        void deleteEvent();
        void okClicked();
        void canselClicked();
        void editEvent();
        void beforeFinish();
        Boolean isViewAttached();
    }
}
