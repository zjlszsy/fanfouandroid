package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Log;

import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by wendyzhou on 3/24/2017.
 */

class HttpRequest extends AsyncTask<String, Void, List<FanfouStatus>> {
    private Exception exception;
    public String mUsernameInput;
    public String mPasswordInput;

    protected List<FanfouStatus> doInBackground(String... url) {
        try {
            URL timelineAPI = new URL(url[0]);
            URLConnection timelineConnection = timelineAPI.openConnection();

//            String userpass = String.format(Locale.US, "%s:%s", mUsernameInput, mPasswordInput);
//            String basicAuth = String.format(Locale.US, "Basic %s", new String(Base64.encode(userpass.getBytes(), Base64.NO_WRAP)));
//            timelineConnection.setRequestProperty("Authorization", basicAuth);
            InputStream in = timelineConnection.getInputStream();
            FeedParser xmlParser = new FeedParser();
            return xmlParser.parse(in);
        } catch (Exception e) {
            this.exception = e;
            Log.e("Exception", "detail", e);
            return null;
        }
    }

    protected void onPostExecute(List<FanfouStatus> statusList) {
    }
}
