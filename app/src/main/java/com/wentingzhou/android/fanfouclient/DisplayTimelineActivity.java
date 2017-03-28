package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;


public class DisplayTimelineActivity extends Activity {
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);
    WebView browser = (WebView) findViewById(R.id.webkit);
    MyWebViewClient myClient = new MyWebViewClient();
    myClient.mUsernameInput = getIntent().getStringExtra(USERNAME);
    myClient.mPasswordInput = getIntent().getStringExtra(PASSWORD);
    browser.setWebViewClient(myClient);
    browser.loadUrl("http://api.fanfou.com/statuses/friends_timeline.xml");
    HttpRequest request = new HttpRequest();
    request.mUsernameInput = getIntent().getStringExtra(USERNAME);
    request.mPasswordInput = getIntent().getStringExtra(PASSWORD);
    try {
      request.execute("http://api.fanfou.com/statuses/friends_timeline.xml");
    } catch (Exception e) {
      Log.e("Exception", "detail", e);
    }
  }

}
