package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.wentingzhou.android.fanfouclient.DisplayTimelineActivity.USERNAME;

/**
 * Created by wendyzhou on 3/24/2017.
 */

class HttpRequest extends AsyncTask<String, Void, String> {
    private Exception exception;
    public String mUsernameInput;
    public String mPasswordInput;

    protected String doInBackground(String... url) {
        try {
            URL timelineAPI = new URL(url[0]);
            URLConnection timelineConnection = timelineAPI.openConnection();

            String userpass = mUsernameInput+ ":" + mPasswordInput;
            String basicAuth = "Basic " + new String(Base64.encode(userpass.getBytes(), Base64.NO_WRAP));


            timelineConnection.setRequestProperty("Authorization", basicAuth);
            InputStream in = timelineConnection.getInputStream();


            BufferedReader inputReader = new BufferedReader(new InputStreamReader(in));

            String inputLine;
            while ((inputLine = inputReader.readLine()) != null)
                Log.e("current line", inputLine);
            inputReader.close();
            return null;
        }
        catch (Exception e) {
            this.exception = e;
            return null;
        }
    }


    protected void onPostExecute(String feed) {
    }
}
