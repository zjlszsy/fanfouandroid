package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wendyzhou on 3/31/2017.
 */

public class UserTimelineActivity extends Activity {
    public static final String USERTIMELINEURL = "URL";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public void onCreate(Bundle currentBundle) {
        super.onCreate(currentBundle);
        setContentView(R.layout.main);
        HttpRequest request = new HttpRequest();
        String url = getIntent().getStringExtra(USERTIMELINEURL);
        request.mURL = url;

        String userName = getIntent().getStringExtra(USERNAME);
        request.mUsernameInput = userName;

        String passWord = getIntent().getStringExtra(PASSWORD);
        request.mPasswordInput = passWord;

        List<FanfouStatus> statusList = new ArrayList<FanfouStatus>();
        try {
            statusList = request.execute(url).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        FanfouStatus[] statusArray = new FanfouStatus[statusList.size()];
        statusArray = statusList.toArray(statusArray);
        FeedListAdaptor adaptor = new FeedListAdaptor(this, statusArray, userName, passWord);
        ListView feedList = (ListView) findViewById(R.id.list);
        feedList.setAdapter(adaptor);
    }
}
