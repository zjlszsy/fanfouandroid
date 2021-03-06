package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class DisplayTimelineActivity extends Activity implements OnLoadMoreTimelineTaskCompleted{
    public final int STATUS_REMAINING = 5;
    public static final String API = "userFanfouAPI";
    private static HashSet<String> lastMsgIds;
    static final int POST_NEW_STATUS_REQUEST = 2;  // The request code
    FeedListAdaptor adaptor;
    List<FanfouStatus> statusListFinal = null;
    FanfouAPI api;
    ListView feedList;
    private SwipeRefreshLayout swipeLayout;
    private StatusListProvider statusListProvider;
    private List<FanfouStatus> returnedNewList;



    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        lastMsgIds = new HashSet<String>();
        api = getIntent().getParcelableExtra(API);
        TimelineRequest request = new TimelineRequest();
        List<FanfouStatus> statusList = null;
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        try {
            statusList = request.execute(api).get();
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }
        statusListFinal = statusList;
        statusListProvider = new StatusListProvider(statusListFinal);
        adaptor = new FeedListAdaptor(this, statusListProvider, api);
        feedList = (ListView) findViewById(R.id.list);

        feedList.setAdapter(adaptor);
        feedList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                String lastMessageID = statusListProvider.getList().get(totalItemCount - 2).statusID;

                if (lastInScreen == totalItemCount - STATUS_REMAINING && !lastMsgIds.contains(lastMessageID)) {
                    lastMsgIds.add(lastMessageID);
                    LoadMoreTimelineIntoFeedlistRequest request = new LoadMoreTimelineIntoFeedlistRequest(DisplayTimelineActivity.this, DisplayTimelineActivity.this);
                    request.setID(statusListProvider.getList().get(totalItemCount - 2).statusID);
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
        TimelineRequest request = new TimelineRequest();
        List<FanfouStatus> statusList = null;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_status:
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
                startActivityForResult(newStatus, POST_NEW_STATUS_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == POST_NEW_STATUS_REQUEST) {
            if (resultCode == RESULT_OK) {
                refreshItems();
            }
        }
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




