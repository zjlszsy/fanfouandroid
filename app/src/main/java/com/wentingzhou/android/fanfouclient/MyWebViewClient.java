package com.wentingzhou.android.fanfouclient;

import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by wendyzhou on 3/23/2017.
 */

public class MyWebViewClient extends WebViewClient {

    public String mUsernameInput;
    public String mPasswordInput;

    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {
        handler.proceed(mUsernameInput, mPasswordInput);
    }
}