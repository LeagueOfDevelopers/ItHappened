package ru.lod_misis.ithappened.ui.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.ui.activities.UserActionsActivity;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.R;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TrackingsPresenterImpl implements TrackingsContract.TrackingsPresenter {

    private TrackingService service;
    private Context context;
    private TrackingsContract.TrackingsView trackingView;
    private FactService factService;

    public TrackingsPresenterImpl(TrackingService service,
                                  Context context,
                                  FactService factService) {
        this.service = service;
        this.context = context;
        this.factService = factService;
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
            if (!allTrackingV1s.get(i).setDeleted()) {
                visibleTrackingV1s.add(allTrackingV1s.get(i));
            }
        }

        trackingView.showTrackings(visibleTrackingV1s);
    }

    @Override
    public void deleteTracking(UUID trackingId) {
        service.RemoveTracking(trackingId);

        factService.calculateOneTrackingFacts(service.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        Log.d("statistics", "calculate");
                    }
                });

        factService.calculateAllTrackingsFacts(service.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        Log.d("statistics", "calculate");
                    }
                });

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
