package ru.lod_misis.ithappened.ui.presenters;

import android.util.Log;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DeletePresenterImpl implements DeleteContract.DeletePresenter {
    private DeleteContract.DeleteView eventDetailsView;
    private FactService factService;
    private TrackingService trackingSercvice;

    private String STATISTICS = "statistics";

    @Inject
    public DeletePresenterImpl(TrackingService trackingSercvice, FactService factService) {
        this.trackingSercvice = trackingSercvice;
        this.factService = factService;
    }

    @Override
    public void attachView(DeleteContract.DeleteView eventDetailsView) {
        this.eventDetailsView = eventDetailsView;
    }

    @Override
    public void detachView() {
        eventDetailsView = null;
    }

    @Override
    public void okClicked(UUID trackingId, UUID eventId) {
        trackingSercvice.RemoveEvent(eventId);
        factService.onChangeCalculateOneTrackingFacts(trackingSercvice.GetTrackingCollection(), trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d(STATISTICS, "calculateOneTrackingFact");
                    }
                });
        factService.calculateAllTrackingsFacts(trackingSercvice.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d(STATISTICS, "calculateOneTrackingFact");
                    }
                });
        eventDetailsView.showMessage("Событие удалено");
        eventDetailsView.finishDetailsEventActivity();
    }

    @Override
    public void canselClicked() {

    }

    @Override
    public Boolean isViewAttached() {
        return eventDetailsView != null;
    }
}
