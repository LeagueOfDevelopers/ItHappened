package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;

public class SumScaleFact {

    Tracking tracking;
    List<Event> eventCollection = new ArrayList<>();
    Double priority = 2.0;

    public SumScaleFact(Tracking tracking){
        this.tracking = tracking;
        for(Event event : tracking.GetEventCollection()){
            if(!event.GetStatus()){
                eventCollection.add(event);
            }
        }
    }

    public Double getSumValue(){
        if(applicabilityFunction()){
            Double sumValue = 0.0;
            for(Event event : eventCollection){
                if(event.GetScale()!=null){
                    sumValue+=event.GetScale();
                }
            }
            return sumValue;
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
