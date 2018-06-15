package ru.lod_misis.ithappened.Models;

import ru.lod_misis.ithappened.Domain.NewTracking;
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
    @SerializedName("newTrackingCollection")
    public List<NewTracking> newTrackingCollection;

    public SynchronizationRequest(String userNickname, Date nicknameDateOfChange, List<NewTracking> newTrackingCollection) {
        UserNickname = userNickname;
        NicknameDateOfChange = nicknameDateOfChange;
        this.newTrackingCollection = newTrackingCollection;
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

    public List<NewTracking> getNewTrackingCollection() {
        return newTrackingCollection;
    }

    public void setNewTrackingCollection(List<NewTracking> newTrackingCollection) {
        this.newTrackingCollection = newTrackingCollection;
    }
}
