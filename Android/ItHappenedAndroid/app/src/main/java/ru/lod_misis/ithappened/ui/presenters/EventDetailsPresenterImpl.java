package ru.lod_misis.ithappened.ui.presenters;

import android.util.Log;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EventDetailsPresenterImpl implements EventDetailsContract.EventDetailsPresenter {
    EventDetailsContract.EventDetailsView eventDetailsView;
    FactService factService;
    UUID trackingId;
    UUID eventId;
    TrackingService trackingSercvice;

    @Inject
    public EventDetailsPresenterImpl(TrackingService trackingSercvice, FactService factService) {
        this.trackingSercvice = trackingSercvice;
        this.factService = factService;
    }

    @Override
    public void init() {
        if (isViewAttached()) {
            eventDetailsView.startedConfiguration(trackingSercvice, trackingId, eventId);
            eventDetailsView.startConfigurationView();
        }

    }

    @Override
    public void attachView(EventDetailsContract.EventDetailsView eventDetailsView, UUID trackingId, UUID eventId) {
        this.eventDetailsView = eventDetailsView;
        this.trackingId = trackingId;
        this.eventId = eventId;
    }

    @Override
    public void detachView() {
        eventDetailsView = null;
    }

    @Override
    public void deleteEvent() {
        if (isViewAttached()) {
            eventDetailsView.deleteEvent();
        }
    }

    @Override
    public void okClicked() {
        trackingSercvice.RemoveEvent(eventId);
        factService.onChangeCalculateOneTrackingFacts(trackingSercvice.GetTrackingCollection(), trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("statistics", "calculateOneTrackingFact");
                    }
                });
        factService.calculateAllTrackingsFacts(trackingSercvice.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("statistics", "calculateOneTrackingFact");
                    }
                });
        eventDetailsView.showMessage("Событие удалено");
        eventDetailsView.finishDetailsEventActivity();
    }

    @Override
    public void canselClicked() {

    }

    @Override
    public void editEvent() {
        if (isViewAttached()) {
            eventDetailsView.editEvent();
        }
    }

    @Override
    public void beforeFinish() {
        if (isViewAttached()) {
            eventDetailsView.finishDetailsEventActivity();
        }
    }

    @Override
    public Boolean isViewAttached() {
        return eventDetailsView != null;
    }
}
