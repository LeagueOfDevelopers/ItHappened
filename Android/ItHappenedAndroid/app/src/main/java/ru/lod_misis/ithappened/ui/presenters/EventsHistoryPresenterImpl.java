package ru.lod_misis.ithappened.ui.presenters;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.models.Comparison;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.Rating;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EventsHistoryPresenterImpl implements EventsHistoryContract.EventsHistoryPresenter {

    private TrackingService service;
    private EventsHistoryContract.EventsHistoryView eventsHistoryView;
    private List<EventV1> eventV1s = new ArrayList<>();

    @Inject
    public EventsHistoryPresenterImpl (TrackingService service) {
        this.service = service;
    }

    @Override
    public void filterEvents (List<UUID> trackingId ,
                              Date dateFrom ,
                              Date dateTo ,
                              Comparison scaleComparison ,
                              Double scale ,
                              Comparison ratingComparison ,
                              Rating rating , int startPosition , int endPosition) {
        eventsHistoryView.showLoading(true);
        eventV1s = new ArrayList<>();
        service.FilterEventCollection(trackingId ,
                dateFrom ,
                dateTo ,
                scaleComparison ,
                scale ,
                ratingComparison ,
                rating , startPosition , endPosition)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Action1<EventV1>() {
                    @Override
                    public void call (EventV1 eventV1) {
                        eventV1s.add(eventV1);
                    }
                } , new Action1<Throwable>() {
                    @Override
                    public void call (Throwable throwable) {
                        eventsHistoryView.showLoading(false);
                    }
                } ,
                new Action0() {
                    @Override
                    public void call () {
                        eventsHistoryView.showLoading(false);
                        eventsHistoryView.showEvents(eventV1s);
                    }
                });
    }

    @Override
    public void onViewAttach (EventsHistoryContract.EventsHistoryView view) {
        this.eventsHistoryView = view;
    }

    @Override
    public void onViewDettach () {
        eventsHistoryView = null;
    }

    @Override
    public void cancleFilters () {
        eventV1s = new ArrayList<>();
        eventsHistoryView.showLoading(true);
        service.FilterEventCollection
                (null , null , null ,
                        null , null , null ,
                        null , 0 , 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventV1>() {
                               @Override
                               public void call (EventV1 eventV1) {
                                   eventV1s.add(eventV1);
                               }
                           } ,
                        new Action1<Throwable>() {
                            @Override
                            public void call (Throwable throwable) {
                                eventsHistoryView.showLoading(false);
                            }
                        } ,
                        new Action0() {
                            @Override
                            public void call () {
                                eventsHistoryView.showEvents(eventV1s);
                                eventsHistoryView.showLoading(false);
                                /*eventsAdpt = new EventsAdapter(allEvents, getActivity(), 1);
                                eventsRecycler.setAdapter(eventsAdpt);*/
                            }
                        });
    }

    @Override
    public String prepareDataForDialog (List<String> strings , List<UUID> idCollection , List<Boolean> selectedItems) {
        List<TrackingV1> trackings = service.GetTrackingCollection();
        String allText = "";

        for (int i = 0; i < trackings.size(); i++) {
            if (!trackings.get(i).setDeleted()) {
                strings.add(trackings.get(i).getTrackingName());
                idCollection.add(trackings.get(i).getTrackingId());
                selectedItems.add(true);
            }
        }

        for (int i = 0; i < strings.size(); i++) {
            strings.size();
            allText += strings.get(i) + ", ";
        }

        return allText;
    }

    @Override
    @Nullable
    public List<UUID> setUuidsCollection () {
        List<UUID> uuids=new ArrayList<>();
        for (int i = 0; i < service.GetTrackingCollection().size(); i++) {
            if (!service.GetTrackingCollection().get(i).setDeleted()) {
                uuids.add(service.GetTrackingCollection().get(i).getTrackingId());
            }
        }
        return uuids;
    }
}
