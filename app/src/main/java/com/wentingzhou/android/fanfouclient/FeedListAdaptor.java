package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

/**
 * Created by wendyzhou on 3/29/2017.
 */

public class FeedListAdaptor extends ArrayAdapter<FanfouStatus> {

    private final Activity context;
    private final FanfouStatus[] statusList;
    private final String mUsername;
    private final String mPassword;
    private final String USERTIMELINEURL = "http://api.fanfou.com/statuses/user_timeline.xml?id=";


    public FeedListAdaptor(Activity context, FanfouStatus[] statusList, String username, String password) {
        super(context, R.layout.feedlist, statusList);
        this.context = context;
        this.statusList = statusList;
        this.mUsername = username;
        this.mPassword = password;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.feedlist, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);


        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(view.getContext(), UserTimelineActivity.class);
                intent.putExtra(UserTimelineActivity.USERTIMELINEURL,
                        USERTIMELINEURL + statusList[position].userinfo.userID);
                intent.putExtra(UserTimelineActivity.USERNAME, mUsername);
                intent.putExtra(UserTimelineActivity.PASSWORD, mPassword);
                view.getContext().startActivity(intent);
            }
        });

        txtTitle.setText(statusList[position].userinfo.userNickName);
        Glide.with(context)
                .load(statusList[position].userinfo.profileImageLink)
                .into(imageView);
        extratxt.setText(statusList[position].text);
        return rowView;
    };
}
