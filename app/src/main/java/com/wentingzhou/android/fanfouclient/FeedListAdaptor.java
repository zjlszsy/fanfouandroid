package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
    private FanfouAPI api;


    public FeedListAdaptor(Activity context, List<FanfouStatus> statusList, FanfouAPI api) {
        this.context = context;
        this.statusList = statusList;
        this.api = api;

    }

    @Override
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
                intent.putExtra(UserTimelineActivity.user_id,
                        statusList.get(position).userinfo.userID);
                Log.e("pass ID", statusList.get(position).userinfo.userID);
                intent.putExtra(UserTimelineActivity.API, api);
                view.getContext().startActivity(intent);
            }
        });

        txtTitle.setText(statusList.get(position).userinfo.userNickName);
        Glide.with(context)
                .load(statusList.get(position).userinfo.profileImageLink)
                .into(imageView);
        extratxt.setText(statusList.get(position).text);
        return rowView;
    };

    @Override
    public int getCount() {
        return statusList.size();

    }

    @Override
    public FanfouStatus getItem(int position) {
        return statusList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
