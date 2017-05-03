package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import org.oauthsimple.model.OAuthToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class DisplayTimelineActivity extends Activity {
    public static final String USERNAME = "username";
    public final String TIMELINEURL = "http://api.fanfou.com/statuses/friends_timeline.xml";
    public final String MOREURL = "http://api.fanfou.com/statuses/friends_timeline.xml?max_id=%s";
    public final int statusRemaining = 5;
    public final String FRIENDlISTURL = "http://api.fanfou.com/users/friends.xml";
    private static final String DELIMITER = "\0";
    private static final String USERDETAIL = "userDetails";
    private static final String USERNAMEKEY = "username";
    private static final String TOKEN = "accessToken";


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        String userName = getIntent().getStringExtra(USERNAME);
        SharedPreferences accountInfo = getSharedPreferences(USERDETAIL, Context.MODE_PRIVATE);
        String[] names = accountInfo.getString(USERNAMEKEY, null).split(DELIMITER);
        String oauthToken = accountInfo.getString(TOKEN, null).split(DELIMITER)[Arrays.asList(names).indexOf(userName)];
        Gson gson = new Gson();
        OAuthToken token = gson.fromJson(oauthToken, OAuthToken.class);
        TimelineRequest request = new TimelineRequest();
        List<FanfouStatus> statusList = null;
        final FanfouAPI api = new FanfouAPI();
        api.setAccessToken(token);
        api.updateURL(TIMELINEURL);
        try {
            statusList = request.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        final List<FanfouStatus> listnerList = statusList;
        final FeedListAdaptor adaptor = new FeedListAdaptor(this, listnerList, userName);
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
                    TimelineRequest request = new TimelineRequest();
                    List<FanfouStatus> newStatusList = null;
                    String lastMessageID = listnerList.get(listnerList.size() - 1).statusID;
                    try {
                        api.updateURL(String.format(Locale.US, MOREURL, lastMessageID));
                        newStatusList = request.execute(api).get();
                        Log.e("Load more", "test");
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
        String userName = getIntent().getStringExtra(USERNAME);
        SharedPreferences accountInfo = getSharedPreferences(USERDETAIL, Context.MODE_PRIVATE);
        String[] names = accountInfo.getString(USERNAMEKEY, null).split(DELIMITER);
        String oauthToken = accountInfo.getString(TOKEN, null).split(DELIMITER)[Arrays.asList(names).indexOf(userName)];
        Gson gson = new Gson();
        OAuthToken token = gson.fromJson(oauthToken, OAuthToken.class);
        FanfouAPI api = new FanfouAPI();
        api.setAccessToken(token);
        api.updateURL(FRIENDlISTURL);
        ArrayList<String> friendList = null;
        try {
            friendList = friendListRequest.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        Intent newStatus = new Intent(this, NewStatusActivity.class);
        newStatus.putExtra(NewStatusActivity.USERNAME, getIntent().getStringExtra(USERNAME));
        newStatus.putExtra(NewStatusActivity.FRIENDLIST, friendList);
        startActivity(newStatus);
    }
}




