package ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.StringParse;

/**
 * Created by Ded on 30.03.2018.
 */

public class LongTimeAgoFact extends Fact{

    private TrackingV1 trackingV1;
    private Double daysSinceLastEvent;

    public LongTimeAgoFact(TrackingV1 trackingV1) {
        this.trackingV1 = trackingV1;
        trackingId = trackingV1.getTrackingId();
    }

    @Override
    public void calculateData() {

        List<EventV1> eventV1Collection = new ArrayList<>();
        for(EventV1 eventV1 : trackingV1.getEventHistory())
        {
            if (!eventV1.isDeleted())
                eventV1Collection.add(eventV1);
        }

        Date curDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        Date lastEventDate = new Date(Long.MIN_VALUE);

        for (EventV1 eventV1 : eventV1Collection)
        {
            if (lastEventDate.before(eventV1.getEventDate()))
                lastEventDate = eventV1.getEventDate();
        }

        daysSinceLastEvent =
                (double)(curDate.getTime() - lastEventDate.getTime())/1000/60/60/24;

        calculatePriority();

    }

    @Override
    protected void calculatePriority() {
        priority = Math.log(daysSinceLastEvent);
    }

    @Override
    public String textDescription() {
        NumberFormat format = new DecimalFormat("#.##");

        return String.format("Событие <b>%s</b> не происходило уже <b>%s</b> %s",
                trackingV1.getTrackingName(), format.format(daysSinceLastEvent),
                StringParse.days(daysSinceLastEvent.intValue()));
    }

    public Double getDaysSinceLastEvent() {
        return daysSinceLastEvent;
    }
}
