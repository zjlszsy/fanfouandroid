package com.wentingzhou.android.fanfouclient.model;

import java.io.Serializable;
import com.google.gson.Gson;
import com.wentingzhou.android.fanfouclient.FanfouAPI;
import org.oauthsimple.model.OAuthToken;

/**
 * Created by wendyzhou on 3/29/2017.
 */

public class FanfouUserInfo implements Serializable{
    public String userNickName;
    public String profileImageLink;
    public String userID;
    private String tokenJson;

    public FanfouUserInfo(String userNickName, String profileImageLink, String userID) {
        this.userNickName = userNickName;
        this.profileImageLink = profileImageLink;
        this.userID = userID;
    }

    public void setTokenJson(OAuthToken token) {
        Gson gson = new Gson();
        tokenJson = gson.toJson(token);
    }

    public FanfouAPI getAPI() {
        FanfouAPI api = new FanfouAPI();
        api.setAccessToken(new Gson().fromJson(tokenJson, OAuthToken.class));
        return api;
    }

}
