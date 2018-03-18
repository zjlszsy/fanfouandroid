package com.wentingzhou.android.fanfouclient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;


/**
 * Created by wendyzhou on 4/26/2017.
 */

public class PostStatusRequest extends AsyncTask<FanfouAPI, Void, String> {
    public String statusText;
    public File photo;

    protected String doInBackground(FanfouAPI... api) {
        try {
            if (photo != null) {
                return api[0].uploadPhoto(photo, statusText);
            }
            return api[0].postNewStatus(statusText);
        } catch (Exception e) {
            Log.e("IO exception", "Issue");
        }
        return null;
    }
}
