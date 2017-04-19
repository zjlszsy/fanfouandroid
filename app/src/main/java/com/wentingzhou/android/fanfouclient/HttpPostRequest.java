package com.wentingzhou.android.fanfouclient;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

/**
 * Created by wendyzhou on 4/12/2017.
 */

public class HttpPostRequest extends AsyncTask<String, Void, String> {
    private Exception exception;
    public String mUsernameInput;
    public String mPasswordInput;
    public String POST_PARAMS;

    protected String doInBackground(String... url) {
        try {
            Log.e("Username is !!", mUsernameInput);
            Log.e("Password is !!", mPasswordInput);
            Log.e("Posting text !!", POST_PARAMS);
            URL postAPI = new URL(url[0]);
            HttpURLConnection postConnection = (HttpURLConnection) postAPI.openConnection();

            String userpass = String.format(Locale.US, "%s:%s", mUsernameInput, mPasswordInput);
            String basicAuth = String.format(Locale.US, "Basic %s", new String(Base64.encode(userpass.getBytes(), Base64.NO_WRAP)));
            postConnection.setRequestProperty("Authorization", basicAuth);

            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String postText = "status=" + POST_PARAMS;
            byte[] postDataBytes = postText.getBytes("UTF-8");
            postConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            postConnection.setDoOutput(true);
            postConnection.getOutputStream().write(postDataBytes);



//            String postText = "status=" + POST_PARAMS;
//            OutputStream os = postConnection.getOutputStream();
//            os.write(postText.getBytes());
//            os.flush();

//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            writer.write(postText);
//            writer.flush();
//            writer.close();
//            os.close();
//            postConnection.connect();


            int responseCode = postConnection.getResponseCode();
            String returnCode = Integer.toString(responseCode);
            Log.e("POST Response Code!!", returnCode);
            return returnCode;
        } catch (Exception e) {
            this.exception = e;
            Log.e("Exception", "detail", e);
            return null;
        }
    }

    protected void onPostExecute(String s) {
    }
}
