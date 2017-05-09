package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;


/**
 * Created by wendyzhou on 3/24/2017.
 */

class TimelineRequest extends AsyncTask<FanfouAPI, Void, List<FanfouStatus>> {
    private Activity activity;
    private FanfouAPI api;
    public final int STATUS_REMAINING = 5;
    private static HashSet<String> lastMsgIds;

    protected List<FanfouStatus> doInBackground(FanfouAPI ... inputAPI) {
        try {
            api = inputAPI[0];
            String returnedTimeline = api.fetchTimeline();
            InputStream stream = new ByteArrayInputStream(returnedTimeline.getBytes(StandardCharsets.UTF_8));
            FeedParser xmlParser = new FeedParser();
            return xmlParser.parse(stream);
        } catch (Exception e) {
            Log.e("Exception", "detail", e);
        }
        return null;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    protected void onPostExecute(List<FanfouStatus> list) {
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.timeline_loading_progress);
        progressBar.setVisibility(View.GONE);
        final FeedListAdaptor adaptor = new FeedListAdaptor(activity, list, api);
        ListView feedList = (ListView) activity.findViewById(R.id.list);
        feedList.setAdapter(adaptor);
        final List<FanfouStatus> statusListFinal = list;
        lastMsgIds = new HashSet<String>();
        feedList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                String lastMessageID = statusListFinal.get(totalItemCount - 2).statusID;

                if (lastInScreen >= totalItemCount - STATUS_REMAINING && !lastMsgIds.contains(lastMessageID)) {
                    lastMsgIds.add(lastMessageID);
                    LoadMoreTimelineIntoFeedlistRequest request = new LoadMoreTimelineIntoFeedlistRequest();
                    request.setID(statusListFinal.get(totalItemCount - 2).statusID);
                    request.statusList = statusListFinal;
                    request.adaptor = adaptor;
                    try {
                        request.execute(api);
                    } catch (Exception e){
                        Log.e("Exception", "detail", e);
                    }
                }
            }
        });
    }
}
