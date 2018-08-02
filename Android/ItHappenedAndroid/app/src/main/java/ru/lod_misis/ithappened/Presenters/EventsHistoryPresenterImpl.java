package ru.lod_misis.ithappened.Presenters;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EventsHistoryPresenterImpl implements EventsHistoryContract.EventsHistoryPresenter {

    ITrackingRepository repository;
    TrackingService service;
    Context context;
    EventsHistoryContract.EventsHistoryView eventsHistoryView;
    List<EventV1> eventV1s = new ArrayList<>();

    public EventsHistoryPresenterImpl(ITrackingRepository repository,
                                      TrackingService service,
                                      Context context,
                                      EventsHistoryContract.EventsHistoryView eventsHistoryView) {
        this.repository = repository;
        this.service = service;
        this.context = context;
        this.eventsHistoryView = eventsHistoryView;
    }

    @Override
    public void filterEvents(List<UUID> trackingId,
                             Date dateFrom,
                             Date dateTo,
                             Comparison scaleComparison,
                             Double scale,
                             Comparison ratingComparison,
                             Rating rating, int startPosition, int endPosition) {
        eventsHistoryView.showLoading(true);
        eventV1s = new ArrayList<>();
        service.FilterEventCollection(trackingId,
                dateFrom,
                dateTo,
                scaleComparison,
                scale,
                ratingComparison,
                rating, startPosition, endPosition)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Action1<EventV1>() {
                    @Override
                    public void call(EventV1 eventV1) {
                        eventV1s.add(eventV1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        eventsHistoryView.showLoading(false);
                    }
                },
                new Action0() {
                    @Override
                    public void call() {
                        /*eventsAdpt = new EventsAdapter(filteredEvents, getActivity(), 1);
                        eventsRecycler.setAdapter(eventsAdpt);

                        if (filteredEvents.size() == 0) {
                            filtersHintText.setVisibility(View.VISIBLE);
                        } else {
                            filtersHintText.setVisibility(View.GONE);
                        }
                        */
                        eventsHistoryView.showLoading(false);
                        eventsHistoryView.showEvents(eventV1s);
                    }
                });
    }

    @Override
    public void cancleFilters() {
        eventV1s = new ArrayList<>();
        eventsHistoryView.showLoading(true);
        service.FilterEventCollection
                (null, null, null,
                        null, null, null,
                        null, 0, 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventV1>() {
                               @Override
                               public void call(EventV1 eventV1) {
                                   eventV1s.add(eventV1);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                eventsHistoryView.showLoading(false);
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {


                                eventsHistoryView.showEvents(eventV1s);
                                eventsHistoryView.showLoading(false);
                                /*eventsAdpt = new EventsAdapter(allEvents, getActivity(), 1);

                                eventsRecycler.setAdapter(eventsAdpt);*/
                            }
                        });
    }

    @Override
    public String prepareDataForDialog(List<TrackingV1> trackings, List<String> strings, List<UUID> idCollection, List<Boolean> selectedItems) {

        String allText = "";

        for (int i = 0; i < trackings.size(); i++) {
            if (!trackings.get(i).GetStatus()) {
                strings.add(trackings.get(i).GetTrackingName());
                idCollection.add(trackings.get(i).GetTrackingID());
                selectedItems.add(true);
            }
        }

        for (int i = 0; i < strings.size(); i++) {
            if (i != strings.size()) {
                allText += strings.get(i) + ", ";
            }
        }

        return allText;
    }
}
