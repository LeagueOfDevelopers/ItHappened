package ru.lod_misis.ithappened.Presenters;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingV1;

public interface EventsHistoryContract {
    interface EventsHistoryView {

        void showEvents(List<EventV1> eventV1s);

        void cancelFilters();

        void showLoading(boolean isLoading);


    }

    interface EventsHistoryPresenter {

        void onViewAttach(EventsHistoryView view);

        void filterEvents(List<UUID> trackingId, Date dateFrom, Date dateTo,
                          Comparison scaleComparison, Double scale,
                          Comparison ratingComparison, Rating rating, int startPosition, int endPosition);

        void cancleFilters();

        String prepareDataForDialog(List<TrackingV1> trackings, List<String> strings, List<UUID> uuids, List<Boolean> selectedItem);

        void onViewDettach();

    }
}
