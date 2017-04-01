package com.wentingzhou.android.fanfouclient.model;

/**
 * Created by wendyzhou on 3/29/2017.
 */

public class FanfouUserInfo {
    public String userNickName;
    public String profileImageLink;
    public String userID;

    public FanfouUserInfo(String userNickName, String profileImageLink, String userID) {
        this.userNickName = userNickName;
        this.profileImageLink = profileImageLink;
        this.userID = userID;
    }
}
