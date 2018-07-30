package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;
import ru.lod_misis.ithappened.Statistics.Facts.Models.TimeSpanEventData;

public class WeekWithLargestEventCountFact extends Fact {

    private List<EventV1> Events;
    private TimeSpanEventData Data;

    public WeekWithLargestEventCountFact(List<TrackingV1> trackings) {
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
        FindWeekWithLargestEventCount();
        if (Data == null) return;
        calculatePriority();
        illustartion = new IllustartionModel(IllustrationType.EVENTSETREF);
        illustartion.setEventHistoryRef(Data.getIllustartionData());
    }

    @Override
    protected void calculatePriority() {
        priority = 0.75 * Data.getEventCount();
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.LargestEventCountWeekDescription(Data);
    }

    public boolean IsFactSignificant() {
        return Data != null && Data.getEventCount() > 1;
    }

    private void FindWeekWithLargestEventCount() {
        List<TimeSpanEventData> counts = new ArrayList<>();
        for (EventV1 e: Events) {
            DateTime date = new DateTime(e.GetEventDate());
            boolean weekFound = false;
            for (TimeSpanEventData d: counts) {
                if (d.IsItThisWeek(date)) {
                    weekFound = true;
                    d.CountIncrement(e.GetEventId());
                    break;
                }
            }
            if (!weekFound) {
                TimeSpanEventData data = new TimeSpanEventData(date);
                data.CountIncrement(e.GetEventId());
                counts.add(data);
            }
        }
        int maxCount = 0;
        for (TimeSpanEventData d: counts) {
            if (d.getEventCount() >= maxCount && d.getRightWeekBorder().isBefore(DateTime.now())) {
                maxCount = d.getEventCount();
                Data = d;
            }
        }
    }

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
