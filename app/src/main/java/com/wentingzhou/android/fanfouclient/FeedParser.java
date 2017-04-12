package com.wentingzhou.android.fanfouclient;

import android.util.Log;
import android.util.Xml;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import com.wentingzhou.android.fanfouclient.model.FanfouUserInfo;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wendyzhou on 3/29/2017.
 */

public class FeedParser {
    private static final String ns = null;
    private static final String NAMETAG = "name";
    private static final String USERIMAGETAG = "profile_image_url";
    private static final String TEXTTAG = "text";
    private static final String STATUSTAG = "status";
    private static final String STATUSESTAG = "statuses";
    private static final String USERTAG = "user";
    private static final String USER_ID_TAG = "id";
    private static final String MESSAGE_ID_TAG = "id";

    public List<FanfouStatus> parse(InputStream in) throws XmlPullParserException, IOException {
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

    private List<FanfouStatus> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<FanfouStatus> fanfouStatusList = new ArrayList<FanfouStatus>();
        parser.require(XmlPullParser.START_TAG, ns, STATUSESTAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(STATUSTAG)) {
                fanfouStatusList.add(readStatus(parser));
            } else {
                skip(parser);
            }
        }
        return fanfouStatusList;
    }

    private FanfouStatus readStatus(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, STATUSTAG);
        String text = null;
        FanfouUserInfo userInfo = null;
        String statusID = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TEXTTAG)) {
                text = readStatusText(parser);
            } else if (name.equals(USERTAG)) {
                userInfo = readUserInfo(parser);
            } else if (name.equals(MESSAGE_ID_TAG)) {
                statusID = readStatusID(parser);
            }else {
                skip(parser);
            }
        }
        return new FanfouStatus(text, statusID, userInfo);
    }

    private FanfouUserInfo readUserInfo(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, USERTAG);
        String userNickName = null;
        String profileImageLink = null;
        String userID = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(NAMETAG)) {
                userNickName = userNickName(parser);
            } else if (name.equals(USERIMAGETAG)) {
                profileImageLink = readImageLink(parser);
            } else if (name.equals(USER_ID_TAG)) {
                userID = readUserID(parser);
            }
            else {
                skip(parser);
            }
        }
        return new FanfouUserInfo(userNickName, profileImageLink, userID);
    }

    private String readStatusID(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, MESSAGE_ID_TAG);
        String statusID = readText(parser);
        Log.e("status ID in ", statusID);
        parser.require(XmlPullParser.END_TAG, ns, MESSAGE_ID_TAG);
        return statusID;
    }

    private String readImageLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, USERIMAGETAG);
        String ImageLink = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, USERIMAGETAG);
        return ImageLink;
    }

    private String readUserID(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, USER_ID_TAG);
        String UserID = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, USER_ID_TAG);
        return UserID;
    }

    private String userNickName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, NAMETAG);
        String nickName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, NAMETAG);
        return nickName;
    }

    private String readStatusText(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TEXTTAG);
        String statusText = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TEXTTAG);
        return statusText;
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
