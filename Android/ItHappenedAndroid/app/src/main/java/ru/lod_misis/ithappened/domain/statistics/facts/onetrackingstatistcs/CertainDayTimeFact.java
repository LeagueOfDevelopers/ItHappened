package ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.models.DayTimeFactModel;
import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustartionModel;
import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustrationType;

/**
 * Created by Ded on 29.03.2018.
 */

public class CertainDayTimeFact extends Fact{

    private TrackingV1 trackingV1;
    private List<DayTimeFactModel> modelList;


    public CertainDayTimeFact(TrackingV1 trackingV1) {
            this.trackingV1 = trackingV1;
            trackingId = trackingV1.getTrackingId();
    }

    @Override
    public void calculateData() {
        illustartion = new IllustartionModel(IllustrationType.PIE);
        List<EventV1> eventV1Collection = new ArrayList<>();

        for(EventV1 eventV1 : trackingV1.getEventHistory()){
            if (!eventV1.isDeleted())
                eventV1Collection.add(eventV1);
        }

        int[] eventCount = new int[4];
        Calendar c = Calendar.getInstance();

        modelList = new ArrayList<>();

        for(EventV1 eventV1 : eventV1Collection){
            c.setTime(eventV1.getEventDate());
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour < 6) eventCount[0]++;
            else if (hour<12) eventCount[1]++;
            else if (hour<18) eventCount[2]++;
            else eventCount[3]++;
        }

        for (int i = 0; i<4; i++) {
            if (eventCount[i] > 0) {
                DayTimeFactModel model = new DayTimeFactModel();
                double percentage = (double)eventCount[i] / eventV1Collection.size() * 100;
                model.calculateDate(i, percentage);
                modelList.add(model);
            }
        }

        illustartion.setDayTimeFactList(modelList);
        calculatePriority();
    }

    public DayTimeFactModel getHighestPercentage(){
        DayTimeFactModel highestModel = new DayTimeFactModel();
        double perc = 0;

        for (DayTimeFactModel model : modelList) {
            if (perc < model.getPercetage()) {
                perc = model.getPercetage();
                highestModel = model;
            }
        }

        return highestModel;
    }

    @Override
    protected void calculatePriority() {
        priority = 0.14 * getHighestPercentage().getPercetage();
    }

    @Override
    public String textDescription() {
        DayTimeFactModel model = getHighestPercentage();
        String dayTime = model.getDayTimeAsString();
        NumberFormat format = new DecimalFormat("#.##");

        return String.format("В <b>%s%s</b> случаев событие <b>%s</b> происходит <b>%s</b>",
                format.format(model.getPercetage()), "%", trackingV1.getTrackingName(), dayTime);
    }
}
