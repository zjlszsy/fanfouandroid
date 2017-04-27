package com.wentingzhou.android.fanfouclient;

import org.oauthsimple.http.OAuthRequest;
import org.oauthsimple.http.Parameter;
import org.oauthsimple.http.Verb;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wendyzhou on 4/26/2017.
 */

public class RequestBuilder {
    private String url;
    private Verb verb;
    private List<Parameter> params;

    public RequestBuilder() {
        params = new ArrayList<Parameter>();
    }

    public void setURL(String url) {
        this.url = url;
    }

    public void verb(Verb verb) {
        this.verb = verb;
    }

    public OAuthRequest build() {
        OAuthRequest request = new OAuthRequest(verb, url);
        if (Verb.GET == verb || Verb.DELETE == verb) {
            for (Parameter param : params) {
                request.addParameter(param);
            }
        } else {
            for (Parameter param : params) {
                request.addParameter(param);
            }
        }
        return request;
    }

}
