package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Log;
import org.oauthsimple.model.OAuthToken;
import java.io.IOException;

/**
 * Created by wendyzhou on 5/1/2017.
 */

public class OauthTokenRequest extends AsyncTask<Void, Void, FanfouAPI> {
    public String mUsernameInput;
    public String mPasswordInput;

    protected FanfouAPI doInBackground(Void ... url) {
        try {
            FanfouAPI api = new FanfouAPI();
            OAuthToken token = api.getOAuthAccessToken(mUsernameInput, mPasswordInput);
            api.setAccessToken(token);
            return api;
        } catch (IOException e) {
            Log.e("IO expection", "Isue");
        }
        return null;
    }
}