package com.wentingzhou.android.fanfouclient;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;

import org.oauthsimple.builder.ServiceBuilder;
import org.oauthsimple.http.OAuthRequest;
import org.oauthsimple.http.Response;
import org.oauthsimple.http.Verb;
import org.oauthsimple.model.OAuthToken;
import org.oauthsimple.model.SignatureType;
import org.oauthsimple.oauth.OAuthService;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;


/**
 * Created by wendyzhou on 4/25/2017.
 */

public class FanfouAPI implements Parcelable {
    private static String API_KEY = "c883af81507f2a23f8aa61520f04f9d3";
    private static String API_SECRET = "92c02d88f59f588039c419540371a1dd";
    private static String CALLBACK_URL = "http://m.fanfou.com";
    private OAuthService mOAuthService;
    private OAuthToken mAccessToken;
    private String tokenString;
    public final String TIMELINEURL = "http://api.fanfou.com/statuses/friends_timeline.xml";
    public final String FRIENDlISTURL = "http://api.fanfou.com/users/friends.xml";
    public final String MORE_TIMELINE_URL = "http://api.fanfou.com/statuses/friends_timeline.xml?max_id=%s";
    private final String USERTIMELINE_URL = "http://api.fanfou.com/statuses/user_timeline.xml?id=%s";
    public static final String POST_URL = "http://api.fanfou.com/statuses/update.xml";

    public static final String PHOTO_URL = "http://api.fanfou.com/photos/upload.xml";
    private String PHOTO_KEY = "photo";
    public final String VERIFY_ACCOUNT_INFO = "http://api.fanfou.com/account/verify_credentials.xml";


    public FanfouAPI() {
        this.mOAuthService = buildOAuthService();
    }

    private OAuthService buildOAuthService() {
        ServiceBuilder builder = new ServiceBuilder().apiKey(API_KEY)
                .apiSecret(API_SECRET).callback(CALLBACK_URL)
                .provider(org.oauthsimple.builder.api.FanfouApi.class)
                .signatureType(SignatureType.HEADER_OAUTH);

        builder.debug().debugStream(new PrintStream(System.out));
        return builder.build();
    }

    public void setAccessToken(OAuthToken token) {
        this.mAccessToken = token;
        Gson gson = new Gson();
        tokenString = gson.toJson(token);
    }

    public OAuthToken getOAuthAccessToken(String username, String password)
            throws IOException {
        try {
            return mOAuthService.getAccessToken(username, password);
        } catch (Exception e) {
            Log.e("Issue", "msg", e);
        }
        return null;
    }

    public String fetch(RequestBuilder builder) {
        OAuthRequest request = builder.build();
        mOAuthService.signRequest(mAccessToken, request);
        try {
            Response response = request.send();
            int statusCode = response.getCode();
            String body = response.getBody();
            return body;
        } catch (IOException e) {
            Log.e("Issue", "IO Exception");
        }
        return null;
    }

    public String fetchTimeline() {
        RequestBuilder builder = new RequestBuilder();
        builder.setURL(TIMELINEURL);
        builder.verb(Verb.GET);
        return fetch(builder);
    }

    public String verifyAccountInfo() {
        RequestBuilder builder = new RequestBuilder();
        builder.setURL(VERIFY_ACCOUNT_INFO);
        builder.verb(Verb.GET);
        return fetch(builder);
    }

    public String fetchMoreTimeline(String id) {
        RequestBuilder builder = new RequestBuilder();
        builder.setURL(String.format(Locale.US, MORE_TIMELINE_URL, id));
        builder.verb(Verb.GET);
        return fetch(builder);
    }

    public String fetchFriendsList() {
        RequestBuilder builder = new RequestBuilder();
        builder.setURL(FRIENDlISTURL);
        builder.verb(Verb.GET);
        return fetch(builder);
    }

    public String fetchUserTimeline(String id) {
        RequestBuilder builder = new RequestBuilder();
        String url = String.format(Locale.US, USERTIMELINE_URL, id);
        builder.setURL(url);
        builder.verb(Verb.GET);
        return fetch(builder);
    }

    public String postNewStatus(String status) {
        RequestBuilder builder = new RequestBuilder();
        builder.setURL(POST_URL);
        builder.verb(Verb.POST);
        builder.status(status);
        return fetch(builder);

    }

    public String uploadPhoto(File photo, String status) {
        RequestBuilder builder = new RequestBuilder();
        builder.setURL(PHOTO_URL);
        builder.verb(Verb.POST);
        builder.status(status);
        builder.file(PHOTO_KEY, photo);
        return fetch(builder);
    }

    public OAuthToken getAccessToken() {
        return mAccessToken;
    }

    protected FanfouAPI(Parcel in) {
        tokenString = in.readString();
        mOAuthService = buildOAuthService();
        mAccessToken = new Gson().fromJson(tokenString, OAuthToken.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tokenString);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FanfouAPI> CREATOR = new Parcelable.Creator<FanfouAPI>() {
        @Override
        public FanfouAPI createFromParcel(Parcel in) {
            return new FanfouAPI(in);
        }

        @Override
        public FanfouAPI[] newArray(int size) {
            return new FanfouAPI[size];
        }
    };
}