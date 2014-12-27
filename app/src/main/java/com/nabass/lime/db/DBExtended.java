package com.nabass.lime.db;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class DBExtended {
    public DBExtended() {

    }

    public static String getEmailFromProfileID(ContentResolver cr, String profileId) {
        String profileEmail = null;
        Cursor c = cr.query(Uri.withAppendedPath(CustomCP.CONTENT_URI_PROFILE, profileId), null, null, null, null);
        if (c.moveToFirst()) {
            profileEmail = c.getString(c.getColumnIndex(CustomCP.COL_EMAIL));
        }
        return profileEmail;
    }

    public static void deleteConversationByEmail(ContentResolver cr, String email) {
        String where = CustomCP.COL_SENDER_EMAIL + "=?";
        String[] selectionArgs = {email};
        int res = cr.delete(CustomCP.CONTENT_URI_MESSAGES, where, selectionArgs);
    }
}
