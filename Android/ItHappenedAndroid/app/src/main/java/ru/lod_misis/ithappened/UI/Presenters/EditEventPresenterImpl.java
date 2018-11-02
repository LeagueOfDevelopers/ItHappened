package ru.lod_misis.ithappened.UI.Presenters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.Domain.TrackingService;
import ru.lod_misis.ithappened.Domain.Models.Rating;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.R;
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
    public void addEventClick() {
        view.addEvent();
    }

    @Override
    public void onViewCreated() {
        TrackingV1 tracking = trackingService.GetTrackingById(trackingId);
        view.onViewCreated(tracking.GetCommentCustomization(),
                tracking.GetRatingCustomization(),
                tracking.GetScaleCustomization(),
                tracking.GetPhotoCustomization(),
                tracking.GetGeopositionCustomization(),
                tracking.GetEvent(eventId).GetEventDate(),
                tracking.GetEvent(eventId).GetComment(),
                tracking.GetEvent(eventId).GetScale(),
                tracking.GetEvent(eventId).GetRating(),
                tracking.GetEvent(eventId).getLongitude(),
                tracking.GetEvent(eventId).getLotitude(),
                tracking.GetEvent(eventId).getPhoto(),
                tracking.GetTrackingName(),
                tracking.getScaleName());
    }

    @Override
    public void setIdentificators(UUID trackingId, UUID eventId) {
        this.trackingId = trackingId;
        this.eventId = eventId;
    }
}
