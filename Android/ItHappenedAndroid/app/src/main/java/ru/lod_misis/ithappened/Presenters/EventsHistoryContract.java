package ru.lod_misis.ithappened.Presenters;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.Rating;

public interface EventsHistoryContract {
    interface EventsHistoryView{

        void showEvents(List<NewEvent> newEvents);
        void cancelFilters();

    }

    interface EventsHistoryPresenter{

        void loadEvents();
        void filterEvents(List<UUID> trackingId, Date dateFrom, Date dateTo,
                          Comparison scaleComparison, Double scale,
                          Comparison ratingComparison, Rating rating);
        void cancleFilters();

    }
}
