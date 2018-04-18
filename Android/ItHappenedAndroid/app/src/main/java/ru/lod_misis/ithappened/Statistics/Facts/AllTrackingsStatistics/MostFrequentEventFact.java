package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
            int eventCount = 0;
            List<Event> eventCollection = tracking.GetEventCollection();
            Date dateOfFirstEvent = Calendar.getInstance(TimeZone.getDefault()).getTime();
            for (Event event : eventCollection) {
                if (!event.isDeleted()) {
                    eventCount++;
                    if(event.GetEventDate().before(dateOfFirstEvent))
                        dateOfFirstEvent = event.GetEventDate();
                }
            }
            if (eventCount < 3) period = 0;
            else {
                period = ((double) (Calendar.getInstance(TimeZone.getDefault()).getTime().getTime()
                        - dateOfFirstEvent.getTime()) / 1000 / 60 / 60 / 24 / eventCount);
            }
            FrequentEventsFactModel model = new FrequentEventsFactModel
                    (period, tracking.GetTrackingName(), tracking.getTrackingId());
            periodList.add(model);
        }

        Collections.sort(periodList, new Comparator<FrequentEventsFactModel>() {
            @Override
            public int compare(FrequentEventsFactModel frequentEventsFactModel, FrequentEventsFactModel t1) {
                return t1.getPeriod().compareTo(frequentEventsFactModel.getPeriod());
            }
        });

        List<FrequentEventsFactModel> firstFiveModels = new ArrayList<>();

        int i =0;
        for (FrequentEventsFactModel model: periodList){
            if (i > 5) break;
            i++;
            firstFiveModels.add(model);
        }

        calculatePriority();
        illustartion = new IllustartionModel(IllustrationType.BAR);
        illustartion.setFrequentEventsModelList(firstFiveModels);

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
            if (min > model.getPeriod() && model.getPeriod()!= 0) {
                min = model.getPeriod();
                minModel = model;
            }
        }
        priority /= min;
    }

    @Override
    public String textDescription() {
        return String.format("Чаще всего у вас происходит событие <b>%s</b> - раз в <b>%.2f</b> дней",
                minModel.getTrackingName(), minModel.getPeriod());
    }

}
