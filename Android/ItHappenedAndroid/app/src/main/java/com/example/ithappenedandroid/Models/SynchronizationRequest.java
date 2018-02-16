package com.example.ithappenedandroid.Models;

import com.example.ithappenedandroid.Domain.Tracking;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SynchronizationRequest {

    @Expose
    @SerializedName("userNickname")
    public String UserNickname;
    @Expose
    @SerializedName("nicknameDateOfChange")
    public Date NicknameDateOfChange;
    @Expose
    @SerializedName("trackingCollection")
    public List<Tracking> TrackingCollection;

    public SynchronizationRequest(String userNickname, Date nicknameDateOfChange, List<Tracking> trackingCollection) {
        UserNickname = userNickname;
        NicknameDateOfChange = nicknameDateOfChange;
        TrackingCollection = trackingCollection;
    }

    public String getUserNickname() {
        return UserNickname;
    }

    public void setUserNickname(String userNickname) {
        UserNickname = userNickname;
    }

    public Date getNicknameDateOfChange() {
        return NicknameDateOfChange;
    }

    public void setNicknameDateOfChange(Date nicknameDateOfChange) {
        NicknameDateOfChange = nicknameDateOfChange;
    }

    public List<Tracking> getTrackingCollection() {
        return TrackingCollection;
    }

    public void setTrackingCollection(List<Tracking> trackingCollection) {
        TrackingCollection = trackingCollection;
    }
}
