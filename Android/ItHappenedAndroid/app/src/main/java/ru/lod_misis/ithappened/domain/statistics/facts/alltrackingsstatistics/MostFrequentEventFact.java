package ru.lod_misis.ithappened.domain.statistics.facts.alltrackingsstatistics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.models.FrequentEventsFactModel;
import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustartionModel;
import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustrationType;
import ru.lod_misis.ithappened.domain.statistics.facts.StringParse;

public class MostFrequentEventFact extends Fact {

    private List<TrackingV1> trackingV1Collection;
    private FrequentEventsFactModel minModel;
    private List<FrequentEventsFactModel> periodList= new ArrayList<>();
    private Double priority = 10.0;

    public MostFrequentEventFact(List<TrackingV1> trackingV1Collection) {
        this.trackingV1Collection = trackingV1Collection;
        trackingId = null;
    }

    public List<FrequentEventsFactModel> getFrequency() {
        for (TrackingV1 trackingV1 : trackingV1Collection) {
            double period;
            int eventCount = 0;
            List<EventV1> eventV1Collection = trackingV1.getEventHistory();
            Date dateOfFirstEvent = Calendar.getInstance(TimeZone.getDefault()).getTime();
            for (EventV1 eventV1 : eventV1Collection) {
                if (!eventV1.isDeleted()) {
                    eventCount++;
                    if (eventV1.getEventDate().before(dateOfFirstEvent))
                        dateOfFirstEvent = eventV1.getEventDate();
                }
            }
            if (eventCount < 3) period = 0;
            else {
                period = ((double) (Calendar.getInstance(TimeZone.getDefault()).getTime().getTime()
                        - dateOfFirstEvent.getTime()) / 1000 / 60 / 60 / 24 / eventCount);
            }
            Integer color = Integer.parseInt(trackingV1.getColor());
            FrequentEventsFactModel model = new FrequentEventsFactModel
                    (period, trackingV1.getTrackingName(), trackingV1.getTrackingId().toString(),
                            Integer.parseInt(trackingV1.getColor()));
            periodList.add(model);
        }

        Collections.sort(periodList, new Comparator<FrequentEventsFactModel>() {
            @Override
            public int compare(FrequentEventsFactModel frequentEventsFactModel, FrequentEventsFactModel t1) {
                return frequentEventsFactModel.getPeriod().compareTo(t1.getPeriod());
            }
        });

        List<FrequentEventsFactModel> firstModels = new ArrayList<>();

        int i = 0;
        for (FrequentEventsFactModel model : periodList) {
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
            if (min > model.getPeriod() && model.getPeriod() != 0) {
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
