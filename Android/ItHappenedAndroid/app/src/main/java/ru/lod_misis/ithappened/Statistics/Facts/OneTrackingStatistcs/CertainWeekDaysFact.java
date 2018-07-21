package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;
import ru.lod_misis.ithappened.Statistics.Facts.Models.WeekDaysFactModel;

public class CertainWeekDaysFact extends Fact {

    private TrackingV1 trackingV1;
    private List<WeekDaysFactModel> modelList = new ArrayList<>();

    public CertainWeekDaysFact(TrackingV1 trackingV1) {
        trackingId = trackingV1.GetTrackingID();
        this.trackingV1 = trackingV1;
    }

    @Override
    public void calculateData() {
        List<EventV1> eventV1Collection = new ArrayList<>();

        for(EventV1 eventV1 : trackingV1.GetEventHistory()){
            if(!eventV1.isDeleted()) eventV1Collection.add(eventV1);
        }

        for(int i = 1; i <= 7; i++)
        {
            illustartion = new IllustartionModel(IllustrationType.PIE);
            WeekDaysFactModel model = new WeekDaysFactModel();
            Calendar c = Calendar.getInstance();
            int eventCount = 0;
            for (EventV1 eventV1 : eventV1Collection) {
                c.setTime(eventV1.GetEventDate());
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
