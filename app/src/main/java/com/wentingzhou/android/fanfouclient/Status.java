package com.wentingzhou.android.fanfouclient;

/**
 * Created by wendyzhou on 3/29/2017.
 */

public class Status {

    public String text;
    public String userNickName;
    public String profileImageLink;

    public Status(String text, String userNickName, String profileImageLink) {
        this.text = text;
        this.userNickName = userNickName;
        this.profileImageLink = profileImageLink;
    }
}