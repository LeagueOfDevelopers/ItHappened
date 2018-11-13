package ru.lod_misis.ithappened.ui.presenters;

import android.app.Activity;
import android.util.Log;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddNewEventPresenterImpl implements AddNewEventContract.AddNewEventPresenter {
    AddNewEventContract.AddNewEventView addNewEventView;
    TrackingDataSource trackingRepository;
    FactService factService;
    TrackingService trackingService;

    private String STATISTICS = "statistics";

    @Inject
    public AddNewEventPresenterImpl(TrackingDataSource trackingRepository,
                                    TrackingService trackingService,
                                    FactService factService) {
        this.trackingRepository = trackingRepository;
        this.trackingService = trackingService;
        this.factService = factService;
    }

    @Override
    public void init (UUID trackingId) {
        if (addNewEventView != null) {
            addNewEventView.startedConfiguration(trackingRepository.getTracking(trackingId));
            addNewEventView.startConfigurationView();
        }
    }

    @Override
    public void attachView (AddNewEventContract.AddNewEventView addNewEventView) {
        this.addNewEventView = addNewEventView;
    }

    @Override
    public void detachView () {
        addNewEventView = null;
    }

    @Override
    public void requestPermission (int codePermission) {
        if (addNewEventView != null) {
            switch ( codePermission ) {
                case 1: {
                    addNewEventView.requestPermissionForGeoposition();
                    break;
                }
            }
        }
    }

    @Override
    public void saveEvent(EventV1 eventV1, UUID trackingId) {
        if (isViewAttached()) {
            trackingService.AddEvent(trackingId, eventV1);

            factService.calculateOneTrackingFacts(trackingService.GetTrackingCollection())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            Log.d(STATISTICS, "calculate");
                        }
                    });

            factService.calculateAllTrackingsFacts(trackingService.GetTrackingCollection())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            Log.d(STATISTICS, "calculate");
                        }
                    });

    public void saveEvent (EventV1 eventV1 , UUID trackingId) {
        if (addNewEventView != null) {
            trackingService.AddEvent(trackingId , eventV1);
            new FactCalculator(trackingRepository).calculateFactsForAddNewEventActivity(trackingId);
            addNewEventView.showMessage("Событие добавлено");
            addNewEventView.finishAddEventActivity();
        }
    }

}
