package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import java.util.List;


public class DisplayTimelineActivity extends Activity {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FAKEURL = "http://api.fanfou.com/statuses/friends_timeline.xml";

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
        FeedListAdaptor adaptor = new FeedListAdaptor(this, statusList, userName, passWord);
        ListView feedList = (ListView) findViewById(R.id.list);
        feedList.setAdapter(adaptor);
    }

    public void postNewStatus(View v) {
        Intent newStatus = new Intent(this, NewStatusActivity.class);
        newStatus.putExtra(NewStatusActivity.USERNAME, getIntent().getStringExtra(USERNAME));
        newStatus.putExtra(NewStatusActivity.PASSWORD, getIntent().getStringExtra(PASSWORD));
        startActivity(newStatus);
    }
}



