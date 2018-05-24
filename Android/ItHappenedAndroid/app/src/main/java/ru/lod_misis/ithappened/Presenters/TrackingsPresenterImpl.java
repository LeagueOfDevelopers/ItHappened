package ru.lod_misis.ithappened.Presenters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Statistics.FactCalculator;

public class TrackingsPresenterImpl implements TrackingsContract.TrackingsPresenter {

    private TrackingService service;
    private Context context;
    private TrackingsContract.TrackingsView trackingView;
    private FactCalculator factCalculator;
    private ITrackingRepository repository;
    private SharedPreferences sharedPreferences;

    public TrackingsPresenterImpl(TrackingService service,
                                  Context context,
                                  TrackingsContract.TrackingsView trackingView,
                                  ITrackingRepository repository,
                                  SharedPreferences sharedPreferences) {
        this.service = service;
        this.context = context;
        this.trackingView = trackingView;
        this.repository = repository;
        this.sharedPreferences = sharedPreferences;
        factCalculator = new FactCalculator(repository);
    }

    @Override
    public void loadTrackings() {
        List<Tracking> allTrackings = service.GetTrackingCollection();
        List<Tracking> visibleTrackings = new ArrayList<>();

        for(int i =0;i<allTrackings.size();i++){
            if(!allTrackings.get(i).GetStatus()){
                visibleTrackings.add(allTrackings.get(i));
            }
        }

        trackingView.showTrackings(visibleTrackings);
    }

    @Override
    public void deleteTracking(UUID trackingId) {
        service.RemoveTracking(trackingId);
        factCalculator.calculateFacts();
        trackingView.showMessage("Отслеживание удалено");
        YandexMetrica.reportEvent("Пользователь удалил отслеживание");
        Intent intent = new Intent(context, UserActionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public void cancelDeleting() {

    }
}
