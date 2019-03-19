package ru.lod_misis.ithappened.ui.presenters;


import java.util.UUID;

import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.models.TrackingV1;

public interface DeleteContract {


    interface DeleteView {

        void showMessage(String message);

        void finishDetailsEventActivity();

    }

    interface DeletePresenter {

        void attachView(DeleteView deleteView);

        void detachView();

        void okClicked(UUID trackingId, UUID eventId);

        void canselClicked();

        Boolean isViewAttached();
    }

}
