package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import org.oauthsimple.model.OAuthToken;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import com.wentingzhou.android.fanfouclient.model.FanfouUserInfo;

/**
 * Created by wendyzhou on 5/1/2017.
 */

public class OauthTokenRequest extends AsyncTask<Void, Void, FanfouUserInfo> {
    public ProgressBar pb;
    private OnOauthTaskCompleted task;
    private OauthRequestProvider provider;

    OauthTokenRequest(OnOauthTaskCompleted task, OauthRequestProvider provider) {
        this.task = task;
        this.provider = provider;
    }

    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
    }


    protected FanfouUserInfo doInBackground(Void ... url) {
        try {
            FanfouAPI api = new FanfouAPI();
            OAuthToken token = api.getOAuthAccessToken(provider.getCurrentUsername(), provider.getCurrentPassword());
            if (token == null) {
                return null;
            }
            api.setAccessToken(token);
            String returnedAccountInfo = api.verifyAccountInfo();
            InputStream stream = new ByteArrayInputStream(returnedAccountInfo.getBytes(StandardCharsets.UTF_8));
            AccountInfoParser accountinfoParser = new AccountInfoParser();
            FanfouUserInfo userInfo = accountinfoParser.parse(stream);
            userInfo.setTokenJson(token);
            return userInfo;
        } catch (Exception e) {
            Log.e("IO expection", "Isue");
        }
        return null;
    }

    @Override
    protected void onPostExecute(FanfouUserInfo userInfo){
        pb.setVisibility(View.GONE);
        if (userInfo == null) {
            task.onTaskCompleted(provider.getRequestFailed());
            return;
        }
        task.setUserInfo(userInfo);
        task.onTaskCompleted(provider.getRequestSucceeded());
    }
}