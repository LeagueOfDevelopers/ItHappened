package ru.lod_misis.ithappened.Presenters;

import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Tracking;

public interface TrackingsContract {
    interface TrackingsView{
        void showTrackings(List<Tracking> visibaleTrackings);
        void showMessage(String message);
    }
    interface TrackingsPresenter{
        void loadTrackings();
        void deleteTracking(UUID trackingId);
        void cancelDeleting();
    }
}
