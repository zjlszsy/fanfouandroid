package com.wentingzhou.android.fanfouclient;

import android.content.Context;
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
    private String userID;
    private String messageID;
    private OnLoadMoreTimelineTaskCompleted task;
    private Context context;


    LoadMoreUserTimelineRequest(OnLoadMoreTimelineTaskCompleted task, Context context) {
        this.task = task;
        this.context = context;
    }

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
        if (list == null) {
            task.onTaskCompleted(context.getResources().getString(R.string.requestFailed));
            return;
        }
        task.updateReturnedNewList(list);
        task.onTaskCompleted(context.getResources().getString(R.string.requestSucceeded));
    }

    public void setUserID(String id) {
        this.userID = id;
    }

    public void setMessageID(String id) {
        this.messageID = id;
    }


}