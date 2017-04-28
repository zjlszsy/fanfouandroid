package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Log;
import org.oauthsimple.model.OAuthToken;
import java.io.IOException;


/**
 * Created by wendyzhou on 4/26/2017.
 */

public class OauthRequest extends AsyncTask<String, Void, String> {
    public String mUsernameInput;
    public String mPasswordInput;

    protected String doInBackground(String... url) {
        try {
            FanfouAPI api = new FanfouAPI();
            OAuthToken token = api.getOAuthAccessToken(mUsernameInput, mPasswordInput);
            api.setAccessToken(token);
            return api.fetchTimeline(url[0]);
        } catch (IOException e) {
            Log.e("IO expection", "Isue");
        }
        return null;
    }
}
