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





/**
 * Created by wendyzhou on 3/23/2017.
 */

public class LoginPageActivity extends Activity {
    private EditText mUser;
    private EditText mPassword;

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

        accounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences accountInfo = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                String name = accountInfo.getString("username", null).split("\0")[i];
                String pass = accountInfo.getString("password", null).split("\0")[i];
                Intent timeline = new Intent(LoginPageActivity.this, DisplayTimelineActivity.class);
                timeline.putExtra(DisplayTimelineActivity.USERNAME, name);
                timeline.putExtra(DisplayTimelineActivity.PASSWORD, pass);
                startActivity(timeline);
            }
        });

        if (accountInfo.getString("username", null) == null) {
            accounts.setVisibility(View.GONE);
        } else {
            String[] usernames = accountInfo.getString("username", null).split("\0");
            accounts.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernames));
        }
    }

    public void toLogin(View v) {
        Intent timeline = new Intent(this, DisplayTimelineActivity.class);
        timeline.putExtra(DisplayTimelineActivity.USERNAME, mUser.getText().toString());
        timeline.putExtra(DisplayTimelineActivity.PASSWORD, mPassword.getText().toString());

        SharedPreferences accountInfo = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = accountInfo.edit();
        String userAccountName = accountInfo.getString("username", null);
        String userAccountPassword = accountInfo.getString("password", null);
        if (accountInfo.getString("username", null) == null) {
            userAccountName =  mUser.getText().toString();
            userAccountPassword = mPassword.getText().toString();
        } else {
            userAccountName = userAccountName + "\0" + mUser.getText().toString();
            userAccountPassword = userAccountPassword + "\0" + mPassword.getText().toString();
        }
        edit.putString("username", userAccountName);
        edit.putString("password", userAccountPassword);
        edit.commit();
        startActivity(timeline);
    }

    public void deleteAccounts(View v) {
        SharedPreferences accountInfo = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = accountInfo.edit();
        edit.clear();
        edit.apply();
        ListView accounts = (ListView) findViewById(R.id.accountList);
        accounts.setVisibility(View.GONE);
    }

}
