package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;
import ru.lod_misis.ithappened.Statistics.Facts.Models.WeekDaysFactModel;

public class CertainWeekDaysFact extends Fact {

    private NewTracking newTracking;
    private List<WeekDaysFactModel> modelList = new ArrayList<>();

    public CertainWeekDaysFact(NewTracking newTracking) {
        trackingId = newTracking.GetTrackingID();
        this.newTracking = newTracking;
    }

    @Override
    public void calculateData() {
        List<NewEvent> newEventCollection = new ArrayList<>();

        for(NewEvent newEvent : newTracking.GetEventCollection()){
            if(!newEvent.isDeleted()) newEventCollection.add(newEvent);
        }

        for(int i = 1; i <= 7; i++)
        {
            illustartion = new IllustartionModel(IllustrationType.PIE);
            WeekDaysFactModel model = new WeekDaysFactModel();
            Calendar c = Calendar.getInstance();
            int eventCount = 0;
            for (NewEvent newEvent : newEventCollection) {
                c.setTime(newEvent.GetEventDate());
                if(c.get(Calendar.DAY_OF_WEEK) == i)
                    eventCount++;
            }
            if (eventCount != 0) {
                Double percentage = (double)eventCount / newEventCollection.size();
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
                format.format(model.getPercetage()), "%", newTracking.getTrackingName(), weekDay);
    }
}
