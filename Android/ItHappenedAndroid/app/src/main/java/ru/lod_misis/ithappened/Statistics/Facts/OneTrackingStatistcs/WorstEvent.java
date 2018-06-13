package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustartionModel;
import ru.lod_misis.ithappened.Statistics.Facts.Models.IllustrationType;

/**
 * Created by Ded on 23.03.2018.
 */

public class WorstEvent extends Fact {

    NewTracking newTracking;
    List<NewEvent> newEventCollection;
    NewEvent worstNewEvent;

    public WorstEvent(NewTracking newTracking)
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

        worstNewEvent = newEventCollection.get(0);

        for(NewEvent newEvent : newEventCollection)
        {
            if (worstNewEvent.GetRating().getRating() > newEvent.GetRating().getRating()
                    && worstNewEvent.GetEventDate().after(newEvent.GetEventDate()))
                worstNewEvent = newEvent;
        }

        illustartion = new IllustartionModel(IllustrationType.EVENTREF);
        illustartion.setNewEventRef(worstNewEvent);

        calculatePriority();
    }

    @Override
    public void calculatePriority() {
        priority = 10.0 - worstNewEvent.GetRating().getRating();
    }

    @Override
    public String textDescription() {
        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        String toReturn = String.format("Худшее событие <b>%s</b> произошло <b>%s</b>, " +
                "вы поставили ему <b>%s</b>", newTracking.getTrackingName(),
                format.format(worstNewEvent.GetEventDate()),
                decimalFormat.format(worstNewEvent.GetRating().getRating()/2.0));

        if (worstNewEvent.GetComment() == null) return toReturn;

        return String.format(toReturn, " с комментарием <b>%s</b>", worstNewEvent.GetComment());
    }

    public NewEvent getWorstNewEvent() { return worstNewEvent; }

}
