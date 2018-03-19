package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class DisplayTimelineActivity extends Activity {
    public final int STATUS_REMAINING = 5;
    public static final String API = "userFanfouAPI";
    private static HashSet<String> lastMsgIds;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        lastMsgIds = new HashSet<String>();
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
                String lastMessageID = statusListFinal.get(totalItemCount - 2).statusID;

                if (lastInScreen == totalItemCount - STATUS_REMAINING && !lastMsgIds.contains(lastMessageID)) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_status:
                FriendListRequest friendListRequest = new FriendListRequest();
                FanfouAPI api = getIntent().getParcelableExtra(API);
                ArrayList<String> friendList = null;
                try {
                    friendList = friendListRequest.execute(api).get();
                } catch (Exception e){
                    Log.e("Exception", "detail", e);
                }
                Intent newStatus = new Intent(this, NewStatusActivity.class);
                newStatus.putExtra(NewStatusActivity.FRIENDS_LIST, friendList);
                newStatus.putExtra(NewStatusActivity.API, api);
                startActivity(newStatus);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}




