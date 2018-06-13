package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;

/**
 * Created by Ded on 30.03.2018.
 */

public class LongTimeAgoFact extends Fact{

    NewTracking newTracking;
    private Double daysSinceLastEvent;

    public LongTimeAgoFact(NewTracking newTracking) {
        this.newTracking = newTracking;
        trackingId = newTracking.GetTrackingID();
    }

    @Override
    public void calculateData() {

        List<NewEvent> newEventCollection = new ArrayList<>();
        for(NewEvent newEvent : newTracking.GetEventCollection())
        {
            if (!newEvent.isDeleted())
                newEventCollection.add(newEvent);
        }

        Date curDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        Date lastEventDate = new Date(Long.MIN_VALUE);

        for (NewEvent newEvent : newEventCollection)
        {
            if (lastEventDate.before(newEvent.GetEventDate()))
                lastEventDate = newEvent.GetEventDate();
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
                newTracking.getTrackingName(), format.format(daysSinceLastEvent),
                StringParse.days(daysSinceLastEvent.intValue()));
    }

    public Double getDaysSinceLastEvent() {
        return daysSinceLastEvent;
    }
}
