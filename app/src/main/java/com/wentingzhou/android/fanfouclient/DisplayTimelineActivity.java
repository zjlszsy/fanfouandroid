package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import java.util.List;


public class DisplayTimelineActivity extends Activity {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public final String FAKEURL = "http://api.fanfou.com/statuses/friends_timeline.xml";
    public final String MOREURL = "http://api.fanfou.com/statuses/friends_timeline.xml?max_id=";

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
        final FanfouStatus[] statusArray = statusList.toArray(new FanfouStatus[statusList.size()]);
        final FeedListAdaptor adaptor = new FeedListAdaptor(this, statusArray, userName, passWord);
        ListView feedList = (ListView) findViewById(R.id.list);
        feedList.setAdapter(adaptor);
        feedList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if(lastInScreen == totalItemCount) {
                    HttpRequest request = new HttpRequest();
                    String userName = getIntent().getStringExtra(USERNAME);
                    request.mUsernameInput = userName;

                    String passWord = getIntent().getStringExtra(PASSWORD);
                    request.mPasswordInput = passWord;

                    List<FanfouStatus> newStatusList = null;
                    try {
                        newStatusList = request.execute(MOREURL + statusArray[statusArray.length-1].statusID).get();
                        Log.e("Getting URL", MOREURL + statusArray[statusArray.length-1].statusID);
                    } catch (Exception e){
                        Log.e("Exception", "detail", e);
                    }
                    for (int i = 0; i < statusArray.length; i++) {
                        statusArray[i] = newStatusList.get(i);
                    }
                    adaptor.notifyDataSetChanged();
                }
            }
        });

    }

}



