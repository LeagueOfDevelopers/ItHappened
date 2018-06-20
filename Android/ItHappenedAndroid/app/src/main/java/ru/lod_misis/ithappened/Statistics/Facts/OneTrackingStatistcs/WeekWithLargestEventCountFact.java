package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;
import ru.lod_misis.ithappened.Statistics.Facts.Models.TimeSpanEventData;

public class WeekWithLargestEventCountFact extends Fact {

    private List<Event> Events;
    private TimeSpanEventData Data;

    public WeekWithLargestEventCountFact(List<Tracking> trackings) {
        Events = new ArrayList<>();
        for (Tracking t: trackings) {
            if (t.isDeleted()) continue;
            for (Event e: t.GetEventCollection()) {
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
        for (Event e: Events) {
            DateTime date = new DateTime(e.GetEventDate());
            boolean weekFound = false;
            for (TimeSpanEventData d: counts) {
                if (d.IsItThisWeek(date)) {
                    weekFound = true;
                    d.CountIncrement();
                    break;
                }
            }
            if (!weekFound) {
                TimeSpanEventData data = new TimeSpanEventData(date);
                data.CountIncrement();
                counts.add(data);
            }
        }
        int maxCount = 0;
        for (TimeSpanEventData d: counts) {
            if (d.getEventCount() > maxCount) {
                maxCount = d.getEventCount();
                Data = d;
            }
        }
    }

    private List<Event> SortEventsByDate(List<Event> events) {
        List<Event> copy = new ArrayList<>(events);
        Collections.sort(copy, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return t1.GetEventDate().compareTo(event.GetEventDate());
            }
        });
        return copy;
    }
}
