package com.wentingzhou.android.fanfouclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import org.oauthsimple.model.OAuthToken;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wentingzhou.android.fanfouclient.model.FanfouUserInfo;

/**
 * Created by wendyzhou on 5/1/2017.
 */

public class OauthTokenRequest extends AsyncTask<Void, Void, FanfouUserInfo> {
    public String mUsernameInput;
    public String mPasswordInput;
    private static final String USER_DETAIL = "userDetails";
    private static final String USER_INFO = "userinfo";
    public Context context;

    protected FanfouUserInfo doInBackground(Void ... url) {
        try {
            FanfouAPI api = new FanfouAPI();
            OAuthToken token = api.getOAuthAccessToken(mUsernameInput, mPasswordInput);
            api.setAccessToken(token);
            String returnedAccountInfo = api.verifyAccountInfo();
            InputStream stream = new ByteArrayInputStream(returnedAccountInfo.getBytes(StandardCharsets.UTF_8));
            AccountInfoParser accountinfoParser = new AccountInfoParser();
            FanfouUserInfo userInfo = accountinfoParser.parse(stream);
            userInfo.setTokenJson(token);
            Intent timeline = new Intent(context, DisplayTimelineActivity.class);
            timeline.putExtra(DisplayTimelineActivity.API, userInfo.getAPI());
            context.startActivity(timeline);
            return userInfo;
        } catch (Exception e) {
            Log.e("IO expection", "Isue");
        }
        return null;
    }

    @Override
    protected void onPostExecute(FanfouUserInfo userInfo){
        SharedPreferences accountInfo = context.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        String accountsInfoString = accountInfo.getString(USER_INFO, null);
        if (!accountInfo.contains(USER_INFO)) {
            HashMap<String, FanfouUserInfo> userAccountsInfo = new HashMap<String, FanfouUserInfo>();
            userAccountsInfo.put(mUsernameInput, userInfo);
            Gson gson = new Gson();
            accountsInfoString = gson.toJson(userAccountsInfo);
        } else {
            Gson gson = new Gson();
            Type typeOfHashMap = new TypeToken<HashMap<String, FanfouUserInfo>>() { }.getType();
            HashMap<String, FanfouUserInfo> userAccountsInfo = gson.fromJson(accountsInfoString, typeOfHashMap);
            if (!userAccountsInfo.containsKey(mUsernameInput)){
                userAccountsInfo.put(mUsernameInput, userInfo);
                Gson gson2 = new Gson();
                accountsInfoString = gson2.toJson(userAccountsInfo);
            }
        }
        SharedPreferences.Editor edit = accountInfo.edit();
        edit.putString(USER_INFO, accountsInfoString);
        edit.commit();
    }
}