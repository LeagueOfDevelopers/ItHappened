package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;

/**
 * Created by Ded on 30.03.2018.
 */

public class LongTimeAgoFact extends Fact{

    Tracking tracking;
    private Double daysSinceLastEvent;

    public LongTimeAgoFact(Tracking tracking) {
        this.tracking = tracking;
        trackingId = tracking.GetTrackingID();
    }

    @Override
    public void calculateData() {

        List<Event> eventCollection = new ArrayList<>();
        for(Event event: tracking.GetEventCollection())
        {
            if (!event.isDeleted())
                eventCollection.add(event);
        }

        Date curDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        Date lastEventDate = new Date(Long.MIN_VALUE);

        for (Event event: eventCollection)
        {
            if (lastEventDate.before(event.GetEventDate()))
                lastEventDate = event.GetEventDate();
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
                tracking.getTrackingName(), format.format(daysSinceLastEvent),
                StringParse.days((new Double(format.format(daysSinceLastEvent))).intValue()));
    }

    public Double getDaysSinceLastEvent() {
        return daysSinceLastEvent;
    }
}
