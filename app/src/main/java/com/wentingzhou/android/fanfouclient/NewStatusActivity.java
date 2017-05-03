package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.google.gson.Gson;

import org.oauthsimple.model.OAuthToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wendyzhou on 4/12/2017.
 */

public class NewStatusActivity extends Activity {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String POSTURL = "http://api.fanfou.com/statuses/update.xml";
    public static final String FRIENDLIST = "Friend List";
    public static final Character TOKENIZER = '@';
    public static final Character TOKEN_TERMINATOR  = ' ';
    public MultiAutoCompleteTextView inputEditText;
    private static final String DELIMITER = "\0";
    private static final String USERDETAIL = "userDetails";
    private static final String USERNAMEKEY = "username";
    private static final String TOKEN = "accessToken";



    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.newstatus);
        inputEditText = (MultiAutoCompleteTextView) findViewById(R.id.newStatusText);
        ArrayList<String> friendList = getIntent().getStringArrayListExtra(FRIENDLIST);
        String[] friendArray = friendList.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, friendArray);
        inputEditText.setAdapter(adapter);
        inputEditText.setThreshold(1);
        inputEditText.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
            @Override
            public int findTokenStart(CharSequence charSequence, int i) {
                while (i > 0 && charSequence.charAt(i - 1) != TOKENIZER) {
                    i--;
                }
                return i;
            }

            @Override
            public int findTokenEnd(CharSequence charSequence, int i) {
                int len = charSequence.length();
                while (i < len) {
                    if (charSequence.charAt(i) == TOKEN_TERMINATOR) {
                        return i;
                    } else {
                        i++;
                    }
                }
                return len;
            }

            @Override
            public CharSequence terminateToken(CharSequence charSequence) {
                int i = charSequence.length();

                while (i > 0 && charSequence.charAt(i - 1) == TOKEN_TERMINATOR) {
                    i--;
                }
                if (i > 0 && charSequence.charAt(i - 1) == TOKEN_TERMINATOR) {
                    return charSequence;
                } else {
                    if (charSequence instanceof Spanned) {
                        SpannableString sp = new SpannableString(charSequence + " ");
                        TextUtils.copySpansFrom((Spanned) charSequence, 0, charSequence.length(), Object.class, sp, 0);
                        return sp;
                    } else {
                        return charSequence + " ";
                    }
                }
            }
        });

    }

    public void toPost(View v) {
        OauthRequest oauthRequest = new OauthRequest();
        String userName = getIntent().getStringExtra(USERNAME);
        SharedPreferences accountInfo = getSharedPreferences(USERDETAIL, Context.MODE_PRIVATE);
        String[] names = accountInfo.getString(USERNAMEKEY, null).split(DELIMITER);
        String oauthToken = accountInfo.getString(TOKEN, null).split(DELIMITER)[Arrays.asList(names).indexOf(userName)];
        Gson gson = new Gson();
        OAuthToken token = gson.fromJson(oauthToken, OAuthToken.class);
        FanfouAPI api = new FanfouAPI();
        api.setAccessToken(token);
        api.updateURL(POSTURL);

        try {
            oauthRequest.statusText =  inputEditText.getText().toString();
            String result = oauthRequest.execute(api).get();
            Intent newTimeline = new Intent(this, DisplayTimelineActivity.class);
            newTimeline.putExtra(NewStatusActivity.USERNAME, userName);
            startActivity(newTimeline);
        } catch (Exception e) {
            Log.e("Exception", "Issue");
        }
    }
}
