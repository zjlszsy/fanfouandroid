package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import org.oauthsimple.model.OAuthToken;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * Created by wendyzhou on 3/24/2017.
 */

class TimelineRequest extends AsyncTask<FanfouAPI, Void, List<FanfouStatus>> {
    private Exception exception;

    protected List<FanfouStatus> doInBackground(FanfouAPI ... inputAPI) {
        try {
            FanfouAPI api = inputAPI[0];
            String returnedTimeline = api.fetchTimeline(api.getURL());
            InputStream stream = new ByteArrayInputStream(returnedTimeline.getBytes(StandardCharsets.UTF_8));
            FeedParser xmlParser = new FeedParser();
            return xmlParser.parse(stream);
        } catch (Exception e) {
            this.exception = e;
            Log.e("Exception", "detail", e);
            return null;
        }
    }

    protected void onPostExecute(List<FanfouStatus> statusList) {
    }
}
