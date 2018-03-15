package com.wentingzhou.android.fanfouclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.oauthsimple.model.OAuthToken;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;

/**
 * Created by wendyzhou on 5/1/2017.
 */

public class OauthTokenRequest extends AsyncTask<Void, Void, FanfouAPI> {
    public String mUsernameInput;
    public String mPasswordInput;
    private static final String USERNAME_KEY = "username";
    private static final String USER_DETAIL = "userDetails";
    private static final String DELIMITER = "\0";
    private static final String TOKEN = "accessToken";
    public Context context;
    private ProgressBar pb;
    private LoginPageActivity activity;


    OauthTokenRequest(ProgressBar pb, LoginPageActivity activity) {
        this.pb = pb;
        this.activity = activity;

    }

    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
    }

    protected FanfouAPI doInBackground(Void ... url) {
        try {
            FanfouAPI api = new FanfouAPI();
            OAuthToken token = api.getOAuthAccessToken(mUsernameInput, mPasswordInput);
            if (token == null) {
                return null;
            }
            api.setAccessToken(token);
            return api;
        } catch (IOException e) {
            Log.e("IO expection", "Isue");
        }
        return null;
    }

    @Override
    protected void onPostExecute(FanfouAPI api){
        if (api == null) {
            pb.setVisibility(View.GONE);
            Toast.makeText(activity, "Invalid Login Information. Please try again.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Gson gson = new Gson();
        String tokenJson = gson.toJson(api.getAccessToken());
        SharedPreferences accountInfo = context.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        String userAccountName = accountInfo.getString(USERNAME_KEY, null);
        String userToken = accountInfo.getString(TOKEN, null);
        if (!accountInfo.contains(USERNAME_KEY)) {
            userAccountName =  mUsernameInput;
            userToken = tokenJson;
        } else if (!Arrays.asList(userAccountName.split(DELIMITER)).contains(mUsernameInput)){
            userAccountName = userAccountName + DELIMITER + mUsernameInput;
            userToken = userToken + DELIMITER + tokenJson;
        }
        SharedPreferences.Editor edit = accountInfo.edit();
        edit.putString(USERNAME_KEY, userAccountName);
        edit.putString(TOKEN, userToken);
        edit.apply();
        Intent timeline = new Intent(context, DisplayTimelineActivity.class);
        timeline.putExtra(DisplayTimelineActivity.API, api);
        context.startActivity(timeline);

    }
}