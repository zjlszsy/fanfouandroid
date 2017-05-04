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
import java.io.IOException;
import java.io.PrintStream;


/**
 * Created by wendyzhou on 4/25/2017.
 */

public class FanfouAPI implements Parcelable {
    private static String API_KEY = "c883af81507f2a23f8aa61520f04f9d3";
    private static String API_SECRET = "92c02d88f59f588039c419540371a1dd";
    private static String CALLBACK_URL = "http://m.fanfou.com";
    private OAuthService mOAuthService;
    private OAuthToken mAccessToken;
    private String url;
    private String tokenString;

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

    public String fetchTimeline(String url) {
        RequestBuilder builder = new RequestBuilder();
        builder.setURL(url);
        builder.verb(Verb.GET);
        return fetch(builder);
    }

    public String postNewStatus(String status, String url) {
        RequestBuilder builder = new RequestBuilder();
        builder.setURL(url);
        builder.verb(Verb.POST);
        builder.status(status);
        return fetch(builder);

    }

    public OAuthToken getAccessToken() {
        return mAccessToken;
    }

    public void updateURL(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }

    protected FanfouAPI(Parcel in) {
        API_KEY = in.readString();
        API_SECRET = in.readString();
        CALLBACK_URL = in.readString();
        tokenString = in.readString();
        url = in.readString();
        mOAuthService = buildOAuthService();
        mAccessToken = new Gson().fromJson(tokenString, OAuthToken.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(API_KEY);
        dest.writeString(API_SECRET);
        dest.writeString(CALLBACK_URL);
        dest.writeString(tokenString);
        dest.writeString(url);
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