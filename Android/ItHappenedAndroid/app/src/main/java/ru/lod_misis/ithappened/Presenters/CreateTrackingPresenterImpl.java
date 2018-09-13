package ru.lod_misis.ithappened.Presenters;

import android.content.SharedPreferences;

import javax.inject.Inject;

import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Statistics.FactCalculator;


public class CreateTrackingPresenterImpl implements CreateTrackingContract.CreateTrackingPresenter {
    CreateTrackingContract.CreateTrackingView createTrackingView;
    ITrackingRepository trackingRepository;
    FactCalculator factCalculator;
    SharedPreferences sharedPreferences;

    @Inject
    public CreateTrackingPresenterImpl(SharedPreferences sharedPreferences,
                                       ITrackingRepository trackingRepository,
                                       FactCalculator factCalculator){
        this.sharedPreferences=sharedPreferences;
        this.trackingRepository = trackingRepository;
        this.factCalculator = factCalculator;
    }
    @Override
    public void init() {
        if(isViewAttached()){
        createTrackingView.startConfigurationView();
        createTrackingView.satredConfiguration();}
    }

    @Override
    public void attachView(CreateTrackingContract.CreateTrackingView createTrackingView) {
        this.createTrackingView=createTrackingView;
    }

    @Override
    public void detachView() {
        this.createTrackingView=null;
    }

    @Override
    public void createNewTracking() {
        if(isViewAttached()){
            createTrackingView.createTracking();
        }
    }

    @Override
    public void saveNewTracking(TrackingV1 newTrackingV1) {
        if(isViewAttached()){
            trackingRepository.AddNewTracking(newTrackingV1);
            factCalculator.calculateFacts();
            createTrackingView.showMessage("Отслеживание добавлено");
            createTrackingView.finishCreatingTracking();
        }
    }

    @Override
    public void requestPermission(int codePermission) {
        switch (codePermission){
            case 1:{createTrackingView.requestPermissionForGeoposition();
                break;}
        }
    }

    @Override
    public Boolean isViewAttached() {
        return this.createTrackingView!=null;
    }

}
