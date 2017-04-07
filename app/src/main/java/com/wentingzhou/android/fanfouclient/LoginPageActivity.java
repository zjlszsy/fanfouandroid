package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Created by wendyzhou on 3/23/2017.
 */

public class LoginPageActivity extends Activity {
    private EditText mUser;
    private EditText mPassword;
    private static final String[] items={"Rock", "Paper", "scissors"};



    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        mUser = (EditText) findViewById(R.id.username);
        mUser.setHint(R.string.input_Username);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword.setHint(R.string.input_Password);
        ListView accounts = (ListView) findViewById(R.id.accountList);
        SharedPreferences accountInfo = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = accountInfo.edit();
        String userAccountName = accountInfo.getString("username", null);
        Log.e("username",userAccountName);
        edit.clear();
        edit.commit();
        if (userAccountName == null) {
            accounts.setVisibility(View.GONE);
        } else {
            accounts.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
        }




    }

    public void toLogin(View v) {
        Intent timeline = new Intent(this, DisplayTimelineActivity.class);
        timeline.putExtra(DisplayTimelineActivity.USERNAME, mUser.getText().toString());
        timeline.putExtra(DisplayTimelineActivity.PASSWORD, mPassword.getText().toString());

        SharedPreferences accountInfo = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = accountInfo.edit();
        String userAccountName = accountInfo.getString("username", null);
        String userAccountPassword = accountInfo.getString("username", null);
        edit.clear();
        edit.commit();
        if (userAccountName == null) {
            userAccountName =  mUser.getText().toString();
            userAccountPassword = mPassword.getText().toString();
        } else {
            userAccountName = userAccountName + "\0" + mUser.getText().toString();
            userAccountPassword = userAccountPassword + "\0" + mPassword.getText().toString();
        }
        edit.putString("username", userAccountName);
        edit.putString("password", userAccountPassword);
        edit.apply();

        Log.e("Username!!!", userAccountName);
        Log.e("Password!!!", userAccountPassword);
        startActivity(timeline);
    }
}
