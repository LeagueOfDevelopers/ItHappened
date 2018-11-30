package ru.lod_misis.ithappened.ui.presenters;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.domain.models.Comparison;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.Rating;

public interface EventsHistoryContract {
    interface EventsHistoryView {

        void showEvents (List<EventV1> eventV1s);

        void cancelFilters ();

        void showLoading (boolean isLoading);


    }

    interface EventsHistoryPresenter {

        void onViewAttach (EventsHistoryView view);

        void filterEvents (List<UUID> trackingId , Date dateFrom , Date dateTo ,
                           Comparison scaleComparison , Double scale ,
                           Comparison ratingComparison , Rating rating , int startPosition , int endPosition);

        void cancleFilters ();

        String prepareDataForDialog (List<String> strings , List<UUID> uuids , List<Boolean> selectedItem);

        List<UUID> setUuidsCollection ();

        void onViewDettach ();

    }
}
