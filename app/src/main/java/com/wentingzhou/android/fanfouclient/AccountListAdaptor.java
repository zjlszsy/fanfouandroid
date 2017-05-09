package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wentingzhou.android.fanfouclient.model.FanfouUserInfo;

import java.util.List;

/**
 * Created by wendyzhou on 5/8/2017.
 */

public class AccountListAdaptor extends BaseAdapter {
    private final Activity context;
    private List<FanfouUserInfo> userInfoList;

    public AccountListAdaptor(Activity context, List<FanfouUserInfo> userInfoList) {
        this.context = context;
        this.userInfoList = userInfoList;
    }

    @Override
    public int getCount() {
        return userInfoList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.accountlist, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.userImage);
        TextView usernameText = (TextView) rowView.findViewById(R.id.username);

        usernameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(view.getContext(), DisplayTimelineActivity.class);
                intent.putExtra(UserTimelineActivity.API, userInfoList.get(position).getAPI());
                view.getContext().startActivity(intent);
                ProgressBar loginProgress = (ProgressBar) context.findViewById(R.id.progressBar);
                loginProgress.setVisibility(View.VISIBLE);
            }
        });

        Glide.with(context)
            .load(userInfoList.get(position).profileImageLink)
            .into(imageView);
        usernameText.setText(userInfoList.get(position).userNickName);
        return rowView;
    }

    @Override
    public FanfouUserInfo getItem(int position) {
        return userInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
