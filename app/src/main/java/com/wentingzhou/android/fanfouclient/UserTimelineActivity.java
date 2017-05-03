package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;

import com.google.gson.Gson;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import org.oauthsimple.model.OAuthToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wendyzhou on 3/31/2017.
 */

public class UserTimelineActivity extends Activity {
    public static final String USERTIMELINEURL = "URL";
    public static final String USERNAME = "username";
    private static final String DELIMITER = "\0";
    private static final String USERDETAIL = "userDetails";
    private static final String USERNAMEKEY = "username";
    private static final String TOKEN = "accessToken";
    public final String FRIENDlISTURL = "http://api.fanfou.com/users/friends.xml";



    public void onCreate(Bundle currentBundle) {
        super.onCreate(currentBundle);
        setContentView(R.layout.main);
        String userName = getIntent().getStringExtra(USERNAME);
        String url = getIntent().getStringExtra(USERTIMELINEURL);
        SharedPreferences accountInfo = getSharedPreferences(USERDETAIL, Context.MODE_PRIVATE);
        String[] names = accountInfo.getString(USERNAMEKEY, null).split(DELIMITER);
        String oauthToken = accountInfo.getString(TOKEN, null).split(DELIMITER)[Arrays.asList(names).indexOf(userName)];
        Gson gson = new Gson();
        OAuthToken token = gson.fromJson(oauthToken, OAuthToken.class);
        FanfouAPI api = new FanfouAPI();
        api.setAccessToken(token);
        api.updateURL(url);
        TimelineRequest request = new TimelineRequest();
        List<FanfouStatus> statusList = new ArrayList<FanfouStatus>();
        try {
            statusList = request.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        FeedListAdaptor adaptor = new FeedListAdaptor(this, statusList, userName);
        ListView feedList = (ListView) findViewById(R.id.list);
        feedList.setAdapter(adaptor);
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
