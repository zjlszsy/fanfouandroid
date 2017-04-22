package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wendyzhou on 4/12/2017.
 */

public class NewStatusActivity extends Activity {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String POSTURL = "http://api.fanfou.com/statuses/update.xml";
    public static final String FRIENDLIST = "Friend List";
    public static final Character TOKENIZER = '@';


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.newstatus);
        final MultiAutoCompleteTextView inputEditText = (MultiAutoCompleteTextView) findViewById(R.id.newStatusText);
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
                if (i < 1 || charSequence.charAt(i - 1) != TOKENIZER) {
                    return i;
                }
                return i;
            }

            @Override
            public int findTokenEnd(CharSequence charSequence, int i) {
                int len = charSequence.length();
                while (i < len) {
                    if (charSequence.charAt(i) == ' ') {
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

                while (i > 0 && charSequence.charAt(i - 1) == ' ') {
                    i--;
                }
                if (i > 0 && charSequence.charAt(i - 1) == ' ') {
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
        //to do
    }
}
