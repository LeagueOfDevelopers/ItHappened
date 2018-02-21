package com.example.ithappenedandroid.StatisticsHelpers;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;

import java.util.List;

public class TextStatisticsHelper {

    private Tracking tracking;

    public TextStatisticsHelper(Tracking trackings) {
        this.tracking = tracking;
    }

    public Double getAvrgScale(){
        Double avrgScale = null;
        int eventsSize = 0;
        if(tracking.GetScaleCustomization()== TrackingCustomization.None){
            return avrgScale;
        }else{
            List<Event> eventsCollection = tracking.GetEventCollection();
            if(eventsCollection!=null) {
                for (Event event : eventsCollection) {
                    if (event.GetScale() == null) {
                        avrgScale += 0;
                        eventsSize++;
                    } else {
                        avrgScale += event.GetScale();
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
        if(tracking.GetEventCollection()!=null)
        return tracking.GetEventCollection().size();
        else{
            return 0;
        }
    }

    public Double getAvrgRating(){
        Double avrgRating = null;
        int eventsSize = 0;
        if(tracking.GetRatingCustomization() == TrackingCustomization.None){
            return avrgRating;
        }else{
            List<Event> eventsCollection = tracking.GetEventCollection();
            if(eventsCollection!=null) {
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
        Double scaleSum = null;
        if(tracking.GetScaleCustomization() == TrackingCustomization.None){
            return scaleSum;
        }else{
            List<Event> eventsCollection = tracking.GetEventCollection();
            if(eventsCollection==null){
                return 0.0;
            }else{
                for(Event event : eventsCollection){
                    if(event.GetScale()==null){
                        scaleSum+=0;
                    }else{
                        scaleSum+=event.GetScale();
                    }
                }
            }
        }
        return scaleSum;
    }

    public Integer getRatingSum(){
        Integer ratingSum = null;
        if(tracking.GetRatingCustomization() == TrackingCustomization.None){
            return ratingSum;
        }else{
            List<Event> eventsCollection = tracking.GetEventCollection();
            if(eventsCollection==null){
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
