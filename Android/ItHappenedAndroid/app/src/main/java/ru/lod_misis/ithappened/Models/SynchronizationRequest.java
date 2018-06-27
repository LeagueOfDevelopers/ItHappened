package ru.lod_misis.ithappened.Models;

import ru.lod_misis.ithappened.Domain.TrackingV1;

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
    @SerializedName("trackingV1Collection")
    public List<TrackingV1> trackingV1Collection;

    public SynchronizationRequest(String userNickname, Date nicknameDateOfChange, List<TrackingV1> trackingV1Collection) {
        UserNickname = userNickname;
        NicknameDateOfChange = nicknameDateOfChange;
        this.trackingV1Collection = trackingV1Collection;
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

    public List<TrackingV1> getTrackingV1Collection() {
        return trackingV1Collection;
    }

    public void setTrackingV1Collection(List<TrackingV1> trackingV1Collection) {
        this.trackingV1Collection = trackingV1Collection;
    }
}
