package com.wentingzhou.android.fanfouclient.model;

import com.wentingzhou.android.fanfouclient.model.FanfouUserInfo;

/**
 * Created by wendyzhou on 3/29/2017.
 */

public class FanfouStatus {
    public String text;
    public String statusID;
    public FanfouUserInfo userinfo;
    public String photo_URL;

    public FanfouStatus(String text, String statusID, FanfouUserInfo userinfo, String photo_URL) {
        this.text = text;
        this.statusID = statusID;
        this.userinfo = userinfo;
        this.photo_URL = photo_URL;
    }
}