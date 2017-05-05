package com.wentingzhou.android.fanfouclient;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by wendyzhou on 4/21/2017.
 */

public class MentionListParser {
    private static final String ns = null;
    private static final String USERS_TAG = "users";
    private static final String SINGLE_USER_TAG = "user";
    private static final String NAME_TAG = "name";


    public ArrayList<String> parse(InputStream in) throws XmlPullParserException, IOException {
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

    private ArrayList<String> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<String> mentionNameList  = new ArrayList<String>();
        parser.require(XmlPullParser.START_TAG, ns, USERS_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(SINGLE_USER_TAG)) {
                mentionNameList.add(readUserName(parser));
            } else {
                skip(parser);
            }
        }
        return mentionNameList;
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

    private String readUserName(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, SINGLE_USER_TAG);
        String text = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(NAME_TAG)) {
                text = readName(parser);
            } else {
                skip(parser);
            }
        }
        return text;
    }

    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, NAME_TAG);
        String mentionUserName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, NAME_TAG);
        return mentionUserName;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
