package com.nabass.lime.db;


import android.content.ContentResolver;

public class DBExtended {

    public DBExtended() {

    }

    public static void clearConversationByEmail(ContentResolver cr, String email) {
        String where = DBConstants.COL_RECIPIENT_ID + "=?";
        String[] selectionArgs = {email};
        int res = cr.delete(DBConstants.DB_MSGS, where, selectionArgs);
    }
}
