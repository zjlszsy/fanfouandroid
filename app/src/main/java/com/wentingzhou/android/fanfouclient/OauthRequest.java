package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Log;



/**
 * Created by wendyzhou on 4/26/2017.
 */

public class OauthRequest extends AsyncTask<FanfouAPI, Void, String> {
    public String statusText;

    protected String doInBackground(FanfouAPI... api) {
        try {
            return api[0].postNewStatus(statusText, api[0].getURL());
        } catch (Exception e) {
            Log.e("IO expection", "Isue");
        }
        return null;
    }
}
