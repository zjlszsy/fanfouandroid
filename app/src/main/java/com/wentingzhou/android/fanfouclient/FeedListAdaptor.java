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
    private final StatusListProvider provider;
    private FanfouAPI api;


    public FeedListAdaptor(Activity context, StatusListProvider provider, FanfouAPI api) {
        this.context = context;
        this.provider = provider;
        this.api = api;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if (position == provider.size()) {
            View rowView = inflater.inflate(R.layout.feedlistprogressbar, null, true);
            return rowView;
        }
        View rowView = inflater.inflate(R.layout.single_status_view, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);


        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(view.getContext(), UserTimelineActivity.class);
                intent.putExtra(UserTimelineActivity.user_id,
                        provider.get(position).userinfo.userID);
                intent.putExtra(UserTimelineActivity.API, api);
                view.getContext().startActivity(intent);
            }
        });


        txtTitle.setText(provider.get(position).userinfo.userNickName);
        Glide.with(context)
                .load(provider.get(position).userinfo.profileImageLink)
                .into(imageView);
        extratxt.setText(provider.get(position).text);
        if (provider.get(position).photo_URL != null) {
            final String photoLink = provider.get(position).photo_URL;
            final String photoLargeLink = provider.get(position).photo_large_URL;
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
        return provider.size() + 1;
    }

    @Override
    public FanfouStatus getItem(int position) {
        if (position < provider.size()) {
            return provider.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
