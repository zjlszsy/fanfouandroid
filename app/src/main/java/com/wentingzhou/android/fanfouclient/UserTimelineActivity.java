package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by wendyzhou on 3/31/2017.
 */

public class UserTimelineActivity extends Activity implements OnLoadMoreTimelineTaskCompleted{
    public static final String user_id = "userID";
    public static final String API = "userFanfouAPI";
    private static HashSet<String> lastMsgIds;
    public FeedListAdaptor adaptor;
    private List<FanfouStatus> returnedNewList;
    private StatusListProvider statusListProvider;
    private FanfouAPI api;
    private String userID;
    private SwipeRefreshLayout swipeLayout;


    public void onCreate(Bundle currentBundle) {
        super.onCreate(currentBundle);
        setContentView(R.layout.main);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        lastMsgIds = new HashSet<String>();
        api = getIntent().getParcelableExtra(API);
        userID = getIntent().getStringExtra(user_id);
        UserTimelineRequest request = new UserTimelineRequest();
        request.setID(userID);
        List<FanfouStatus> statusList = new ArrayList<FanfouStatus>();
        try {
            statusList = request.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        List<FanfouStatus>  statusListFinal = statusList;
        statusListProvider = new StatusListProvider(statusListFinal);
        adaptor = new FeedListAdaptor(this, statusListProvider, api);
        ListView feedList = (ListView) findViewById(R.id.list);
        feedList.setAdapter(adaptor);
        feedList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                String lastMessageID = statusListProvider.getList().get(totalItemCount - 2).statusID;

                if (lastInScreen == totalItemCount - 5 && !lastMsgIds.contains(lastMessageID)) {
                    lastMsgIds.add(lastMessageID);
                    LoadMoreUserTimelineRequest request = new LoadMoreUserTimelineRequest(UserTimelineActivity.this, UserTimelineActivity.this);
                    request.setMessageID(statusListProvider.getList().get(totalItemCount - 2).statusID);
                    request.setUserID(userID);

                    try {
                        request.execute(api);
                    } catch (Exception e){
                        Log.e("Exception", "detail", e);
                    }

                }
            }
        });
    }

    public void refreshItems() {
        UserTimelineRequest request = new UserTimelineRequest();
        request.setID(userID);
        List<FanfouStatus> statusList = new ArrayList<FanfouStatus>();
        try {
            statusList = request.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        onItemsLoadComplete(statusList);
    }

    void onItemsLoadComplete(List<FanfouStatus> statusList) {
        statusListProvider.setStatusList(statusList);
        adaptor.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onTaskCompleted(String response) {
        if (response.equals(getResources().getString(R.string.requestFailed))){
            Toast.makeText(this, "Something went wrong. Please try again later.",
                    Toast.LENGTH_LONG).show();
        } else if (response.equals(getResources().getString(R.string.requestSucceeded))) {
            statusListProvider.appendList(returnedNewList);
            adaptor.notifyDataSetChanged();
        }

    }

    public void updateReturnedNewList(List<FanfouStatus> list) {
        this.returnedNewList = list;
    }
}
