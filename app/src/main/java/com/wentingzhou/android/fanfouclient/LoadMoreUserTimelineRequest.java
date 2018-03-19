package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Log;

import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by wenting on 3/19/18.
 */

public class LoadMoreUserTimelineRequest extends AsyncTask<FanfouAPI, Void, List<FanfouStatus>> {
    public String userID;
    public String messageID;
    public List<FanfouStatus> statusList;
    public FeedListAdaptor adaptor;

    protected List<FanfouStatus> doInBackground(FanfouAPI ... inputAPI) {
        try {
            FanfouAPI api = inputAPI[0];
            String returnedTimeline = api.fetchMoreUserTimeline(userID, messageID);
            InputStream stream = new ByteArrayInputStream(returnedTimeline.getBytes(StandardCharsets.UTF_8));
            FeedParser xmlParser = new FeedParser();
            return xmlParser.parse(stream);
        } catch (Exception e) {
            Log.e("Exception", "detail", e);
            return null;
        }
    }

    protected void onPostExecute(List<FanfouStatus> list) {
        if (statusList != null && list != null) {
            statusList.addAll(list);
            adaptor.notifyDataSetChanged();
        } else if (list == null) {
            Log.e("Network Error", "Try again later");
        }
    }

    public void setUserID(String id) {
        this.userID = id;
    }

    public void setMessageID(String id) {
        this.messageID = id;
    }


}