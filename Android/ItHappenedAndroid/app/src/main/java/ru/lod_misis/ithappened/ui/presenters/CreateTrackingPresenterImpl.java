package ru.lod_misis.ithappened.ui.presenters;

import android.util.Log;

import javax.inject.Inject;

import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class CreateTrackingPresenterImpl implements CreateTrackingContract.CreateTrackingPresenter {
    private CreateTrackingContract.CreateTrackingView createTrackingView;
    private TrackingService trackingService;
    private FactService factService;

    private String STATISTICS = "statistics";

    @Inject
    public CreateTrackingPresenterImpl (TrackingService trackingService ,
                                        FactService factService) {
        this.trackingService = trackingService;
        this.factService = factService;
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
        if (createTrackingView != null) {
            trackingService.AddTracking(newTrackingV1);

            factService.calculateOneTrackingFacts(trackingService.GetTrackingCollection())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call (Object o) {
                            Log.d(STATISTICS , "calculate");
                        }
                    });

            factService.calculateAllTrackingsFacts(trackingService.GetTrackingCollection())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call (Object o) {
                            Log.d(STATISTICS , "calculate");
                        }
                    });

            createTrackingView.showMessage("Отслеживание добавлено");
            createTrackingView.finishCreatingTracking();
        }
    }

    @Override
    public void requestPermission (int codePermission) {
        if (createTrackingView != null)
            switch ( codePermission ) {
                case 1: {
                    createTrackingView.requestPermissionForGeoposition();
                    break;
                }
            }
    }

}
