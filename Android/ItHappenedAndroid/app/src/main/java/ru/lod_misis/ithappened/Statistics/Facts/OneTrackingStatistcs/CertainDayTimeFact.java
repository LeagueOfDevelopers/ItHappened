package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Handler;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.DayTime;
import ru.lod_misis.ithappened.Statistics.Facts.Models.DayTimeFactModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;
import ru.lod_misis.ithappened.Statistics.Facts.Models.WeekDaysFactModel;

/**
 * Created by Ded on 29.03.2018.
 */

public class CertainDayTimeFact extends Fact{
    Tracking tracking;
    List<DayTimeFactModel> modelList;


    public CertainDayTimeFact(Tracking tracking) {
            this.tracking = tracking;
            trackingId = tracking.GetTrackingID();
    }

    @Override
    public void calculateData() {
        illustartion = new IllustartionModel(IllustrationType.PIE);
        List<Event> eventCollection = new ArrayList<>();

        for(Event event: tracking.GetEventCollection()){
            if (!event.isDeleted())
                eventCollection.add(event);
        }

        int[] eventCount = new int[4];
        Calendar c = Calendar.getInstance();

        modelList = new ArrayList<>();

        for(Event event: eventCollection){
            c.setTime(event.GetEventDate());
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour < 6) eventCount[0]++;
            else if (hour<12) eventCount[1]++;
            else if (hour<18) eventCount[2]++;
            else eventCount[3]++;
        }

        for (int i = 0; i<4; i++) {
            if (eventCount[i] > 0) {
                DayTimeFactModel model = new DayTimeFactModel();
                double percentage = (double)eventCount[i] / eventCollection.size() * 100;
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

        return String.format("В %s%s случаев событие %s происходит %s",
                format.format(model.getPercetage()), "%", tracking.getTrackingName(), dayTime);
    }
}
