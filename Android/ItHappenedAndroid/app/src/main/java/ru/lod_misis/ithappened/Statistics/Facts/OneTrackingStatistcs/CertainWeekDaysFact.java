package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;
import ru.lod_misis.ithappened.Statistics.Facts.Models.WeekDaysFactModel;

/**
 * Created by Ded on 29.03.2018.
 */

public class CertainWeekDaysFact extends Fact {

    private Tracking tracking;
    private List<WeekDaysFactModel> modelList = new ArrayList<>();

    public CertainWeekDaysFact(Tracking tracking) {
        trackingId = tracking.GetTrackingID();
        this.tracking = tracking;
    }

    @Override
    public void calculateData() {
        List<Event> eventCollection = new ArrayList<>();

        for(Event event: tracking.GetEventCollection()){
            if(!event.isDeleted()) eventCollection.add(event);
        }

        for(int i = 1; i <= 7; i++)
        {
            illustartion = new IllustartionModel(IllustrationType.PIE);
            WeekDaysFactModel model = new WeekDaysFactModel();
            Calendar c = Calendar.getInstance();
            int eventCount = 0;
            for (Event event: eventCollection) {
                c.setTime(event.GetEventDate());
                if(c.get(Calendar.DAY_OF_WEEK) == i)
                    eventCount++;
            }
            if (eventCount != 0) {
                Double percentage = (double)eventCount / eventCollection.size();
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

        return String.format("В %.2f%s случаев событие %s происходит %s",
                model.getPercetage(), "%", tracking.getTrackingName(), weekDay);
    }
}
