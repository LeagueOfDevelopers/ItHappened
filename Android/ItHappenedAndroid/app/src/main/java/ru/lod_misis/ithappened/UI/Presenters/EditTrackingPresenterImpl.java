package ru.lod_misis.ithappened.UI.Presenters;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.Domain.TrackingService;
import ru.lod_misis.ithappened.Domain.Models.TrackingCustomization;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;

public class EditTrackingPresenterImpl implements EditTrackingContract.EditTrackingPresenter {

    TrackingService service;
    EditTrackingContract.EditTrackingView editTrackingView;
    UUID trackingId;

    @Inject
    public EditTrackingPresenterImpl(TrackingService service) {
        this.service = service;
    }

    @Override
    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }

    @Override
    public void onViewAttached(EditTrackingContract.EditTrackingView editTrackingView) {
        this.editTrackingView = editTrackingView;
    }

    @Override
    public void onViewDettached() {
        editTrackingView = null;
    }

    @Override
    public void onEditClick() {

        String trackingName = editTrackingView.getEditTrackingName();


        if (trackingName.isEmpty() || trackingName.trim().isEmpty()) {
            editTrackingView.showError("Введите название отслеживания");
        } else {

            String trackingColor = editTrackingView.getTrackingColor();
            String scaleType = editTrackingView.getScaleName();
            TrackingCustomization scale = editTrackingView.getScaleCustomization();
            TrackingCustomization rating = editTrackingView.getRatingCustomization();
            TrackingCustomization comment = editTrackingView.getCommentCustomization();
            TrackingCustomization geoposition = editTrackingView.getGeopositionCustomization();
            TrackingCustomization photo = editTrackingView.getPhotoCustomization();


            if ((scale == TrackingCustomization.Optional || scale == TrackingCustomization.Required) &&
                    (scaleType.isEmpty()
                            || scaleType.trim().isEmpty())) {
                editTrackingView.showError("Введите единицу измерения шкалы");
            } else {
                if (scale != TrackingCustomization.None) {
                }
                service.EditTracking(trackingId, scale, rating, comment, geoposition, photo, trackingName, scaleType, trackingColor);
                editTrackingView.completeEdit();
            }
        }
    }

    @Override
    public TrackingV1 getTrackingState() {
        return service.GetTrackingById(trackingId);
    }
}
