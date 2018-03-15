package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        if (position == statusList.size()) {
            View rowView = inflater.inflate(R.layout.feedlistprogressbar, null, true);
            return rowView;
        }
        View rowView = inflater.inflate(R.layout.feedlist, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);


        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(view.getContext(), UserTimelineActivity.class);
                intent.putExtra(UserTimelineActivity.user_id,
                        statusList.get(position).userinfo.userID);
                intent.putExtra(UserTimelineActivity.API, api);
                view.getContext().startActivity(intent);
            }
        });


        txtTitle.setText(statusList.get(position).userinfo.userNickName);
        Glide.with(context)
                .load(statusList.get(position).userinfo.profileImageLink)
                .into(imageView);
        extratxt.setText(statusList.get(position).text);
        if (statusList.get(position).photo_URL != null) {
            final String photoLink = statusList.get(position).photo_URL;
            final String photoLargeLink = statusList.get(position).photo_large_URL;
            ImageView photoView = (ImageView) rowView.findViewById(R.id.photo);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent viewPhotoIntent = new Intent(context, DisplayPhotoActivity.class);
                    viewPhotoIntent.putExtra("photoLink", photoLargeLink);
                    context.startActivity(viewPhotoIntent);
                }
            });
            photoView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(photoLink)
                    .into(photoView);
        }




        return rowView;
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
