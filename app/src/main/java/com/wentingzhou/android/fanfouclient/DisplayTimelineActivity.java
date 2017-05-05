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
import java.util.HashSet;
import java.util.List;


public class DisplayTimelineActivity extends Activity {
    public final int status_Remaining = 5;
    public static final String API = "userFanfouAPI";
    private static HashSet<String> LAST_MSG_IDS;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        LAST_MSG_IDS = new HashSet<String>();
        final FanfouAPI api = getIntent().getParcelableExtra(API);
        TimelineRequest request = new TimelineRequest();
        List<FanfouStatus> statusList = null;
        try {
            statusList = request.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        final List<FanfouStatus> statusListFinal = statusList;
        final FeedListAdaptor adaptor = new FeedListAdaptor(this, statusListFinal, api);
        ListView feedList = (ListView) findViewById(R.id.list);
        feedList.setAdapter(adaptor);
        feedList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                String lastMessageID = statusListFinal.get(totalItemCount - 1).statusID;

                if (lastInScreen == totalItemCount - status_Remaining && !LAST_MSG_IDS.contains(lastMessageID)) {
                    LAST_MSG_IDS.add(lastMessageID);
                    LoadMoreTimelineRequest request = new LoadMoreTimelineRequest();
                    request.setID(statusListFinal.get(totalItemCount - 1).statusID);
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




