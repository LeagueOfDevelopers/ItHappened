package ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.models.builders.DescriptionBuilder;
import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustartionModel;
import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustrationType;
import ru.lod_misis.ithappened.domain.statistics.facts.models.TimeSpanEventData;

public class DayWithLargestEventCountFact extends Fact {

    private List<EventV1> Events;
    private TimeSpanEventData LargestEventCountDay;

    public DayWithLargestEventCountFact(List<TrackingV1> trackings) {
        Events = new ArrayList<>();
        for (TrackingV1 t: trackings) {
            if (t.isDeleted()) continue;
            for (EventV1 e: t.getEventCollection()) {
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
        illustartion.setEventHistoryRef(LargestEventCountDay.getIllustartionData());
    }

    @Override
    protected void calculatePriority() {
        priority = 1.5 * LargestEventCountDay.getEventCount();
    }

    @Override
    public String textDescription() {
        initIllustration();
        return DescriptionBuilder.LargestEventCountDayDescription(LargestEventCountDay);
    }

    public boolean IsFactSignificant() {
        return LargestEventCountDay != null && LargestEventCountDay.getEventCount() > 1;
    }

    private void CalculateLargestByEventCountDay() {
        List<TimeSpanEventData> counts = new ArrayList<>();
        for (EventV1 e: Events) {
            DateTime date = new DateTime(e.getEventDate());
            boolean dayFound = false;
            for (TimeSpanEventData d: counts) {
                if (d.IsItThisDay(date)) {
                    d.CountIncrement(e.getEventId());
                    dayFound = true;
                    break;
                }
            }
            if (!dayFound) {
                TimeSpanEventData data = new TimeSpanEventData(date);
                data.CountIncrement(e.getEventId());
                counts.add(data);
            }
        }
        int maxCount = 0;
        for (TimeSpanEventData d: counts) {
            if (d.getEventCount() >= maxCount && d.getDate().isBefore(DateTime.now())) {
                maxCount = d.getEventCount();
                LargestEventCountDay = d;
            }
        }
    }

    private void initIllustration(){
        Date dayDate = LargestEventCountDay.getDate().toDate();
        List<Date> dayBorders = new ArrayList<>();
        Date leftBoreder = new Date(dayDate.getYear(),
                dayDate.getMonth(),
                dayDate.getDate(),
                0,
                0);
        Date rightBoreder = new Date(dayDate.getYear(),
                dayDate.getMonth(),
                dayDate.getDate(),
                23,
                59);
        dayBorders.add(leftBoreder);
        dayBorders.add(rightBoreder);
        illustartion.setEventHistoryRef(dayBorders);
    }

    private List<EventV1> SortEventsByDate(List<EventV1> events) {
        List<EventV1> copy = new ArrayList<>(events);
        Collections.sort(copy, new Comparator<EventV1>() {
            @Override
            public int compare(EventV1 event, EventV1 t1) {
                return t1.getEventDate().compareTo(event.getEventDate());
            }
        });
        return copy;
    }
}
