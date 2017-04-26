package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class DisplayTimelineActivity extends Activity {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public final String FAKEURL = "http://api.fanfou.com/statuses/friends_timeline.xml";
    public final String MOREURL = "http://api.fanfou.com/statuses/friends_timeline.xml?max_id=%s";
    public final int statusRemaining = 5;
    public final String FRIENDlISTURL = "http://api.fanfou.com/users/friends.xml";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        HttpRequest request = new HttpRequest();
        String userName = getIntent().getStringExtra(USERNAME);
        request.mUsernameInput = userName;

        String passWord = getIntent().getStringExtra(PASSWORD);
        request.mPasswordInput = passWord;
        List<FanfouStatus> statusList = null;
        try {
            statusList = request.execute(FAKEURL).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        final List<FanfouStatus> listnerList = statusList;
        final FeedListAdaptor adaptor = new FeedListAdaptor(this, listnerList, userName, passWord);
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
                    HttpRequest request = new HttpRequest();
                    request.mUsernameInput = getIntent().getStringExtra(USERNAME);
                    request.mPasswordInput = getIntent().getStringExtra(PASSWORD);

                    List<FanfouStatus> newStatusList = null;
                    String lastMessageID = listnerList.get(listnerList.size()-1).statusID;
                    try {
                        newStatusList = request.execute(String.format(Locale.US, MOREURL, lastMessageID)).get();
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
        friendListRequest.mUsernameInput = getIntent().getStringExtra(USERNAME);
        friendListRequest.mPasswordInput = getIntent().getStringExtra(PASSWORD);
        ArrayList<String> friendList = null;
        try {
            friendList = friendListRequest.execute(FRIENDlISTURL).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        Intent newStatus = new Intent(this, NewStatusActivity.class);
        newStatus.putExtra(NewStatusActivity.USERNAME, getIntent().getStringExtra(USERNAME));
        newStatus.putExtra(NewStatusActivity.PASSWORD, getIntent().getStringExtra(PASSWORD));
        newStatus.putExtra(NewStatusActivity.FRIENDLIST, friendList);
        startActivity(newStatus);
    }
}




