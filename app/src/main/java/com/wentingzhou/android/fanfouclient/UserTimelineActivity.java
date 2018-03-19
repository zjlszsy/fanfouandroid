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

/**
 * Created by wendyzhou on 3/31/2017.
 */

public class UserTimelineActivity extends Activity {
    public static final String user_id = "userID";
    public static final String API = "userFanfouAPI";
    private static HashSet<String> lastMsgIds;



    public void onCreate(Bundle currentBundle) {
        super.onCreate(currentBundle);
        setContentView(R.layout.main);
        lastMsgIds = new HashSet<String>();
        final FanfouAPI api = getIntent().getParcelableExtra(API);
        final String userID = getIntent().getStringExtra(user_id);
        UserTimelineRequest request = new UserTimelineRequest();
        request.setID(userID);
        List<FanfouStatus> statusList = new ArrayList<FanfouStatus>();
        try {
            statusList = request.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        final List<FanfouStatus> statusListFinal = statusList;
        final FeedListAdaptor adaptor = new FeedListAdaptor(this, statusList, api);
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

                if (lastInScreen == totalItemCount - 5 && !lastMsgIds.contains(lastMessageID)) {
                    Log.e("Loading", "More messages");
                    lastMsgIds.add(lastMessageID);
                    LoadMoreUserTimelineRequest request = new LoadMoreUserTimelineRequest();
                    request.setMessageID(statusListFinal.get(totalItemCount - 2).statusID);
                    request.setUserID(userID);
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
        newStatus.putExtra(NewStatusActivity.FRIENDS_LIST, friendList);
        newStatus.putExtra(NewStatusActivity.API, api);
        startActivity(newStatus);
    }



}
