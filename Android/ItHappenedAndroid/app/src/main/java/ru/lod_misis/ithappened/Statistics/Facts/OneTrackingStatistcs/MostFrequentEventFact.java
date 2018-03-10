package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Models.FrequentEventsFactModel;

/**
 * Created by Ded on 09.03.2018.
 */

public class MostFrequentEventFact implements Fact{
    public MostFrequentEventFact(List<Tracking> trackingCollection)
    {
        this.trackingCollection = trackingCollection;
    }

    public List<FrequentEventsFactModel> getFrequency()
    {
        List<FrequentEventsFactModel> periodList = new ArrayList<>();

        for(Tracking tracking: trackingCollection) {
            int period;
            List<Event> eventCollection = tracking.GetEventCollection();
            Date dateOfFirstEvent = Calendar.getInstance(TimeZone.getDefault()).getTime();
            for (Event event : eventCollection) {
                if (!event.isDeleted() && event.GetEventDate().before(dateOfFirstEvent)) {
                    dateOfFirstEvent = event.GetEventDate();
                }
            }
            period = (int) ((Calendar.getInstance(TimeZone.getDefault()).getTime().getTime() - dateOfFirstEvent.getTime()) / 1000 / 60 / 60 / 24);
            FrequentEventsFactModel model = new FrequentEventsFactModel
                    (period, tracking.GetTrackingName(), tracking.getTrackingId());
            periodList.add(model);
        }
        return periodList;
    }


    public Boolean applicabilityFunction() {
        return false;
    }

    @Override
    public Double getPriority() {
        return priority;
    }

    List<Tracking> trackingCollection;
    Double priority = 10.0;
}
