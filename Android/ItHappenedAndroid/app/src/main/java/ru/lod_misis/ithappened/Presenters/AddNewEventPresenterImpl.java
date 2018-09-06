package ru.lod_misis.ithappened.Presenters;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Statistics.FactCalculator;
import ru.lod_misis.ithappened.Utils.UserDataUtils;

public class AddNewEventPresenterImpl implements AddNewEventContract.AddNewEventPresenter {
    AddNewEventContract.AddNewEventView addNewEventView;
    ITrackingRepository trackingRepository;
    FactCalculator factCalculator;
    TrackingService trackingService;
    public AddNewEventPresenterImpl(SharedPreferences sharedPreferences){
        trackingRepository= UserDataUtils.setUserDataSet(sharedPreferences);
        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), trackingRepository);
        factCalculator=new FactCalculator(trackingRepository);
    }

    @Override
    public void addNewEvent() {
        if(isViewAttached()){
        addNewEventView.addNewEvent();}
    }

    @Override
    public void init(Activity activity) {
        if(isViewAttached()){
            UUID trackingId = UUID.fromString(activity.getIntent().getStringExtra("trackingId"));
            TrackingV1 trackingV1 = trackingRepository.GetTracking(trackingId);
            addNewEventView.startedConfiguration(trackingId,trackingV1);
            addNewEventView.startConfigurationView();

        }
    }

    @Override
    public void attachView(AddNewEventContract.AddNewEventView addNewEventView) {
        this.addNewEventView=addNewEventView;
    }

    @Override
    public void detachView() {
        addNewEventView=null;
    }

    @Override
    public void requestPermission(int codePermission) {
        if (isViewAttached()){
            switch (codePermission){
                case 1:{
                    addNewEventView.requestPermissionForGeoposition();
                    break;}
            }
        }
    }

    @Override
    public void saveEvent(EventV1 eventV1,UUID trackingId) {
        if(isViewAttached()){
        trackingService.AddEvent(trackingId, eventV1);
        new FactCalculator(trackingRepository).calculateFactsForAddNewEventActivity(trackingId);
        addNewEventView.showMessage("Событие добавлено");
        addNewEventView.finishAddEventActivity();}
    }

    @Override
    public Boolean isViewAttached() {
        return addNewEventView!=null;
    }
}
