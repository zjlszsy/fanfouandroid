package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by wendyzhou on 4/21/2017.
 */

public class FriendListRequest extends AsyncTask<String, Void, ArrayList<String>> {
    private Exception exception;
    public String mUsernameInput;
    public String mPasswordInput;

    protected ArrayList<String> doInBackground(String... url) {
        try {
            URL timelineAPI = new URL(url[0]);
            URLConnection timelineConnection = timelineAPI.openConnection();
            String userpass = String.format(Locale.US, "%s:%s", mUsernameInput, mPasswordInput);
            String basicAuth = String.format(Locale.US, "Basic %s", new String(Base64.encode(userpass.getBytes(), Base64.NO_WRAP)));
            timelineConnection.setRequestProperty("Authorization", basicAuth);
            InputStream in = timelineConnection.getInputStream();
            MentionListParser friendListParser = new MentionListParser();
            return friendListParser.parse(in);
        } catch (Exception e) {
            this.exception = e;
            Log.e("Exception", "detail", e);
            return null;
        }
    }

    protected void onPostExecute(ArrayList<String> friendList) {
    }
}
