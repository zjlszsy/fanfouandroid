package com.wentingzhou.android.fanfouclient;

import android.util.Xml;

import com.wentingzhou.android.fanfouclient.model.FanfouUserInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wendyzhou on 5/8/2017.
 */

public class AccountInfoParser {
    private static final String ns = null;
    private static final String USER_ID_TAG = "id";
    private static final String NAME_TAG = "name";
    private static final String USER_IMAGE_TAG = "profile_image_url";
    private static final String USER_TAG = "user";

    public FanfouUserInfo parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private FanfouUserInfo readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, USER_TAG);
        String userID = null;
        String userName = null;
        String userImage = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(USER_ID_TAG)) {
                userID = readUserID(parser);
            } else if (name.equals(NAME_TAG)) {
                userName = readNameTag(parser);
            } else if (name.equals(USER_IMAGE_TAG)) {
                userImage = readUserImage(parser);
            } else {
                skip(parser);
            }
        }
        return new FanfouUserInfo(userName, userImage, userID);
    }

    private String readUserImage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, USER_IMAGE_TAG);
        String ImageLink = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, USER_IMAGE_TAG);
        return ImageLink;
    }

    private String readNameTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, NAME_TAG);
        String nickName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, NAME_TAG);
        return nickName;
    }

    private String readUserID(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, USER_ID_TAG);
        String UserID = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, USER_ID_TAG);
        return UserID;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}
