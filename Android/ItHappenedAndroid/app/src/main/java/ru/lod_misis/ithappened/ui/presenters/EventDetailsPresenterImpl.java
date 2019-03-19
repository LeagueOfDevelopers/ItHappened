package ru.lod_misis.ithappened.ui.presenters;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.domain.TrackingService;

public class EventDetailsPresenterImpl implements EventDetailsContract.EventDetailsPresenter {

    private EventDetailsContract.EventDetailsView view;
    private TrackingService trackingService;

    @Inject
    public EventDetailsPresenterImpl(TrackingService trackingSercvice) {
        this.trackingService = trackingSercvice;
    }

    @Override
    public void init(UUID trackingId) {
        if (isViewAttached()) {
            view.startedConfiguration(trackingService.GetTrackingById(trackingId));
            view.startConfigurationView();
        }

    }

    @Override
    public void attachView(EventDetailsContract.EventDetailsView eventDetailsView) {
        this.view = eventDetailsView;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public Boolean isViewAttached() {
        return view != null;
    }
}
