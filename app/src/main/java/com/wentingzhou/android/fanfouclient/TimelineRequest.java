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
 * Created by wendyzhou on 3/24/2017.
 */

class TimelineRequest extends AsyncTask<FanfouAPI, Void, List<FanfouStatus>> {
    private Exception exception;
    public List<FanfouStatus> listnerList;
    public FeedListAdaptor adaptor;


    protected List<FanfouStatus> doInBackground(FanfouAPI ... inputAPI) {
        try {
            FanfouAPI api = inputAPI[0];
            String returnedTimeline = api.fetchTimeline(api.getURL());
            if (returnedTimeline != null) {
                InputStream stream = new ByteArrayInputStream(returnedTimeline.getBytes(StandardCharsets.UTF_8));
                FeedParser xmlParser = new FeedParser();
                return xmlParser.parse(stream);
            }
        } catch (Exception e) {
            this.exception = e;
            Log.e("Exception", "detail", e);
        }
        return null;
    }

    protected void onPostExecute(List<FanfouStatus> list) {
        if (listnerList != null && list != null) {
            listnerList.addAll(list);
            adaptor.notifyDataSetChanged();
        } else if (list == null) {
            Log.e("Network Error", "Try again later");
        }
    }
}
