package ru.lod_misis.ithappened.Statistics.Facts.AllTrackingsStatistics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.FrequentEventsFactModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;

/**
 * Created by Ded on 09.03.2018.
 */

public class MostFrequentEventFact extends Fact{

    List<TrackingV1> trackingV1Collection;
    FrequentEventsFactModel minModel;
    List<FrequentEventsFactModel> periodList= new ArrayList<>();
    Double priority = 10.0;
    List<Integer> colors;

    public MostFrequentEventFact(List<TrackingV1> trackingV1Collection)
    {
        this.trackingV1Collection = trackingV1Collection;
        colors = new ArrayList<>();
        trackingId = null;
    }

    public List<FrequentEventsFactModel> getFrequency()
    {
        for(TrackingV1 trackingV1 : trackingV1Collection) {
            double period;
            int eventCount = 0;
            List<EventV1> eventV1Collection = trackingV1.GetEventCollection();
            Date dateOfFirstEvent = Calendar.getInstance(TimeZone.getDefault()).getTime();
            for (EventV1 eventV1 : eventV1Collection) {
                if (!eventV1.isDeleted()) {
                    eventCount++;
                    if(eventV1.GetEventDate().before(dateOfFirstEvent))
                        dateOfFirstEvent = eventV1.GetEventDate();
                }
            }
            if (eventCount < 3) period = 0;
            else {
                period = ((double) (Calendar.getInstance(TimeZone.getDefault()).getTime().getTime()
                        - dateOfFirstEvent.getTime()) / 1000 / 60 / 60 / 24 / eventCount);
            }
            FrequentEventsFactModel model = new FrequentEventsFactModel
                    (period, trackingV1.GetTrackingName(), trackingV1.getTrackingId(), Integer.parseInt(trackingV1.getColor()));
            periodList.add(model);
        }

        Collections.sort(periodList, new Comparator<FrequentEventsFactModel>() {
            @Override
            public int compare(FrequentEventsFactModel frequentEventsFactModel, FrequentEventsFactModel t1) {
                return t1.getPeriod().compareTo(frequentEventsFactModel.getPeriod());
            }
        });

        List<FrequentEventsFactModel> firstModels = new ArrayList<>();

        int i =0;
        for (FrequentEventsFactModel model: periodList){
            i++;
            if (i > 4) break;
            firstModels.add(model);
        }

        calculatePriority();
        illustartion = new IllustartionModel(IllustrationType.BAR);
        illustartion.setFrequentEventsModelList(firstModels);

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
        DecimalFormat format = new DecimalFormat("#.##");
        return String.format("Чаще всего у вас происходит событие <b>%s</b> - раз в <b>%s</b> %s",
                minModel.getTrackingName(), format.format(minModel.getPeriod()),
                StringParse.days(minModel.getPeriod().intValue()));
    }

}
