package ru.lod_misis.ithappened.Presenters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EventDetailsPresenterImpl implements EventDetailsContract.EventDetailsPresenter {
    EventDetailsContract.EventDetailsView eventDetailsView;
    InMemoryFactRepository factRepository;
    UUID trackingId;
    UUID eventId;
    TrackingService trackingSercvice;

    @Inject
    public EventDetailsPresenterImpl(TrackingService trackingSercvice,InMemoryFactRepository factRepository) {
        this.trackingSercvice = trackingSercvice;
        this.factRepository = factRepository;
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
        factRepository.onChangeCalculateOneTrackingFacts(trackingSercvice.GetTrackingCollection(), trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculateOneTrackingFact");
                    }
                });
        factRepository.calculateAllTrackingsFacts(trackingSercvice.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculateOneTrackingFact");
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
