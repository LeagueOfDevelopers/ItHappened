package ru.lod_misis.ithappened.StatisticsHelpers;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;

import java.util.List;

public class TextStatisticsHelper {

    private Tracking tracking;
    List<Event> eventsCollection;

    public TextStatisticsHelper(Tracking tracking) {

        this.tracking = tracking;

        eventsCollection = tracking.GetEventCollection();

        for(int i=0;i<eventsCollection.size();i++){
            if(eventsCollection.get(i).GetStatus()){
                eventsCollection.remove(i);
            }
        }
    }

    public Double getAvrgScale(){
        double avrgScale = 0;
        int eventsSize = 0;
        if(tracking.GetScaleCustomization()== TrackingCustomization.None){
            return null;
        }else{
            if(eventsCollection.size()!=0) {
                for (Event event : eventsCollection) {
                    if (event.GetScale() == null) {
                        avrgScale += 0;
                        eventsSize++;
                    } else {
                        avrgScale += event.GetScale().doubleValue();
                        eventsSize++;
                    }
                }
            }else{
                return 0.0;
            }
        }
        return avrgScale/eventsSize;
    }

    public Integer getEventsCount(){
        return tracking.GetEventCollection().size();
    }

    public Double getAvrgRating(){
        double avrgRating = 0;
        int eventsSize = 0;
        if(tracking.GetRatingCustomization() == TrackingCustomization.None){
            return null;
        }else{
            if(eventsCollection.size()!=0) {
                for (Event event : eventsCollection) {
                    if (event.GetRating() == null) {
                        avrgRating += 0;
                        eventsSize++;
                    } else {
                        avrgRating += event.GetRating().GetRatingValue();
                        eventsSize++;
                    }
                }
            }else{
                return 0.0;
            }
        }
        return avrgRating/eventsSize;
    }

    public Double getScaleSum(){
        double scaleSum = 0;
        if(tracking.GetScaleCustomization() == TrackingCustomization.None){
            return null;
        }else{
            if(eventsCollection.size()==0){
                return 0.0;
            }else{
                for(Event event : eventsCollection){
                    if(event.GetScale()==null){
                        scaleSum+=0;
                    }else{
                        scaleSum+=event.GetScale().doubleValue();
                    }
                }
            }
        }
        return scaleSum;
    }

    public Integer getRatingSum(){
        int ratingSum = 0;
        if(tracking.GetRatingCustomization() == TrackingCustomization.None){
            return null;
        }else{
            if(eventsCollection.size()==0){
                return 0;
            }else{
                for(Event event : eventsCollection){
                    if(event.GetRating()==null){
                        ratingSum+=0;
                    }else{
                        ratingSum+=event.GetRating().GetRatingValue();
                    }
                }
            }
        }
        return ratingSum;
    }
}
