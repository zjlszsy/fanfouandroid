package com.wentingzhou.android.fanfouclient;

import com.wentingzhou.android.fanfouclient.model.FanfouUserInfo;

/**
 * Created by wenting on 3/20/18.
 */

interface OnOauthTaskCompleted extends OnTaskCompleted {

    void onTaskCompleted(String response);

    void setUserInfo(FanfouUserInfo userInfo);
}
