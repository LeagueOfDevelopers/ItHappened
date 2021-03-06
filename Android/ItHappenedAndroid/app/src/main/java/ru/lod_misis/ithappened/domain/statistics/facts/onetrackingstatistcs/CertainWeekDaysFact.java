package ru.lod_misis.ithappened.domain.statistics.facts.onetrackingstatistcs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustartionModel;
import ru.lod_misis.ithappened.domain.statistics.facts.models.IllustrationType;
import ru.lod_misis.ithappened.domain.statistics.facts.models.WeekDaysFactModel;

public class CertainWeekDaysFact extends Fact {

    private TrackingV1 trackingV1;
    private List<WeekDaysFactModel> modelList;

    public CertainWeekDaysFact(TrackingV1 trackingV1) {
        trackingId = trackingV1.getTrackingId();
        this.trackingV1 = trackingV1;
        modelList = new ArrayList<>();
    }

    @Override
    public void calculateData() {
        List<EventV1> eventV1Collection = new ArrayList<>();
        modelList = new ArrayList<>();

        for(EventV1 eventV1 : trackingV1.getEventHistory()){
            if(!eventV1.isDeleted()) eventV1Collection.add(eventV1);
        }

        for(int i = 1; i <= 7; i++)
        {
            illustartion = new IllustartionModel(IllustrationType.PIE);
            WeekDaysFactModel model = new WeekDaysFactModel();
            Calendar c = Calendar.getInstance();
            int eventCount = 0;
            for (EventV1 eventV1 : eventV1Collection) {
                c.setTime(eventV1.getEventDate());
                if(c.get(Calendar.DAY_OF_WEEK) == i)
                    eventCount++;
            }
            if (eventCount != 0) {
                Double percentage = (double)eventCount / eventV1Collection.size();
                percentage *= 100;
                model.calculateData(percentage, i);
                modelList.add(model);
            }
        }
        illustartion.setWeekDaysFactList(modelList);
        calculatePriority();
    }

    public WeekDaysFactModel getHighestPercentage(){
        WeekDaysFactModel highestModel = new WeekDaysFactModel();
        double perc = 0;

        for (WeekDaysFactModel model : modelList) {
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
        WeekDaysFactModel model = getHighestPercentage();
        String weekDay = model.getWeekDayAsString();
        NumberFormat format = new DecimalFormat("#.##");

        return String.format("В <b>%s%s</b> случаев событие <b>%s</b> происходит <b>%s</b>",
                format.format(model.getPercetage()), "%", trackingV1.getTrackingName(), weekDay);
    }
}
