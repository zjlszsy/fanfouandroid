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
import android.widget.ProgressBar;
import android.widget.Toast;

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



public class LoginPageActivity extends Activity implements OnOauthTaskCompleted {
    private EditText mUser;
    private EditText mPassword;
    private static final String USER_DETAIL = "userDetails";
    private static final String USER_INFO = "userinfo";
    List<FanfouUserInfo> accountsInfo;
    ProgressBar pb;
    FanfouUserInfo userInfo;
    Type typeOfHashMap = new TypeToken<HashMap<String, FanfouUserInfo>>() { }.getType();
    HashMap<String, FanfouUserInfo> userAccountsInfo;
    OauthRequestProvider oauthRequestProvider;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        setTitle(getString(R.string.loginPage));
        mUser = (EditText) findViewById(R.id.username);
        mUser.setHint(R.string.input_Username);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword.setHint(R.string.input_Password);
        loadSharedPreference();
    }

    public void loadSharedPreference() {
        ListView accounts = (ListView) findViewById(R.id.accountList);
        SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        if (!accountInfo.contains(USER_INFO)) {
            accounts.setVisibility(View.GONE);
        } else {
            accounts.setVisibility(View.VISIBLE);
            String accountsInfoString = accountInfo.getString(USER_INFO, null);
            Gson gson = new Gson();
            userAccountsInfo = gson.fromJson(accountsInfoString, typeOfHashMap);
            accountsInfo = new ArrayList<FanfouUserInfo>(userAccountsInfo.values());
            accounts.setAdapter(new AccountListAdaptor(this, accountsInfo));
        }
    }

    public void toLogin(View v) {
        hideSoftKeyboard(this);

        oauthRequestProvider = new OauthRequestProvider(mUser.getText().toString(), mPassword.getText().toString(),
                getResources().getString(R.string.requestFailed), getResources().getString(R.string.requestSucceeded));
        OauthTokenRequest tokenRequest = new OauthTokenRequest(this, oauthRequestProvider);
        tokenRequest.pb = pb;
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void clearEditText() {
        mUser.getText().clear();
        mPassword.getText().clear();
    }

    @Override
    public void onTaskCompleted(String response) {
        if (response.equals(oauthRequestProvider.getRequestFailed())) {
            Toast.makeText(this, "Invalid Login Information. Please try again.",
                    Toast.LENGTH_LONG).show();
        } else if (response.equals(oauthRequestProvider.getRequestSucceeded())) {
            SharedPreferences accountInfo = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
            String accountsInfoString = accountInfo.getString(USER_INFO, null);
            Gson gson = new Gson();
            if (!accountInfo.contains(USER_INFO)) {
                HashMap<String, FanfouUserInfo> userAccountsInfo = new HashMap<String, FanfouUserInfo>();
                userAccountsInfo.put(mUser.getText().toString(), userInfo);
                accountsInfoString = gson.toJson(userAccountsInfo);
            } else {
                HashMap<String, FanfouUserInfo> userAccountsInfo = gson.fromJson(accountsInfoString, typeOfHashMap);
                if (!userAccountsInfo.containsKey(oauthRequestProvider.getCurrentUsername())){
                    userAccountsInfo.put(oauthRequestProvider.getCurrentUsername(), userInfo);
                    Gson gson2 = new Gson();
                    accountsInfoString = gson2.toJson(userAccountsInfo);
                }
            }
            SharedPreferences.Editor edit = accountInfo.edit();
            edit.putString(USER_INFO, accountsInfoString);
            edit.commit();
            loadSharedPreference();
            clearEditText();
            Intent timeline = new Intent(this, DisplayTimelineActivity.class);
            timeline.putExtra(DisplayTimelineActivity.API, userInfo.getAPI());
            startActivity(timeline);
        }
    }

    @Override
    public void setUserInfo(FanfouUserInfo userInfo) {
        this.userInfo = userInfo;
    }
}