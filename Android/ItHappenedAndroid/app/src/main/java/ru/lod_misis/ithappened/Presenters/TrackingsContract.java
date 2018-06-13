package ru.lod_misis.ithappened.Presenters;

import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.NewTracking;

public interface TrackingsContract {
    interface TrackingsView{
        void showTrackings(List<NewTracking> visibaleNewTrackings);
        void showMessage(String message);
    }
    interface TrackingsPresenter{
        void loadTrackings();
        void deleteTracking(UUID trackingId);
        void cancelDeleting();
    }
}
