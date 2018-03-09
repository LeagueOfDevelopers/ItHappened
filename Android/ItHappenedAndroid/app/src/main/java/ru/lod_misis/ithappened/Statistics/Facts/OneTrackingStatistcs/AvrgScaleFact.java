package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;

/**
 * Created by Пользователь on 09.03.2018.
 */

public class AvrgScaleFact {

    Tracking tracking;
    List<Event> eventCollection = new ArrayList<>();
    Double priority = 3.0;

    public AvrgScaleFact(Tracking tracking){
        this.tracking = tracking;
        for(Event event : tracking.GetEventCollection()){
            if(!event.GetStatus()){
                eventCollection.add(event);
            }
        }
    }

    public Double getAvrgValue(){
        if(applicabilityFunction()){
            Double sumValue = 0.0;
            int count = 0;
            for(Event event : eventCollection){
                if(event.GetScale()!=null){
                    sumValue+=event.GetScale();
                    count++;
                }
            }
            return sumValue/count;
        }else{
            return null;
        }
    }

    public Boolean applicabilityFunction(){
        if(tracking.GetScaleCustomization()== TrackingCustomization.None){
            return false;
        }
        if(tracking.GetScaleCustomization()==TrackingCustomization.Required && eventCollection.size()>1){
            return true;
        }
        if(tracking.GetScaleCustomization()==TrackingCustomization.Optional){
            int count = 0;
            for(Event event:eventCollection){
                if(event.GetScale()!=null){
                    count++;
                }
            }
            if(count>1){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public Double getPriority(){
        return priority;
    }

}
