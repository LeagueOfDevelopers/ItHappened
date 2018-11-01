package ru.lod_misis.ithappened.UI.Presenters;

import android.app.Activity;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.Domain.TrackingService;
import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Data.Repositories.ITrackingRepository;
import ru.lod_misis.ithappened.Domain.Statistics.FactCalculator;

public class AddNewEventPresenterImpl implements AddNewEventContract.AddNewEventPresenter {
    AddNewEventContract.AddNewEventView addNewEventView;
    ITrackingRepository trackingRepository;
    FactCalculator factCalculator;
    TrackingService trackingService;

    @Inject
    public AddNewEventPresenterImpl(ITrackingRepository trackingRepository,
                                    TrackingService trackingService,
                                    FactCalculator factCalculator) {
        this.trackingRepository = trackingRepository;
        this.trackingService = trackingService;
        this.factCalculator = factCalculator;
    }

    @Override
    public void addNewEvent() {
        if(isViewAttached()){
        addNewEventView.addNewEvent();}
    }

    @Override
    public void init(Activity activity) {
        if (isViewAttached()) {
            UUID trackingId = UUID.fromString(activity.getIntent().getStringExtra("trackingId"));
            TrackingV1 trackingV1 = trackingRepository.GetTracking(trackingId);
            addNewEventView.startedConfiguration(trackingId, trackingV1);
            addNewEventView.startConfigurationView();

        }
    }

    @Override
    public void attachView(AddNewEventContract.AddNewEventView addNewEventView) {
        this.addNewEventView = addNewEventView;
    }

    @Override
    public void detachView() {
        addNewEventView = null;
    }

    @Override
    public void requestPermission(int codePermission) {
        if (isViewAttached()) {
            switch (codePermission) {
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
            new FactCalculator(trackingRepository).calculateFactsForAddNewEventActivity(trackingId);
            addNewEventView.showMessage("Событие добавлено");
            addNewEventView.finishAddEventActivity();
        }
    }

    @Override
    public Boolean isViewAttached() {
        return addNewEventView != null;
    }
}
