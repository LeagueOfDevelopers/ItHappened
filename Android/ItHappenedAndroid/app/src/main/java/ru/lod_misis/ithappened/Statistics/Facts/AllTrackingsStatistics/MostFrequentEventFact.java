package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.FrequentEventsFactModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;

/**
 * Created by Ded on 09.03.2018.
 */

public class MostFrequentEventFact extends Fact{

    List<Tracking> trackingCollection;
    FrequentEventsFactModel minModel;
    List<FrequentEventsFactModel> periodList= new ArrayList<>();
    Double priority = 10.0;

    public MostFrequentEventFact(List<Tracking> trackingCollection)
    {
        this.trackingCollection = trackingCollection;
        trackingId = null;
    }

    public List<FrequentEventsFactModel> getFrequency()
    {
        for(Tracking tracking: trackingCollection) {
            double period;
            List<Event> eventCollection = tracking.GetEventCollection();
            Date dateOfFirstEvent = Calendar.getInstance(TimeZone.getDefault()).getTime();
            for (Event event : eventCollection) {
                if (!event.isDeleted() && event.GetEventDate().before(dateOfFirstEvent)) {
                    dateOfFirstEvent = event.GetEventDate();
                }
            }
            period = ((double)(Calendar.getInstance(TimeZone.getDefault()).getTime().getTime()
                    - dateOfFirstEvent.getTime()) / 1000 / 60 / 60 / 24);
            FrequentEventsFactModel model = new FrequentEventsFactModel
                    (period, tracking.GetTrackingName(), tracking.getTrackingId());
            periodList.add(model);
        }
        illustartion = new IllustartionModel(IllustrationType.BAR);
        illustartion.setFrequentEventsModelList(periodList);

        return periodList;
    }


    @Override
    public void calculateData() {
        getFrequency();
    }

    @Override
    public Double getPriority() {
        return priority;
    }

    @Override
    public void calculatePriority() {
        Double min = Double.MAX_VALUE;
        for (FrequentEventsFactModel model : periodList) {
            if (min > model.getPeriod()) {
                min = model.getPeriod();
                minModel = model;
            }
        }
        priority /= min;
    }

    @Override
    public String textDescription() {
        return String.format("Чаще всего у вас происходит событие %s - раз в %s.2f дней",
                minModel.getTrackingName(), minModel.getPeriod());
    }

}
