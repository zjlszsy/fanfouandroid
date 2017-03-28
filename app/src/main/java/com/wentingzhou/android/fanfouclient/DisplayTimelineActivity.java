package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class DisplayTimelineActivity extends Activity {
  private WebView browser;
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);
    browser = (WebView)findViewById(R.id.webkit);
    MyWebViewClient myclient = new MyWebViewClient ();
    myclient.mUsernameInput = getIntent().getStringExtra(USERNAME);
    myclient.mPasswordInput = getIntent().getStringExtra(PASSWORD);
    browser.setWebViewClient(myclient);
    browser.loadUrl("http://api.fanfou.com/statuses/friends_timeline.xml");
  }
}
