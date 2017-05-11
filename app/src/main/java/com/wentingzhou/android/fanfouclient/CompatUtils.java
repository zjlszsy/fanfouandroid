package com.wentingzhou.android.fanfouclient;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class CompatUtils {
    private static String SELECTION = "_id=?";
    private static String SPLIT_CHAR = ":";
    private static final String COLUMN = "_data";


    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(SPLIT_CHAR);
            Uri contentUri  = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            final String[] selectionArgs = new String[]{
                    split[1]
            };
            return getDataColumn(context, contentUri, SELECTION, selectionArgs);
        }
        return uri.getPath();
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String[] projection = {
                COLUMN
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(COLUMN);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
