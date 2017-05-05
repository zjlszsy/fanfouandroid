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

/**
 * Created by wendyzhou on 3/31/2017.
 */

public class UserTimelineActivity extends Activity {
    public static final String user_id = "userID";
    public static final String API = "userFanfouAPI";



    public void onCreate(Bundle currentBundle) {
        super.onCreate(currentBundle);
        setContentView(R.layout.main);
        FanfouAPI api = getIntent().getParcelableExtra(API);
        String id = getIntent().getStringExtra(user_id);
        UserTimelineRequest request = new UserTimelineRequest();
        request.setID(id);
        List<FanfouStatus> statusList = new ArrayList<FanfouStatus>();
        try {
            statusList = request.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        FeedListAdaptor adaptor = new FeedListAdaptor(this, statusList, api);
        ListView feedList = (ListView) findViewById(R.id.list);
        feedList.setAdapter(adaptor);
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
        newStatus.putExtra(NewStatusActivity.FRIENDLIST, friendList);
        newStatus.putExtra(NewStatusActivity.API, api);
        startActivity(newStatus);
    }


}
