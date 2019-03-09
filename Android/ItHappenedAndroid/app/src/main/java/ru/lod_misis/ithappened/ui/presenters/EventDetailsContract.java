package ru.lod_misis.ithappened.ui.presenters;

import java.util.UUID;

import ru.lod_misis.ithappened.domain.models.TrackingV1;

public interface EventDetailsContract {

    interface EventDetailsView  {

        void startConfigurationView();

        void startedConfiguration(TrackingV1 tracking);
    }

    interface EventDetailsPresenter{

        void init(UUID trackingId);

        void attachView(EventDetailsView deleteView);

        void detachView();

        Boolean isViewAttached();
    }
}
