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
import ru.lod_misis.ithappened.Statistics.Facts.Models.DayEventData;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;

public class DayWithLargestEventCount extends Fact {

    private List<Event> Events;
    private DayEventData LargestEventCountDay;

    public DayWithLargestEventCount(List<Tracking> trackings) {
        Events = new ArrayList<>();
        for (Tracking t: trackings) {
            for (Event e: t.GetEventCollection()) {
                if (!e.isDeleted()) { Events.add(e); }
            }
        }
        Events = SortEventsByDate(Events);
    }

    @Override
    public void calculateData() {
        CalculateLargestByEventCountDay();
    }

    @Override
    protected void calculatePriority() {
        priority = 1.5 * LargestEventCountDay.getEventCount();
    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.LargestEventCountDescription(LargestEventCountDay);
    }

    public boolean IsFactSignificant() {
        return LargestEventCountDay.getEventCount() > 1;
    }

    private void CalculateLargestByEventCountDay() {
        List<DayEventData> counts = new ArrayList<>();
        for (Event e: Events) {
            DateTime date = new DateTime(e.GetEventDate());
            boolean dayFound = false;
            for (DayEventData d: counts) {
                if (d.IsItThisDay(date)) {
                    d.CountIncrement();
                    dayFound = true;
                    break;
                }
            }
            if (!dayFound) {
                DayEventData data = new DayEventData(date);
                data.CountIncrement();
                counts.add(data);
            }
        }
        Collections.sort(counts, new Comparator<DayEventData>() {
            @Override
            public int compare(DayEventData dayEventData, DayEventData t1) {
                return t1.getEventCount() - dayEventData.getEventCount();
            }
        });
        LargestEventCountDay = counts.get(0);
    }

    private List<Event> SortEventsByDate(List<Event> events) {
        List<Event> copy = new ArrayList<>(events);
        Collections.sort(copy, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return event.GetEventDate().compareTo(t1.GetEventDate());
            }
        });
        return copy;
    }
}
