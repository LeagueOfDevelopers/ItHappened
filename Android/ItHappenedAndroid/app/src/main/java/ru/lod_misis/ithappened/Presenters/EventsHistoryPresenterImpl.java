package ru.lod_misis.ithappened.Presenters;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Rating;
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
    public void loadEvents() {



        service.FilterEventCollection(null,
                null,
                null,
                null,
                null,
                null,
                null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventV1>() {
                               @Override
                               public void call(EventV1 eventV1) {
                                   eventV1s.add(eventV1);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   Log.e("History", "History is failure");
                               }
                           },
                        new Action0() {
                            @Override
                            public void call() {
                                List<EventV1> visibaleEventV1s = new ArrayList<>();
                                for (int i = 0; i< eventV1s.size(); i++){
                                    if(!eventV1s.get(i).GetStatus()){
                                        visibaleEventV1s.add(eventV1s.get(i));
                                    }
                                }
                                eventsHistoryView.showEvents(visibaleEventV1s);
                            }
                        });
    }

    @Override
    public void filterEvents(List<UUID> trackingId,
                             Date dateFrom,
                             Date dateTo,
                             Comparison scaleComparison,
                             Double scale,
                             Comparison ratingComparison,
                             Rating rating) {
        eventV1s = new ArrayList<>();
        service.FilterEventCollection(trackingId,
                dateFrom,
                dateTo,
                scaleComparison,
                scale,
                ratingComparison,
                rating)
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
                        eventsHistoryView.showEvents(eventV1s);
                    }
                });
    }

    @Override
    public void cancleFilters() {
        eventV1s = new ArrayList<>();
        service.FilterEventCollection
                (null,null,null,
                        null,null,null,
                        null)
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

                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {


                                eventsHistoryView.showEvents(eventV1s);
                                /*eventsAdpt = new EventsAdapter(allEvents, getActivity(), 1);

                                eventsRecycler.setAdapter(eventsAdpt);*/
                            }
                        });
    }
}
