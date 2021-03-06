package com.wentingzhou.android.fanfouclient;

import android.media.Image;
import android.util.Xml;
import com.wentingzhou.android.fanfouclient.model.FanfouStatus;
import com.wentingzhou.android.fanfouclient.model.FanfouUserInfo;
import com.wentingzhou.android.fanfouclient.model.ImageURLS;

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
    private static final String NAME_TAG = "name";
    private static final String USER_IMAGE_TAG = "profile_image_url";
    private static final String TEXT_TAG = "text";
    private static final String STATUS_TAG = "status";
    private static final String STATUSES_TAG = "statuses";
    private static final String USER_TAG = "user";
    private static final String USER_ID_TAG = "id";
    private static final String MESSAGE_ID_TAG = "id";
    private static final String PHOTO_TAG = "photo";
    private static final String IMAGE_URL_TAG = "imageurl";
    private static final String IMAGE__LARGE_URL_TAG = "largeurl";

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
        parser.require(XmlPullParser.START_TAG, ns, STATUSES_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(STATUS_TAG)) {
                fanfouStatusList.add(readStatus(parser));
            } else {
                skip(parser);
            }
        }
        return fanfouStatusList;
    }

    private FanfouStatus readStatus(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, STATUS_TAG);
        String text = null;
        FanfouUserInfo userInfo = null;
        String statusID = null;
        String photo_URL = null;
        String photo_large_URL = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TEXT_TAG)) {
                text = readStatusText(parser);
            } else if (name.equals(PHOTO_TAG)) {
                ImageURLS imageURLS = readImageURL(parser);
                photo_URL = imageURLS.imageURL;
                photo_large_URL = imageURLS.largeURL;
            } else if (name.equals(USER_TAG)) {
                userInfo = readUserInfo(parser);
            } else if (name.equals(MESSAGE_ID_TAG)) {
                statusID = readStatusID(parser);
            } else {
                skip(parser);
            }
        }
        return new FanfouStatus(text, statusID, userInfo, photo_URL, photo_large_URL);
    }

    private FanfouUserInfo readUserInfo(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, USER_TAG);
        String userNickName = null;
        String profileImageLink = null;
        String userID = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(NAME_TAG)) {
                userNickName = userNickName(parser);
            } else if (name.equals(USER_IMAGE_TAG)) {
                profileImageLink = readImageLink(parser);
            } else if (name.equals(USER_ID_TAG)) {
                userID = readUserID(parser);
            } else {
                skip(parser);
            }
        }
        return new FanfouUserInfo(userNickName, profileImageLink, userID);
    }

    private ImageURLS readImageURL(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, PHOTO_TAG);
        String image_URL = null;
        String large_URL = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(IMAGE_URL_TAG)) {
                image_URL = readStatusImageURL(parser);
            } else if (name.equals(IMAGE__LARGE_URL_TAG)) {
                large_URL = readStatusImageLargeURL(parser);
            }else {
                skip(parser);
            }
        }
        return new ImageURLS(image_URL, large_URL);
    }

    private String readStatusImageLargeURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, IMAGE__LARGE_URL_TAG);
        String ImageLink = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, IMAGE__LARGE_URL_TAG);
        return ImageLink;
    }


    private String readStatusImageURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, IMAGE_URL_TAG);
        String ImageLink = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, IMAGE_URL_TAG);
        return ImageLink;
    }


    private String readStatusID(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, MESSAGE_ID_TAG);
        String statusID = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, MESSAGE_ID_TAG);
        return statusID;
    }

    private String readImageLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, USER_IMAGE_TAG);
        String ImageLink = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, USER_IMAGE_TAG);
        return ImageLink;
    }

    private String readUserID(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, USER_ID_TAG);
        String UserID = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, USER_ID_TAG);
        return UserID;
    }

    private String userNickName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, NAME_TAG);
        String nickName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, NAME_TAG);
        return nickName;
    }

    private String readStatusText(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TEXT_TAG);
        String statusText = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TEXT_TAG);
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
