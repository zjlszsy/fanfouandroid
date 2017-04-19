package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by wendyzhou on 4/12/2017.
 */

public class NewStatusActivity extends Activity {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String POSTURL = "http://api.fanfou.com/statuses/update.xml";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.newstatus);
        EditText statusText = (EditText) findViewById(R.id.newStatusText);
        statusText.setHint(R.string.write_new_status);
    }

    public void toPost(View v) {
        EditText statusText = (EditText) findViewById(R.id.newStatusText);
        String statusContent = statusText.getText().toString();

        HttpPostRequest postRequest = new HttpPostRequest();
        String userName = getIntent().getStringExtra(USERNAME);
        postRequest.mUsernameInput = userName;
        String passWord = getIntent().getStringExtra(PASSWORD);
        postRequest.mPasswordInput = passWord;

        postRequest.POST_PARAMS = statusContent;

        try {
            String postStatusCode = postRequest.execute(POSTURL).get();
            Log.e("Post code!!", postStatusCode);
        } catch (Exception e){
            Log.e("Exception", "detail", e);
        }

    }
}
