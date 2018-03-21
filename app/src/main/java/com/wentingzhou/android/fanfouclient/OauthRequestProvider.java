package com.wentingzhou.android.fanfouclient;

/**
 * Created by wenting on 3/20/18.
 */

public class OauthRequestProvider {
    private String currentUsername;
    private String currentPassword;
    private String REQUEST_FAILED;
    private String REQUEST_SUCCEEDED;

    OauthRequestProvider(String currentUsername, String currentPassword,
                         String REQUEST_FAILED, String REQUEST_SUCCEEDED) {
        this.currentUsername = currentUsername;
        this.currentPassword = currentPassword;
        this.REQUEST_FAILED = REQUEST_FAILED;
        this.REQUEST_SUCCEEDED = REQUEST_SUCCEEDED;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getRequestFailed() {
        return REQUEST_FAILED;
    }

    public String getRequestSucceeded() {
        return REQUEST_SUCCEEDED;
    }
}