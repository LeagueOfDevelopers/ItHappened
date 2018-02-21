package com.example.ithappenedandroid.StatisticsHelpers;

import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Domain.TrackingCustomization;

import java.util.List;

public class TextStatisticsHelper {

    private Tracking tracking;

    public TextStatisticsHelper(Tracking tracking) {
        this.tracking = tracking;
    }

    public Double getAvrgScale(){
        double avrgScale = 0;
        int eventsSize = 0;
        if(tracking.GetScaleCustomization()== TrackingCustomization.None){
            return null;
        }else{
            List<Event> eventsCollection = tracking.GetEventCollection();
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
            List<Event> eventsCollection = tracking.GetEventCollection();
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
            List<Event> eventsCollection = tracking.GetEventCollection();
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
            List<Event> eventsCollection = tracking.GetEventCollection();
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
