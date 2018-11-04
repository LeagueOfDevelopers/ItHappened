package ru.lod_misis.ithappened.UI.Presenters;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.Domain.TrackingService;
import ru.lod_misis.ithappened.Domain.Models.Rating;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Domain.Statistics.FactCalculator;

public class EditEventPresenterImpl implements EditEventContract.EditEventPresenter {


    EditEventContract.EditEventView view;
    TrackingService trackingService;
    FactCalculator factCalculator;
    private UUID trackingId;
    private UUID eventId;

    @Inject
    public EditEventPresenterImpl(TrackingService trackingService, FactCalculator factCalculator) {
        this.trackingService = trackingService;
        this.factCalculator = factCalculator;
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
        trackingService.EditEvent(trackingId,
                eventId,
                scale,
                rating,
                comment,
                latitude,
                longitude,
                photoPath,
                eventDate);
        factCalculator.calculateFacts();
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
