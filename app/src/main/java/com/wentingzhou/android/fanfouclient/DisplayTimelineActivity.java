package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import java.util.List;


public class DisplayTimelineActivity extends Activity {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FAKEURL = "https://s3-us-west-2.amazonaws.com/superninjawendyzhou/file/home_timeline.xml";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        HttpRequest request = new HttpRequest();
        request.mUsernameInput = getIntent().getStringExtra(USERNAME);
        request.mPasswordInput = getIntent().getStringExtra(PASSWORD);
        List<FanfouStatus> statusList = null;
        try {
            statusList = request.execute(FAKEURL).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        FanfouStatus[] statusArray = new FanfouStatus[statusList.size()];
        statusArray = statusList.toArray(statusArray);
        FeedListAdaptor adaptor = new FeedListAdaptor(this, statusArray);
        ListView feedList = (ListView) findViewById(R.id.list);
        feedList.setAdapter(adaptor);
    }
}



