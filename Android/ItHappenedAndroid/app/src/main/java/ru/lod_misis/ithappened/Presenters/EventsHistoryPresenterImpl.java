package ru.lod_misis.ithappened.Presenters;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.Event;
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
    List<Event> events = new ArrayList<>();

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
                null).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Event>() {
                               @Override
                               public void call(Event event) {
                                   events.add(event);
                                   //hintForEventsHistory.setVisibility(View.GONE);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {

                               }
                           },
                        new Action0() {
                            @Override
                            public void call() {
                                List<Event> visibaleEvents = new ArrayList<>();
                                for (int i=0;i<events.size();i++){
                                    if(!events.get(i).GetStatus()){
                                        visibaleEvents.add(events.get(i));
                                    }
                                }
                                eventsHistoryView.showEvents(visibaleEvents);
                                //eventsAdpt = new EventsAdapter(eventsForAdapter, getActivity(), 1);
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
        events = new ArrayList<>();
        service.FilterEventCollection(trackingId,
                dateFrom,
                dateTo,
                scaleComparison,
                scale,
                ratingComparison,
                rating)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Action1<Event>() {
                    @Override
                    public void call(Event event) {
                        events.add(event);
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
                        eventsHistoryView.showEvents(events);
                    }
                });
    }

    @Override
    public void cancleFilters() {
        events = new ArrayList<>();
        service.FilterEventCollection
                (null,null,null,
                        null,null,null,
                        null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Event>() {
                               @Override
                               public void call(Event event) {
                                   events.add(event);
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


                                eventsHistoryView.showEvents(events);
                                /*eventsAdpt = new EventsAdapter(allEvents, getActivity(), 1);

                                eventsRecycler.setAdapter(eventsAdpt);*/
                            }
                        });
    }
}
