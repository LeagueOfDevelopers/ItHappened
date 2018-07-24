package ru.lod_misis.ithappened.Statistics.Facts.Models.Trends;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.EventV1;

public class EventsTimeDistribution {

    private List<Interval> intervalKeys;
    private List<List<EventV1>> events;

    public EventsTimeDistribution() {
        intervalKeys = new ArrayList<>();
        events = new ArrayList<>();
    }

    public int addNewInterval(DateTime from, DateTime to) {
        if (from.isAfter(to)) {
            return -1;
        }
        intervalKeys.add(new Interval(from, to));
        events.add(new ArrayList<EventV1>());
        return intervalKeys.size() - 1;
    }

    public Interval getIntervalById(int id) {
        return intervalKeys.get(id);
    }

    public void addEventToCollection(EventV1 event, int id) {
        events.get(id).add(event);
    }

    public boolean isThisIntervalEmpty(int id) {
        return events.get(id).size() == 0;
    }

    public EventV1 getFirstEventFromInterval(int id) {
        return events.get(id).get(0);
    }

    public List<Double> toCountsArray() {
        List<Double> counts = new ArrayList<>();
        for (List<EventV1> uuids : events) {
            counts.add((double)uuids.size());
        }
        return counts;
    }
}
