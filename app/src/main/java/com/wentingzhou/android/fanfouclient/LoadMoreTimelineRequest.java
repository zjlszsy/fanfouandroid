package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Log;

import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by wendyzhou on 5/4/2017.
 */

public class LoadMoreTimelineRequest extends AsyncTask<FanfouAPI, Void, List<FanfouStatus>> {
    public String id;
    private Exception exception;

    protected List<FanfouStatus> doInBackground(FanfouAPI ... inputAPI) {
        try {
            FanfouAPI api = inputAPI[0];
            String returnedTimeline = api.fetchMoreTimeline(id);
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

    public void setID(String id) {
        this.id = id;
    }
}