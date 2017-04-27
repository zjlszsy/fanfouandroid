package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Log;

import org.oauthsimple.model.OAuthToken;

import java.io.IOException;
import java.net.URL;

/**
 * Created by wendyzhou on 4/26/2017.
 */

public class OauthRequest extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... url) {
        try {
            String timelineAPI = url[0];
            FanfouAPI api = new FanfouAPI();
            OAuthToken token = api.getOAuthAccessToken("zhouwentingzj@gmail.com", "azoth1042");
            api.setAccessToken(token);
            String result = api.fetchTimeline(timelineAPI);
            Log.e("Result is ", result);
            return result;
        } catch (IOException e) {
            Log.e("IO expection", "Isue");
        }
        return null;
    }
}
