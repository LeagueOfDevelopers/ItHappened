package ru.lod_misis.ithappened.Statistics.Facts.OneTrackingStatistcs;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;


public class AvrgRatingFact {


    Tracking tracking;
    List<Event> eventCollection = new ArrayList<>();
    Double priority = 3.0;

    public AvrgRatingFact(Tracking tracking){
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
                    sumValue+=event.GetRating().GetRatingValue();
                    count++;
                }
            }
            return sumValue/count;
        }else{
            return null;
        }
    }

    public Boolean applicabilityFunction(){
        if(tracking.GetRatingCustomization()== TrackingCustomization.None){
            return false;
        }
        if(tracking.GetRatingCustomization()==TrackingCustomization.Required && eventCollection.size()>1){
            return true;
        }
        if(tracking.GetRatingCustomization()==TrackingCustomization.Optional){
            int count = 0;
            for(Event event:eventCollection){
                if(event.GetRating()!=null){
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
        return Math.sqrt(getAvrgValue());
    }

}
