package ru.lod_misis.ithappened.Data.DTOModels;

import com.google.gson.annotations.Expose;

/**
 * Created by Пользователь on 01.05.2018.
 */

public class RefreshModel {

    @Expose
    public String accessToken;
    @Expose
    public String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
