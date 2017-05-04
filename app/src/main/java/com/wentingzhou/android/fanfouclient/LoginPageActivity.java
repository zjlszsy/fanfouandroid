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

import com.google.gson.Gson;

import org.oauthsimple.model.OAuthToken;

import java.io.IOException;


/**
 * Created by wendyzhou on 3/23/2017.
 */

public class LoginPageActivity extends Activity {
    private EditText mUser;
    private EditText mPassword;
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String USER_DETAIL = "userDetails";
    private static final String DELIMITER = "\0";
    private static final String TOKEN = "accessToken";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        mUser = (EditText) findViewById(R.id.username);
        mUser.setHint(R.string.input_Username);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword.setHint(R.string.input_Password);
        ListView accounts = (ListView) findViewById(R.id.accountList);
        SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);

        accounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
                String name = accountInfo.getString(USERNAME_KEY, null).split(DELIMITER)[i];
                Intent timeline = new Intent(LoginPageActivity.this, DisplayTimelineActivity.class);
                timeline.putExtra(DisplayTimelineActivity.USERNAME, name);
                startActivity(timeline);
            }
        });

        if (accountInfo.getString(USERNAME_KEY, null) == null) {
            accounts.setVisibility(View.GONE);
        } else {
            String[] usernames = accountInfo.getString(USERNAME_KEY, null).split(DELIMITER);
            accounts.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernames));
        }
    }

    public void toLogin(View v) {
        SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = accountInfo.edit();
        String userAccountName = accountInfo.getString(USERNAME_KEY, null);
        String userAccountPassword = accountInfo.getString(PASSWORD_KEY, null);
        String userAPI = accountInfo.getString(TOKEN, null);

        try {
            OauthTokenRequest tokenRequest = new OauthTokenRequest();
            String currentUsername = mUser.getText().toString();
            String currentPassword = mPassword.getText().toString();
            tokenRequest.mUsernameInput = currentUsername;
            tokenRequest.mPasswordInput = currentPassword;
            FanfouAPI resultAPI = tokenRequest.execute().get();
            Gson gson = new Gson();
            String apiJson = gson.toJson(resultAPI.getAccessToken());
            if (accountInfo.getString(USERNAME_KEY, null) == null) {
                userAccountName =  currentUsername;
                userAccountPassword = currentPassword;
                userAPI = apiJson;
            } else {
                userAccountName = userAccountName + DELIMITER + currentUsername;
                userAccountPassword = userAccountPassword + DELIMITER + currentPassword;
                userAPI = userAPI + DELIMITER + apiJson;
            }
            edit.putString(USERNAME_KEY, userAccountName);
            edit.putString(PASSWORD_KEY, userAccountPassword);
            edit.putString(TOKEN, userAPI);
            edit.commit();
            Intent timeline = new Intent(this, DisplayTimelineActivity.class);
            timeline.putExtra(DisplayTimelineActivity.USERNAME, currentUsername);
            startActivity(timeline);
        } catch (Exception e) {
            Log.e("IO exception", "Issue");
        }
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
