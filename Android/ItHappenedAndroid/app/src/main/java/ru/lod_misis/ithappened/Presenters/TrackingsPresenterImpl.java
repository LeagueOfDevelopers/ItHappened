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
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Statistics.FactCalculator;

public class TrackingsPresenterImpl implements TrackingsContract.TrackingsPresenter {

    private TrackingService service;
    private Context context;
    private TrackingsContract.TrackingsView trackingView;
    private FactCalculator factCalculator;

    public TrackingsPresenterImpl(TrackingService service,
                                  Context context,
                                  FactCalculator factCalculator) {
        this.service = service;
        this.context = context;
        this.factCalculator = factCalculator;
    }

    @Override
    public void onViewAttach(TrackingsContract.TrackingsView view) {
        trackingView = view;
    }

    @Override
    public void onViewDettach() {
        trackingView = null;
    }

    @Override
    public void loadTrackings() {
        List<TrackingV1> allTrackingV1s = service.GetTrackingCollection();
        List<TrackingV1> visibleTrackingV1s = new ArrayList<>();

        for (int i = 0; i < allTrackingV1s.size(); i++) {
            if (!allTrackingV1s.get(i).GetStatus()) {
                visibleTrackingV1s.add(allTrackingV1s.get(i));
            }
        }

        trackingView.showTrackings(visibleTrackingV1s);
    }

    @Override
    public void deleteTracking(UUID trackingId) {
        service.RemoveTracking(trackingId);
        factCalculator.calculateFacts();
        trackingView.showMessage("Отслеживание удалено");
        YandexMetrica.reportEvent(context.getString(R.string.metrica_delete_tracking));
        Intent intent = new Intent(context, UserActionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public void cancelDeleting() {

    }
}
