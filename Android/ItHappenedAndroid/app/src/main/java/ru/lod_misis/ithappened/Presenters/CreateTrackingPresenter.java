package ru.lod_misis.ithappened.Presenters;

import android.content.SharedPreferences;

import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Statistics.FactCalculator;
import ru.lod_misis.ithappened.Utils.UserDataUtils;


public class CreateTrackingPresenter implements CreateTrackingContract.CreateTrackingPresenter {
    CreateTrackingContract.CreateTrackingView createTrackingView;
    ITrackingRepository trackingRepository;
    FactCalculator factCalculator;
    SharedPreferences sharedPreferences;

    public CreateTrackingPresenter(SharedPreferences sharedPreferences){
        this.sharedPreferences=sharedPreferences;
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
            trackingRepository= UserDataUtils.setUserDataSet(sharedPreferences);
            factCalculator=new FactCalculator(trackingRepository);
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
