package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.oauthsimple.model.OAuthToken;

import java.util.Arrays;


/**
 * Created by wendyzhou on 3/23/2017.
 */

public class LoginPageActivity extends Activity {
    private EditText mUser;
    private EditText mPassword;
    private static final String USERNAME_KEY = "username";
    private static final String USER_DETAIL = "userDetails";
    private static final String DELIMITER = "\0";
    private static final String TOKEN = "accessToken";
    private ProgressBar loginProgress;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        mUser = (EditText) findViewById(R.id.username);
        mUser.setHint(R.string.input_Username);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword.setHint(R.string.input_Password);
        ListView accounts = (ListView) findViewById(R.id.accountList);
        loginProgress = (ProgressBar) findViewById(R.id.progressBar);

        accounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
                String oauthToken = accountInfo.getString(TOKEN, null).split(DELIMITER)[i];
                Gson gson = new Gson();
                OAuthToken token = gson.fromJson(oauthToken, OAuthToken.class);
                FanfouAPI api = new FanfouAPI();
                api.setAccessToken(token);
                Intent timeline = new Intent(LoginPageActivity.this, DisplayTimelineActivity.class);
                timeline.putExtra(DisplayTimelineActivity.API, api);
                startActivity(timeline);
                loginProgress.setVisibility(View.VISIBLE);
            }
        });
        SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        if (!accountInfo.contains(USERNAME_KEY)) {
            accounts.setVisibility(View.GONE);
        } else {
            String[] usernames = accountInfo.getString(USERNAME_KEY, null).split(DELIMITER);
            accounts.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernames));
        }
    }

    public void toLogin(View v) {
        OauthTokenRequest tokenRequest = new OauthTokenRequest();
        String currentUsername = mUser.getText().toString();
        String currentPassword = mPassword.getText().toString();
        tokenRequest.mUsernameInput = currentUsername;
        tokenRequest.mPasswordInput = currentPassword;
        tokenRequest.context = findViewById(R.id.loginButton).getContext();
        try {
            tokenRequest.execute();
        } catch (Exception e) {
            Log.e("IO exception", "issue", e);
        }
        loginProgress.setVisibility(View.VISIBLE);
    }

    public void deleteAccounts(View v) {
        SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = accountInfo.edit();
        edit.clear();
        edit.apply();
        ListView accounts = (ListView) findViewById(R.id.accountList);
        accounts.setVisibility(View.GONE);
    }
}
