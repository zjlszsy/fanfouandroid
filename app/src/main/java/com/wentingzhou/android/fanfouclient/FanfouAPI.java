package com.wentingzhou.android.fanfouclient;

import android.util.Log;

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

public class FanfouAPI {
    private static final String API_KEY = "c883af81507f2a23f8aa61520f04f9d3";
    private static final String API_SECRET = "92c02d88f59f588039c419540371a1dd";
    private static final String CALLBACK_URL = "http://m.fanfou.com";
    private OAuthService mOAuthService;
    private OAuthToken mAccessToken;

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
}
