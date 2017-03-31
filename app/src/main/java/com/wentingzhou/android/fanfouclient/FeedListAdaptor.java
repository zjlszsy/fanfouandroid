package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
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


    public FeedListAdaptor(Activity context, FanfouStatus[] statusList) {
        super(context, R.layout.feedlist, statusList);
        this.context = context;
        this.statusList = statusList;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.feedlist, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(statusList[position].userinfo.userNickName);
        Glide.with(context)
                .load(statusList[position].userinfo.profileImageLink)
                .into(imageView);
        extratxt.setText(statusList[position].text);
        return rowView;
    };
}
