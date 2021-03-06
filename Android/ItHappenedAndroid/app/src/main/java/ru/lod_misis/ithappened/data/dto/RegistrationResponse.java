package ru.lod_misis.ithappened.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Пользователь on 16.02.2018.
 */

public class RegistrationResponse{


    @Expose
    @SerializedName("userNickname")
    public String UserNickname;
    @Expose
    @SerializedName("userId")
    public String UserId;
    @Expose
    @SerializedName("picUrl")
    public String PicUrl;
    @Expose
    @SerializedName("nicknameDateOfChange")
    public Date NicknameDateOfChange;
    @Expose
    @SerializedName("token")
    public String Token;
    @Expose
    @SerializedName("refreshToken")
    public String RefreshToken;

    public String getRefreshToken() {
        return RefreshToken;
    }

    public void setRefreshToken(String RefreshToken) {
        this.RefreshToken = RefreshToken;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public Date getNicknameDateOfChange() {
        return NicknameDateOfChange;
    }

    public void setNicknameDateOfChange(Date nicknameDateOfChange) {
        NicknameDateOfChange = nicknameDateOfChange;
    }

    public String getUserNickname() {
        return UserNickname;
    }

    public void setUserNickname(String userNickname) {
        UserNickname = userNickname;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

}
