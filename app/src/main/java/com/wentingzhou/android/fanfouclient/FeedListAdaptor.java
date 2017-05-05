package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import java.util.List;

/**
 * Created by wendyzhou on 3/29/2017.
 */

public class FeedListAdaptor extends BaseAdapter {

    private final Activity context;
    private final List<FanfouStatus> statusList;
    private final String mUsername;
    private final String USERTIMELINE_URL = "http://api.fanfou.com/statuses/user_timeline.xml?id=";


    public FeedListAdaptor(Activity context, List<FanfouStatus> statusList, String username) {
        this.context = context;
        this.statusList = statusList;
        this.mUsername = username;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if (position < statusList.size()) {
            View rowView = inflater.inflate(R.layout.feedlist, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);


            txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), UserTimelineActivity.class);
                    intent.putExtra(UserTimelineActivity.USERTIMELINEURL,
                            USERTIMELINE_URL + statusList.get(position).userinfo.userID);
                    intent.putExtra(UserTimelineActivity.USERNAME, mUsername);
                    view.getContext().startActivity(intent);
                }
            });

            txtTitle.setText(statusList.get(position).userinfo.userNickName);
            Glide.with(context)
                    .load(statusList.get(position).userinfo.profileImageLink)
                    .into(imageView);
            extratxt.setText(statusList.get(position).text);
            return rowView;
        } else {
            View rowView = inflater.inflate(R.layout.feedlistprogressbar, null, true);
            return rowView;
        }
    };

    @Override
    public int getCount() {
        return statusList.size() + 1;
    }

    @Override
    public FanfouStatus getItem(int position) {
        if (position < statusList.size()) {
            return statusList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
