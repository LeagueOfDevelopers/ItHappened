package ru.lod_misis.ithappened.Presenters;

import android.util.Log;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Statistics.FactCalculator;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
    public void addEventClick(int commentState, int ratingState, int scaleState, int geopositionState) {
        boolean commentFlag = true;
        boolean scaleFlag = true;
        boolean ratingFlag = true;
        boolean geopositionFlag = true;

        if (commentState == 2 && view.getComment().isEmpty()) {
            commentFlag = false;
        }

        if (ratingState == 2 && view.getRating().getRating() == 0) {
            ratingFlag = false;
        }

        if (scaleState == 2 && view.getScale() != null) {
            scaleFlag = false;
        }

        if (geopositionState == 2 && (view.getLatitude() == null || view.getLongitude() == null)) {
            geopositionFlag = false;
        }

        String comment = null;
        Double scale = null;
        Rating rating = null;


        if (commentFlag && ratingFlag && scaleFlag && geopositionFlag) {
            if (!view.getComment().isEmpty() && !view.getComment().trim().isEmpty()) {
                comment = view.getComment().trim();
            }
            if (!(view.getRating() == null)) {
                rating = new Rating((int) (view.getRating().getRating()));
            }
            if (view.getScale() != null) {
                try {
                    scale = view.getScale();
                    Date eventDate = null;
                    Locale locale = new Locale("ru");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
                    simpleDateFormat.setTimeZone(TimeZone.getDefault());
                    try {
                        eventDate = simpleDateFormat.parse(view.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    trackingService.EditEvent(trackingId,
                            eventId,
                            scale,
                            rating,
                            comment,
                            view.getLatitude(),
                            view.getLongitude(),
                            view.getPhotoPath(),
                            eventDate);
                    view.reportEvent(R.string.metrica_edit_event);
                    factCalculator.calculateFacts();
                    view.showMessage("Событие изменено");
                    view.showEditResult();
                } catch (Exception e) {
                    view.showMessage("Введите число");
                }
            } else {
                Date eventDate = null;
                Locale locale = new Locale("ru");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                try {
                    eventDate = simpleDateFormat.parse(view.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                trackingService.EditEvent(trackingId,
                        eventId,
                        scale,
                        rating,
                        comment,
                        view.getLatitude(),
                        view.getLongitude(),
                        view.getPhotoPath(),
                        eventDate);
                view.reportEvent(R.string.metrica_edit_event);
                factCalculator.calculateFactsForAddNewEventActivity(trackingId);
                view.showMessage("Событие изменено");
                view.showEditResult();

            }
        } else {
            view.showMessage("Заполните поля с *");
        }
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
                tracking.getScaleName());
    }

    @Override
    public void setIdentificators(UUID trackingId, UUID eventId) {
        this.trackingId = trackingId;
        this.eventId = eventId;
    }
}
