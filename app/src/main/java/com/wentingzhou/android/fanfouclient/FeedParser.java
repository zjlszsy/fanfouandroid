package com.wentingzhou.android.fanfouclient;

import android.util.Xml;
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

    public List parse(InputStream in) throws XmlPullParserException, IOException {
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

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List statusList = new ArrayList();
        parser.require(XmlPullParser.START_TAG, ns, "statuses");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("status")) {
                statusList.add(readStatus(parser));
            } else {
                skip(parser);
            }
        }
        return statusList;
    }

    private Status readStatus (XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "status");

        String text = null;
        String userNickName = null;
        String profileImageLink = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("source")) {
                text = readStatusText(parser);
            } else if (name.equals("name")) {
                userNickName = userNickName(parser);
            } else if (name.equals("profile_image_url")) {
                profileImageLink = readImageLink(parser);
            } else {
                skip(parser);
            }
        }
        return new Status(text, userNickName, profileImageLink);
    }

    private String readStatusText(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "source");
        String statusText = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "source");
        return statusText;
    }

    private String readImageLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "profile_image_url");
        String ImageLink = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "profile_image_url");
        return ImageLink;
    }

    private String userNickName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String nickName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return nickName;
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
