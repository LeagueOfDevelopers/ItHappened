package ru.lod_misis.ithappened.Presenters;

import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.TrackingV1;

public interface TrackingsContract {
    interface TrackingsView{
        void showTrackings(List<TrackingV1> visibaleTrackingV1s);
        void showMessage(String message);
    }
    interface TrackingsPresenter{
        void loadTrackings();
        void deleteTracking(UUID trackingId);
        void cancelDeleting();
    }
}
