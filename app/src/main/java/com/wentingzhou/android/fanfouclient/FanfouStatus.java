package com.wentingzhou.android.fanfouclient;

/**
 * Created by wendyzhou on 3/29/2017.
 */

public class FanfouStatus {
    public String text;
    public FanfouUserInfo userinfo;

    public FanfouStatus(String text, FanfouUserInfo userinfo) {
        this.text = text;
        this.userinfo = userinfo;
    }
}