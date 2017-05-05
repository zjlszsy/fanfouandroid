package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by wendyzhou on 4/21/2017.
 */

public class FriendListRequest extends AsyncTask<FanfouAPI, Void, ArrayList<String>> {
    private Exception exception;

    protected ArrayList<String> doInBackground(FanfouAPI ... api) {
        try {
            String result = api[0].fetchFriendsList();
            InputStream stream = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
            MentionListParser friendListParser = new MentionListParser();
            return friendListParser.parse(stream);
        } catch (Exception e) {
            this.exception = e;
            Log.e("Exception", "detail", e);
            return null;
        }
    }

    protected void onPostExecute(ArrayList<String> friendList) {
    }
}
