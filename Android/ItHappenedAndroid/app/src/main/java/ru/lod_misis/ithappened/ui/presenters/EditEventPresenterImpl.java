package ru.lod_misis.ithappened.ui.presenters;

import android.util.Log;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.domain.FactService;
import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.domain.models.Rating;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EditEventPresenterImpl implements EditEventContract.EditEventPresenter {

    private EditEventContract.EditEventView view;
    private TrackingService trackingService;
    private FactService factService;
    private UUID trackingId;
    private UUID eventId;

    private String STATISTICS = "statistics";

    @Inject
    public EditEventPresenterImpl(TrackingService trackingService, FactService factService) {
        this.trackingService = trackingService;
        this.factService = factService;
    }

    @Override
    public void onViewAttached(EditEventContract.EditEventView view) {
        this.view = view;
    }

    @Override
    public void onViewDettached() {
        view = null;
    }

    @Override
    public void finish (Double scale,Rating rating,String comment,
                        Double latitude,Double longitude,String photoPath,Date eventDate) {
        trackingService.EditEvent(
                eventId,
                scale,
                rating,
                comment,
                latitude,
                longitude,
                photoPath,
                eventDate);
        factService.calculateOneTrackingFacts(trackingService.GetTrackingCollection())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1() {
            @Override
            public void call(Object o) {
                Log.d(STATISTICS, "calculate");
            }
        });

        factService.calculateAllTrackingsFacts(trackingService.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        Log.d(STATISTICS, "calculate");
                    }
                });
    }

    @Override
    public void addEventClick() {
        view.addEvent();
    }

    @Override
    public void onViewCreated() {
        TrackingV1 tracking = trackingService.GetTrackingById(trackingId);
        view.onViewCreated(tracking.getCommentCustomization(),
                tracking.getRatingCustomization(),
                tracking.getScaleCustomization(),
                tracking.getPhotoCustomization(),
                tracking.getGeopositionCustomization(),
                tracking.getEvent(eventId).getEventDate(),
                tracking.getEvent(eventId).getComment(),
                tracking.getEvent(eventId).getScale(),
                tracking.getEvent(eventId).getRating(),
                tracking.getEvent(eventId).getLongitude(),
                tracking.getEvent(eventId).getLotitude(),
                tracking.getEvent(eventId).getPhoto(),
                tracking.getTrackingName(),
                tracking.getScaleName());
    }

    @Override
    public void setIdentificators(UUID trackingId, UUID eventId) {
        this.trackingId = trackingId;
        this.eventId = eventId;
    }
}
