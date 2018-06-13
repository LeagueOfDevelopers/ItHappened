package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;

/**
 * Created by Ded on 23.03.2018.
 */

public class BestEvent extends Fact {

    NewTracking newTracking;
    List<NewEvent> newEventCollection;
    NewEvent bestNewEvent;

    public BestEvent(NewTracking newTracking)
    {
        this.newTracking = newTracking;
        this.trackingId = newTracking.GetTrackingID();
        newEventCollection = new ArrayList<>();
    }

    @Override
    public void calculateData() {

        for(NewEvent newEvent : newTracking.GetEventCollection()){
            if(!newEvent.GetStatus() && newEvent.GetRating()!=null){
                newEventCollection.add(newEvent);
            }
        }

        bestNewEvent = newEventCollection.get(0);

        Date curDateTime = Calendar.getInstance(TimeZone.getDefault()).getTime();

        for(NewEvent newEvent : newEventCollection)
        {
            Date bestTime = newEvent.GetEventDate();
            if (bestNewEvent.GetRating().getRating() <= newEvent.GetRating().getRating()
                    && 7.0 < ((double)(curDateTime.getTime() - bestTime.getTime())/1000/60/60/24)
                    && bestNewEvent.GetEventDate().after(newEvent.GetEventDate()))
                bestNewEvent = newEvent;
        }

        illustartion = new IllustartionModel(IllustrationType.EVENTREF);
        illustartion.setNewEventRef(bestNewEvent);

        calculatePriority();
    }

    @Override
    public void calculatePriority() {
        priority = (double) bestNewEvent.GetRating().getRating();
    }

    @Override
    public String textDescription() {
        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);

        String toReturn = String.format("Лучшее событие <b>%s</b> произошло <b>%s</b>, " +
                        "вы поставили ему <b>%s</b>", newTracking.getTrackingName(),
                format.format(bestNewEvent.GetEventDate()), bestNewEvent.GetRating().getRating()/2.0);

        if (bestNewEvent.GetComment() == null) return toReturn;

        return String.format(toReturn, " с комментарием <b>%s</b>", bestNewEvent.GetComment());
    }

    public NewEvent getBestNewEvent() { return bestNewEvent; }
}
