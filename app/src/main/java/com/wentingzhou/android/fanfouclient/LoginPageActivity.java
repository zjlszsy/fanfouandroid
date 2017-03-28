package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


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
    }

    public void toLogin(View v) {
        Intent timeline = new Intent(this, DisplayTimelineActivity.class);
        timeline.putExtra(DisplayTimelineActivity.USERNAME, mUser.getText().toString());
        timeline.putExtra(DisplayTimelineActivity.PASSWORD, mPassword.getText().toString());
        startActivity(timeline);
    }
}
