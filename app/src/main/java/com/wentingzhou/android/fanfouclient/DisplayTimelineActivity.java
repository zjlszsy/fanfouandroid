package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DisplayTimelineActivity extends Activity {
    public final String TIMELINEURL = "http://api.fanfou.com/statuses/friends_timeline.xml";
    public final String MOREURL = "http://api.fanfou.com/statuses/friends_timeline.xml?max_id=%s";
    public final int statusRemaining = 5;
    public final String FRIENDlISTURL = "http://api.fanfou.com/users/friends.xml";
    public static final String API = "userFanfouAPI";


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        final FanfouAPI api = getIntent().getParcelableExtra(API);
        api.updateURL(TIMELINEURL);

        TimelineRequest request = new TimelineRequest();
        List<FanfouStatus> statusList = null;
        try {
            statusList = request.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        final List<FanfouStatus> listnerList = statusList;
        final FeedListAdaptor adaptor = new FeedListAdaptor(this, listnerList, api);
        ListView feedList = (ListView) findViewById(R.id.list);
        feedList.setAdapter(adaptor);
        feedList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if (lastInScreen == totalItemCount - statusRemaining) {
                    LoadMoreTimelineRequest request = new LoadMoreTimelineRequest();
                    List<FanfouStatus> newStatusList = null;
                    request.id = listnerList.get(listnerList.size() - 1).statusID;
                    try {
                        newStatusList = request.execute(api).get();
                    } catch (Exception e){
                        Log.e("Exception", "detail", e);
                    }
                    listnerList.addAll(newStatusList);
                    adaptor.notifyDataSetChanged();
                }
            }
        });
    }

    public void openNewStatusActivity(View v) {
        FriendListRequest friendListRequest = new FriendListRequest();
        FanfouAPI api = getIntent().getParcelableExtra(API);
        ArrayList<String> friendList = null;
        try {
            friendList = friendListRequest.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        Intent newStatus = new Intent(this, NewStatusActivity.class);
        newStatus.putExtra(NewStatusActivity.FRIENDLIST, friendList);
        newStatus.putExtra(NewStatusActivity.API, api);
        startActivity(newStatus);
    }
}




