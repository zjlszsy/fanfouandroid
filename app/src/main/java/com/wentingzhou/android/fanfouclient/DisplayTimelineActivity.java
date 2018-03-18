package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import java.util.ArrayList;
import java.util.List;


public class DisplayTimelineActivity extends Activity {
    public static final String API = "userFanfouAPI";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        final FanfouAPI api = getIntent().getParcelableExtra(API);
        List<FanfouStatus> statusList = new ArrayList<FanfouStatus>();
        final FeedListAdaptor adaptor = new FeedListAdaptor(this, statusList, api);
        ListView feedList = (ListView) findViewById(R.id.list);
        feedList.setAdapter(adaptor);
        TimelineRequest request = new TimelineRequest();
        request.setActivity(this);
        try {
            request.execute(api);
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
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




