package ru.lod_misis.ithappened.ui.presenters;

import javax.inject.Inject;

import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.FactCalculator;


public class CreateTrackingPresenterImpl implements CreateTrackingContract.CreateTrackingPresenter {
    private CreateTrackingContract.CreateTrackingView createTrackingView;
    private TrackingDataSource trackingRepository;
    private FactCalculator factCalculator;

    @Inject
    public CreateTrackingPresenterImpl (TrackingDataSource trackingRepository ,
                                        FactCalculator factCalculator) {
        this.trackingRepository = trackingRepository;
        this.factCalculator = factCalculator;
    }

    @Override
    public void attachView (CreateTrackingContract.CreateTrackingView createTrackingView) {
        this.createTrackingView = createTrackingView;
    }

    @Override
    public void detachView () {
        this.createTrackingView = null;
    }

    @Override
    public void saveNewTracking (TrackingV1 newTrackingV1) {
        if ( isViewAttached() ) {
            trackingRepository.createTracking(newTrackingV1);
            factCalculator.calculateFacts();
            createTrackingView.showMessage("Отслеживание добавлено");
            createTrackingView.finishCreatingTracking();
        }
    }

    @Override
    public void requestPermission (int codePermission) {
        switch ( codePermission ) {
            case 1: {
                createTrackingView.requestPermissionForGeoposition();
                break;
            }
        }
    }

    @Override
    public Boolean isViewAttached () {
        return this.createTrackingView != null;
    }

}
