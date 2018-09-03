package ru.lod_misis.ithappened.Presenters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

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
    ITrackingRepository collection;
    TrackingService trackingSercvice;
    public EventDetailsPresenterImpl(SharedPreferences sharedPreferences, Intent intent){
        factRepository = StaticFactRepository.getInstance();
        collection= UserDataUtils.setUserDataSet(sharedPreferences);
        factRepository = StaticFactRepository.getInstance();
        collection= UserDataUtils.setUserDataSet(sharedPreferences);
        trackingSercvice = new TrackingService(sharedPreferences.getString("UserId", ""), collection);
        trackingId = UUID.fromString(intent.getStringExtra("trackingId"));
        eventId = UUID.fromString(intent.getStringExtra("eventId"));
    }
    @Override
    public void init() {
        if(isViewAttached()){
            eventDetailsView.startedConfiguration(collection,trackingId,eventId);
            eventDetailsView.startConfigurationView();
        }

    }

    @Override
    public void attachView(EventDetailsContract.EventDetailsView eventDetailsView) {
       this.eventDetailsView=eventDetailsView;
    }

    @Override
    public void detachView() {
        eventDetailsView=null;
    }

    @Override
    public void deleteEvent() {
        if(isViewAttached()){
            eventDetailsView.deleteEvent();
        }
    }

    @Override
    public void okClicked() {
        trackingSercvice.RemoveEvent(eventId);
        factRepository.onChangeCalculateOneTrackingFacts(collection.GetTrackingCollection(), trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculateOneTrackingFact");
                    }
                });
        factRepository.calculateAllTrackingsFacts(collection.GetTrackingCollection())
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
        if(isViewAttached()){
            eventDetailsView.editEvent();
        }
    }

    @Override
    public void beforeFinish() {
        if(isViewAttached()){
            eventDetailsView.finishDetailsEventActivity();
        }
    }

    @Override
    public Boolean isViewAttached() {
        return eventDetailsView!=null;
    }
}
