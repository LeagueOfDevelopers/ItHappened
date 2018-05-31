package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.BreakData;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Builders.DescriptionBuilder;

public class LongestBreakFact extends Fact {

    private String TrackingName;
    private List<Event> Events;
    private BreakData LongestBreak;

    public LongestBreakFact(Tracking tracking) {
        TrackingName = tracking.GetTrackingName();
        trackingId = tracking.GetTrackingID();
        List<Event> events = new ArrayList<>();
        for (Event e: tracking.GetEventCollection()) {
            if (!e.isDeleted()) events.add(e);
        }
        Events = events;
    }

    @Override
    public void calculateData() {
        LongestBreak = FindLongestBreak();
    }

    @Override
    protected void calculatePriority() {

    }

    @Override
    public String textDescription() {
        return DescriptionBuilder.BuildLongestBreakDEscription(TrackingName, LongestBreak.getFirstEventDate(), LongestBreak.getSecondEventDate());
    }

    private BreakData FindLongestBreak () {
        List<Event> copy = SortEventsByDate(Events);
        long maxInterval = copy.get(1).GetEventDate().getTime() - copy.get(0).getEventDate().getTime();
        Event firstEvent = copy.get(0);
        Event secondEvent = copy.get(1);
        for (int i = 1; i < copy.size() - 1; i++) {
            long delta = copy.get(i + 1).getEventDate().getTime() - copy.get(i).getEventDate().getTime();
            if (delta > maxInterval) {
                firstEvent = copy.get(i);
                secondEvent = copy.get(i + 1);
                maxInterval = delta;
            }
        }
        return new BreakData(firstEvent.GetEventDate(),
                secondEvent.GetEventDate(),
                firstEvent.GetEventId(),
                secondEvent.GetEventId());
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
