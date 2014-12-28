package com.nabass.lime.db;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class DBExtended {
    public DBExtended() {

    }

    public static String getEmailFromProfileID(ContentResolver cr, String profileId) {
        String profileEmail = null;
        Cursor c = cr.query(Uri.withAppendedPath(DBConstants.DB_CONTACTS, profileId), null, null, null, null);
        if (c.moveToFirst()) {
            profileEmail = c.getString(c.getColumnIndex(DBConstants.COL_EMAIL));
        }
        return profileEmail;
    }

    public static void deleteConversationByEmail(ContentResolver cr, String email) {
        String where = DBConstants.COL_SENDER_ID + "=?";
        String[] selectionArgs = {email};
        int res = cr.delete(DBConstants.DB_MSGS, where, selectionArgs);
    }
}
