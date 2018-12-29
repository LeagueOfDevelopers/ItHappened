package ru.lod_misis.ithappened.ui.presenters;

import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.domain.models.TrackingV1;

public interface TrackingsContract {
    interface TrackingsView {
        void showTrackings(List<TrackingV1> visibaleTrackingV1s);

        void showMessage(String message);
    }

    interface TrackingsPresenter {

        void onViewAttach(TrackingsView view);

        void loadTrackings();

        void deleteTracking(UUID trackingId);

        void cancelDeleting();

        void onViewDettach();
    }
}
