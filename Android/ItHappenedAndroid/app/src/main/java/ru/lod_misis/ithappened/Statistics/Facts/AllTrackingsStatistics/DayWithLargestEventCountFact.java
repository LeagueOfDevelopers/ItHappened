package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;
import ru.lod_misis.ithappened.Statistics.Facts.Models.TimeSpanEventData;

public class DayWithLargestEventCountFact extends Fact {

    private List<EventV1> Events;
    private TimeSpanEventData LargestEventCountDay;

    public DayWithLargestEventCountFact(List<TrackingV1> trackings) {
        Events = new ArrayList<>();
        for (TrackingV1 t: trackings) {
            if (t.isDeleted()) continue;
            for (EventV1 e: t.getEventV1Collection()) {
                if (e.isDeleted()) continue;
                Events.add(e);
            }
        }
        Events = SortEventsByDate(Events);
    }

    @Override
    public void calculateData() {
        CalculateLargestByEventCountDay();
        if (LargestEventCountDay == null) return;
        calculatePriority();
        illustartion = new IllustartionModel(IllustrationType.EVENTSETREF);
        illustartion.setEventHistoryRef(LargestEventCountDay.getEventIds());
    }

    @Override
    protected void calculatePriority() {
        priority = 1.5 * LargestEventCountDay.getEventCount();
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.LargestEventCountDayDescription(LargestEventCountDay);
    }

    public boolean IsFactSignificant() {
        return LargestEventCountDay != null && LargestEventCountDay.getEventCount() > 1;
    }

    private void CalculateLargestByEventCountDay() {
        List<TimeSpanEventData> counts = new ArrayList<>();
        for (EventV1 e: Events) {
            DateTime date = new DateTime(e.GetEventDate());
            boolean dayFound = false;
            for (TimeSpanEventData d: counts) {
                if (d.IsItThisDay(date)) {
                    d.CountIncrement(e.GetEventId());
                    dayFound = true;
                    break;
                }
            }
            if (!dayFound) {
                TimeSpanEventData data = new TimeSpanEventData(date);
                data.CountIncrement(e.GetEventId());
                counts.add(data);
            }
        }
        int maxCount = 0;
        for (TimeSpanEventData d: counts) {
            if (d.getEventCount() > maxCount) {
                maxCount = d.getEventCount();
                LargestEventCountDay = d;
            }
        }
    }

    private List<EventV1> SortEventsByDate(List<EventV1> events) {
        List<EventV1> copy = new ArrayList<>(events);
        Collections.sort(copy, new Comparator<EventV1>() {
            @Override
            public int compare(EventV1 event, EventV1 t1) {
                return t1.GetEventDate().compareTo(event.GetEventDate());
            }
        });
        return copy;
    }
}
