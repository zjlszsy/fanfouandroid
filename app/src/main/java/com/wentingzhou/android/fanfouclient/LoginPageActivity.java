package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wentingzhou.android.fanfouclient.model.FanfouUserInfo;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by wendyzhou on 3/23/2017.
 */

public class LoginPageActivity extends Activity implements AdapterView.OnItemClickListener {
    private EditText mUser;
    private EditText mPassword;
    private static final String USER_DETAIL = "userDetails";
    private static final String USER_INFO = "userinfo";
    List<FanfouUserInfo> accountsInfo;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        setTitle(getString(R.string.loginPage));
        mUser = (EditText) findViewById(R.id.username);
        mUser.setHint(R.string.input_Username);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword.setHint(R.string.input_Password);
        ListView accounts = (ListView) findViewById(R.id.accountList);
        SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        if (!accountInfo.contains(USER_INFO)) {
            accounts.setVisibility(View.GONE);
        } else {
            String accountsInfoString = accountInfo.getString(USER_INFO, null);
            Gson gson = new Gson();
            Type typeOfHashMap = new TypeToken<HashMap<String, FanfouUserInfo>>() { }.getType();
            HashMap<String, FanfouUserInfo> userAccountsInfo = gson.fromJson(accountsInfoString, typeOfHashMap);
            accountsInfo = new ArrayList<FanfouUserInfo>(userAccountsInfo.values());
            accounts.setAdapter(new AccountListAdaptor(this, accountsInfo));
            accounts.setOnItemClickListener(this);
        }
    }

    public void toLogin(View v) {
        hideSoftKeyboard(this);
        OauthTokenRequest tokenRequest = new OauthTokenRequest();
        String currentUsername = mUser.getText().toString();
        String currentPassword = mPassword.getText().toString();
        tokenRequest.mUsernameInput = currentUsername;
        tokenRequest.mPasswordInput = currentPassword;
        tokenRequest.context = this;
        try {
            tokenRequest.execute();
        } catch (Exception e) {
            Log.e("IO exception", "issue", e);
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
        Intent intent = new Intent(v.getContext(), DisplayTimelineActivity.class);
        intent.putExtra(UserTimelineActivity.API, accountsInfo.get(position).getAPI());
        v.getContext().startActivity(intent);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}