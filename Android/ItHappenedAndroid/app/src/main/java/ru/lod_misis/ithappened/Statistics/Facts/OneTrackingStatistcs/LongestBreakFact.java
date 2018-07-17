package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.BreakData;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;

public class LongestBreakFact extends Fact {

    private String TrackingName;
    private List<EventV1> Events;
    private BreakData LongestBreak;

    public LongestBreakFact(TrackingV1 tracking) {
        TrackingName = tracking.getTrackingName();
        trackingId = tracking.GetTrackingID();
        List<EventV1> events = new ArrayList<>();
        for (EventV1 e: tracking.getEventV1Collection()) {
            if (!e.isDeleted()) events.add(e);
        }
        Events = events;
    }

    public BreakData getLongestBreak() {
        return LongestBreak;
    }

    @Override
    public void calculateData() {
        LongestBreak = FindLongestBreak();
        calculatePriority();
    }

    @Override
    protected void calculatePriority() {
        if (LongestBreak != null)
            priority = Math.sqrt(LongestBreak.getDuration().toStandardDays().getDays());
        else
            priority = 0.0;
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildLongestBreakDEscription(TrackingName,
                LongestBreak.getFirstEventDate(), LongestBreak.getSecondEventDate());
    }

    private BreakData FindLongestBreak () {
        List<EventV1> copy = SortEventsByDate(Events);
        long maxInterval = 0;
        BreakData data = null;
        for (int i = 1; i < copy.size() - 1; i++) {
            long delta = copy.get(i + 1).getEventDate().getTime() - copy.get(i).getEventDate().getTime();
            if (delta > maxInterval) {
                BreakData point = new BreakData(
                        copy.get(i).GetEventDate(),
                        copy.get(i + 1).GetEventDate(),
                        copy.get(i).GetEventId(),
                        copy.get(i + 1).GetEventId());
                if (IsBreakSignificant(point)) {
                    data = point;
                    maxInterval = delta;
                }
            }
        }
        return data;
    }

    private boolean IsBreakSignificant(BreakData dataToCheck) {
        List<EventV1> copy = SortEventsByDate(Events);
        Duration tripleAverange = Duration.ZERO;
        boolean IsSignificant = true;
        for (int i = 0; i < copy.size() - 1; i++) {
            tripleAverange = tripleAverange.plus((copy.get(i + 1).GetEventDate().getTime() -
                    copy.get(i).GetEventDate().getTime()) * 3 / (copy.size() - 1));
        }
        if (tripleAverange.isLongerThan(dataToCheck.getDuration())) IsSignificant = false;
        if (new DateTime().minusDays(7).isBefore(dataToCheck.getSecondEventDate().getTime())) {
            IsSignificant = false;
        }
        return IsSignificant;
    }
    // Значимо, если перерыв в 3 раза больше, чем среденее время между
    // событиями, количество эвентов в отслеживании больше 10 и первое
    // событие после перерыва было более недели назад (размер коллекции
    // проверяется в функции применимости, а здесь проверяется все остальное,
    // чтобы в случае, если размер коллекции меньше 10, не пришлось считать)

    private List<EventV1> SortEventsByDate(List<EventV1> events) {
        List<EventV1> copy = new ArrayList<>(events);
        Collections.sort(copy, new Comparator<EventV1>() {
            @Override
            public int compare(EventV1 event, EventV1 t1) {
                return event.GetEventDate().compareTo(t1.GetEventDate());
            }
        });
        return copy;
    }
}
