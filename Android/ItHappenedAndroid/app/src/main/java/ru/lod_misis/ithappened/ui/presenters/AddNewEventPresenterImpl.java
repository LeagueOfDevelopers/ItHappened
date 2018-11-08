package ru.lod_misis.ithappened.ui.presenters;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.statistics.FactCalculator;

public class AddNewEventPresenterImpl implements AddNewEventContract.AddNewEventPresenter {

    private AddNewEventContract.AddNewEventView addNewEventView;
    private TrackingDataSource trackingRepository;
    private TrackingService trackingService;

    @Inject
    public AddNewEventPresenterImpl (TrackingDataSource trackingRepository ,
                                     TrackingService trackingService) {
        this.trackingRepository = trackingRepository;
        this.trackingService = trackingService;
    }

    @Override
    public void addNewEvent () {
        if ( isViewAttached() ) {
            addNewEventView.addNewEvent();
        }
    }

    @Override
    public void init (UUID trackingId) {
        if ( isViewAttached() ) {
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
        if ( isViewAttached() ) {
            switch ( codePermission ) {
                case 1: {
                    addNewEventView.requestPermissionForGeoposition();
                    break;
                }
            }
        }
    }

    @Override
    public void saveEvent (EventV1 eventV1 , UUID trackingId) {
        if ( isViewAttached() ) {
            trackingService.AddEvent(trackingId , eventV1);
            new FactCalculator(trackingRepository).calculateFactsForAddNewEventActivity(trackingId);
            addNewEventView.showMessage("Событие добавлено");
            addNewEventView.finishAddEventActivity();
        }
    }

    private Boolean isViewAttached () {
        return addNewEventView != null;
    }
}
