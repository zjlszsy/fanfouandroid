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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class DisplayTimelineActivity extends Activity {
    public static final String USERNAME = "username";
    public final String TIMELINE_URL = "http://api.fanfou.com/statuses/friends_timeline.xml";
    public final String MORE_TIMELINE_URL = "http://api.fanfou.com/statuses/friends_timeline.xml?max_id=%s";
    public final int status_Remaining = 5;
    public final String FRIENDlIST_URL = "http://api.fanfou.com/users/friends.xml";
    private static final String DELIMITER = "\0";
    private static final String USER_DETAIL = "userDetails";
    private static final String USERNAME_KEY = "username";
    private static final String TOKEN = "accessToken";
    private static HashSet<String> LAST_MSG_IDS;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        LAST_MSG_IDS = new HashSet<String>();
        String userName = getIntent().getStringExtra(USERNAME);
        SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        String[] names = accountInfo.getString(USERNAME_KEY, null).split(DELIMITER);
        String oauthToken = accountInfo.getString(TOKEN, null).split(DELIMITER)[Arrays.asList(names).indexOf(userName)];
        Gson gson = new Gson();
        OAuthToken token = gson.fromJson(oauthToken, OAuthToken.class);
        TimelineRequest request = new TimelineRequest();
        List<FanfouStatus> statusList = null;
        final FanfouAPI api = new FanfouAPI();
        api.setAccessToken(token);
        api.updateURL(TIMELINE_URL);
        try {
            statusList = request.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        final List<FanfouStatus> statusListFinal = statusList;
        final FeedListAdaptor adaptor = new FeedListAdaptor(this, statusListFinal, userName);
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


                if (lastInScreen == totalItemCount - status_Remaining && !LAST_MSG_IDS.contains(lastMessageID)) {
                    LAST_MSG_IDS.add(lastMessageID);
                    TimelineRequest request = new TimelineRequest();
                    request.statusList = statusListFinal;
                    request.adaptor = adaptor;
                    try {
                        api.updateURL(String.format(Locale.US, MORE_TIMELINE_URL, lastMessageID));
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
        String userName = getIntent().getStringExtra(USERNAME);
        SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        String[] names = accountInfo.getString(USERNAME_KEY, null).split(DELIMITER);
        String oauthToken = accountInfo.getString(TOKEN, null).split(DELIMITER)[Arrays.asList(names).indexOf(userName)];
        Gson gson = new Gson();
        OAuthToken token = gson.fromJson(oauthToken, OAuthToken.class);
        FanfouAPI api = new FanfouAPI();
        api.setAccessToken(token);
        api.updateURL(FRIENDlIST_URL);
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




